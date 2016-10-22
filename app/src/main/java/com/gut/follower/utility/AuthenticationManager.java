package com.gut.follower.utility;

import android.content.Intent;
import android.widget.Toast;

import com.gut.follower.GutFollower;
import com.gut.follower.activities.login.LoginActivity;
import com.gut.follower.activities.main.MainActivity;
import com.gut.follower.model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gut.follower.commons.InputValidator.checkIfUserCredentialsNotEmpty;
import static com.gut.follower.commons.InputValidator.createAccount;

public class AuthenticationManager {

    public static void login(final String username, final String password){
        if(checkIfUserCredentialsNotEmpty(username, password)){
            JConductorService restApi = ServiceGenerator.createService(JConductorService.class);
            Call<Account> call = restApi.login(createAccount(username, password));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        SessionManager.saveUserCredentials(username, password);
                        Intent intent = new Intent(GutFollower.context.getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        GutFollower.context.startActivity(intent);
                    } else {
                        Toast.makeText(GutFollower.context.getApplicationContext(),
                                response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(GutFollower.context.getApplicationContext(),
                            t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(GutFollower.context.getApplicationContext(),
                    "User credentials can not be empty",
                    Toast.LENGTH_SHORT).show();
    }

    public static void logout(){
        SessionManager.clearUserCredentials();
        Intent intent = new Intent(GutFollower.context.getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        GutFollower.context.startActivity(intent);
    }

}
