package com.gut.follower.utility;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.gut.follower.activities.login.LoginActivity;
import com.gut.follower.activities.main.MainActivity;
import com.gut.follower.model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gut.follower.commons.InputValidator.checkIfUserCredentialsNotEmpty;
import static com.gut.follower.commons.InputValidator.createAccount;

public class AuthenticationManager {

    public static void login(final Context context, final String username, final String password){
        if(checkIfUserCredentialsNotEmpty(username, password)){
            JConductorService restApi = ServiceGenerator.createService(JConductorService.class);
            Call<Account> call = restApi.login(createAccount(username, password));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        SessionManager.saveUserCredentials(context.getApplicationContext(), username, password);
                        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context.getApplicationContext(),
                                response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(context.getApplicationContext(),
                            t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(context.getApplicationContext(),
                    "User credentials can not be empty",
                    Toast.LENGTH_SHORT).show();
    }

    public static void logout(Context context){
        SessionManager.clearUserCredentials(context.getApplicationContext());
        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

}
