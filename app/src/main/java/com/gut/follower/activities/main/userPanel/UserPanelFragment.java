package com.gut.follower.activities.main.userPanel;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gut.follower.R;
import com.gut.follower.model.Account;
import com.gut.follower.utility.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPanelFragment extends Fragment implements UserPanelContract.View, View.OnClickListener{

    private UserPanelContract.Presenter mPresenter;
    private Button mLogoutBtn;
    private Button mDeleteAccountBtn;
    private TextView mUsername;
    private TextView mEmail;
    private TextView mPassword;
    private ImageButton mEditEmail;
    private ImageButton mEditpassword;
    private ImageButton mShowPassword;

    public UserPanelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_panel, container, false);
        initFieldVariables(view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void initFieldVariables(View view) {
        mPresenter = new UserPanelPresenter();
        mPresenter.attachView(this);

        mLogoutBtn = (Button)view.findViewById(R.id.logout_btn);
        mDeleteAccountBtn = (Button)view.findViewById(R.id.delete_btn);
        mUsername = (TextView)view.findViewById(R.id.username);
        mEmail = (TextView)view.findViewById(R.id.email);
        mPassword = (TextView)view.findViewById(R.id.password);
        mEditEmail = (ImageButton)view.findViewById(R.id.email_edit);
        mEditpassword = (ImageButton)view.findViewById(R.id.password_edit);
        mShowPassword = (ImageButton)view.findViewById(R.id.password_show);

        mLogoutBtn.setOnClickListener(this);
        mDeleteAccountBtn.setOnClickListener(this);
        mEditEmail.setOnClickListener(this);
        mEditpassword.setOnClickListener(this);
        mShowPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logout_btn:
                mPresenter.logout();
                break;
            case R.id.delete_btn:
                Toast.makeText(getContext(), "Please, don't!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.email_edit:
                showEditEmailDialog(getString(R.string.edit_email));
                break;
            case R.id.password_edit:
                showEditPasswordDialog(getString(R.string.edit_password));
                break;
            case R.id.password_show:
                setInputType();
                break;
        }
    }

    private void setInputType() {
        if(mPassword.getInputType() == 129){
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            mShowPassword.setBackground(getResources().getDrawable(R.drawable.ic_visibility_off_black_36dp));
        } else {
            mPassword.setInputType(129);
            mShowPassword.setBackground(getResources().getDrawable(R.drawable.ic_remove_red_eye_black_36dp));
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserInfo(Account account) {
        mUsername.setText(account.getUsername());
        mPassword.setText(account.getPassword());
        mEmail.setText(account.getEmail());
    }

    public void showEditPasswordDialog(String title) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater(getArguments());
        View view = inflater.inflate(R.layout.dialog_edit_passsword, null);
        dialogBuilder.setView(view);

        final EditText oldPassword = (EditText)view.findViewById(R.id.old_password);
        final EditText newPassword = (EditText)view.findViewById(R.id.new_password);


        dialogBuilder.setTitle(title);
        dialogBuilder.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               if(validatePasswords(oldPassword, newPassword)){
                   mPresenter.editPassword(newPassword.getText().toString());
               }
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void showEditEmailDialog(String title) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater(getArguments());
        View view = inflater.inflate(R.layout.dialog_edit_email, null);
        dialogBuilder.setView(view);

        final EditText email = (EditText)view.findViewById(R.id.email);


        dialogBuilder.setTitle(title);
        dialogBuilder.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(!getString(R.string.empty_string).equals(email.getText().toString())){
                    mPresenter.editEmail(email.getText().toString());
                }
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    private boolean validatePasswords(EditText oldPassword, EditText newPassword) {
        if (oldPassword != null && newPassword != null) {
            if(oldPassword.getText().toString().equals(SessionManager.getPassword())){
                if(!newPassword.getText().toString().equals(getString(R.string.empty_string))){
                    return true;
                }
            }
        }
        return false;
    }
}
