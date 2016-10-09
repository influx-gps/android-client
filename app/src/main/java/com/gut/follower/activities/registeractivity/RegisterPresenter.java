package com.gut.follower.activities.registeractivity;

import com.gut.follower.model.Account;
import com.gut.follower.utility.AuthenticationManager;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gut.follower.commons.InputValidator.checkIfUserCredentialsNotEmpty;
import static com.gut.follower.commons.InputValidator.createAccount;

public class RegisterPresenter implements RegisterContract.Presenter{

    private RegisterContract.View view;

    @Override
    public void attachView(RegisterContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void register(final String username, final String password, String email) {
        if(checkIfUserCredentialsNotEmpty(username, password, email)){
            view.hideKeyboard();
            view.showLoadingSpinner();
            view.hideUserRegisterForm();
            Account account = createAccount(username, password, email);
            JConductorService restApi = ServiceGenerator.createService(JConductorService.class);
            Call<Account> call = restApi.register(account);
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        AuthenticationManager.login(view.getContext(), username, password);
                    } else {
                        view.showToast(response.message());
                        view.hideLoadingSpinner();
                        view.showUserRegisterForm();
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    view.showToast(t.getMessage());
                    view.hideLoadingSpinner();
                    view.showUserRegisterForm();
                }
            });
        }
    }
}
