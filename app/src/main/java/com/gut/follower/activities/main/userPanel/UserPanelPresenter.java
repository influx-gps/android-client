package com.gut.follower.activities.main.userPanel;

import com.gut.follower.model.Account;
import com.gut.follower.utility.AuthenticationManager;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;
import com.gut.follower.utility.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPanelPresenter implements UserPanelContract.Presenter{

    public static String TAG = "UserPanelPresenter";

    private UserPanelContract.View view;

    @Override
    public void start() {
        loadUserInfo();
    }

    @Override
    public void editEmail(String email) {
        JConductorService restApi = getRestService();
        Call<Account> call = restApi.postAccountInfo(new Account(null, null, email));
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){
                    view.setUserInfo(response.body());
                } else {
                    view.showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                view.showToast(t.toString());
            }
        });
    }

    @Override
    public void editPassword(String password) {
        JConductorService restApi = getRestService();
        Call<Account> call = restApi.postAccountInfo(new Account(null, password, null));
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){
                    Account account = response.body();
                    view.setUserInfo(account);
                    SessionManager.saveUserCredentials(account.getUsername(), account.getPassword());
                } else {
                    view.showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    @Override
    public void loadUserInfo() {
        JConductorService restApi = getRestService();
        Call<Account> call = restApi.getAccountInfo();
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if(response.isSuccessful()){
                    view.setUserInfo(response.body());
                } else {
                    view.showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    @Override
    public void logout() {
        AuthenticationManager.logout();
    }

    @Override
    public void attachView(UserPanelContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    private JConductorService getRestService(){
        return ServiceGenerator
                .createService(JConductorService.class,
                        SessionManager.getUsername(),
                        SessionManager.getPassword());
    }
}
