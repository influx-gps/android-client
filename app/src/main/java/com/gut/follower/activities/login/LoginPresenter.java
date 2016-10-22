package com.gut.follower.activities.login;

import com.gut.follower.model.Account;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;
import com.gut.follower.utility.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gut.follower.commons.InputValidator.checkIfUserCredentialsNotEmpty;
import static com.gut.follower.commons.InputValidator.createAccount;

public class LoginPresenter implements LoginContract.Presenter{

    private LoginContract.View view;

    @Override
    public void start() {

    }

    @Override
    public void attachView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void login(final String username, final String password) {
        if(checkIfUserCredentialsNotEmpty(username, password)){
            view.hideUserLoginForm();
            view.showLoadingSpinner();
            view.hideKeyboard();
            JConductorService restApi = ServiceGenerator.createService(JConductorService.class);
            Call<Account> call = restApi.login(createAccount(username, password));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        SessionManager.saveUserCredentials(username, password);
                        view.startMainActivity();
                    } else {
                        view.hideLoadingSpinner();
                        view.showUserLoginForm();
                        view.showToast(response.message());
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    view.showToast(t.getMessage());
                    view.hideLoadingSpinner();
                    view.showUserLoginForm();
                }
            });
        }
        else {
            view.showToast("User credentials can not be empty");
        }
    }
}
