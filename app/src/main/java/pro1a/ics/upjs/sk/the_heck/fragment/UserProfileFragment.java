package pro1a.ics.upjs.sk.the_heck.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.model.Password;
import pro1a.ics.upjs.sk.the_heck.model.User;
import pro1a.ics.upjs.sk.the_heck.retrofit.HeckAPI;
import pro1a.ics.upjs.sk.the_heck.retrofit.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_ACCESS_TOKEN;

public class UserProfileFragment extends Fragment {
    private HeckAPI api;
    private TextView fullNameTextView, phoneTextView, emailTextView,
            addressTextView, changePasswordTextView;
    private EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private SharedPreferences sharedPreferencesToken;
    private User user;
    private View view;
    private AlertDialog alertDialogAndroid;
    public boolean match;
    public String oldPassword, newPassword, confirmPassword;
    private boolean validPassword;

    public UserProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        init();
        getDetails();
        setButton();
        return view;
    }

    private void setButton() {
        changePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box_change_password, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
                alertDialogBuilderUserInput.setView(mView);

                oldPasswordEditText =
                        (EditText) mView.findViewById(R.id.passwordEditText);
                newPasswordEditText =
                        (EditText) mView.findViewById(R.id.newPasswordEditText);
                confirmPasswordEditText =
                        (EditText) mView.findViewById(R.id.confirmNewPasswordEditText);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", null)
                        .setNegativeButton("Cancel", null);

                alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button b = alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                oldPassword = oldPasswordEditText.getText().toString().trim();
                                newPassword = newPasswordEditText.getText().toString().trim();
                                confirmPassword = confirmPasswordEditText.getText().toString().trim();
                                if (validateConfirmPassword() && validateNewPassword()) {
                                    checkOldPassword(oldPassword);
                                }
                            }
                        });

                        Button c = alertDialogAndroid.getButton(AlertDialog.BUTTON_NEGATIVE);
                        c.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                alertDialogAndroid.cancel();
                            }
                        });
                    }
                });
                alertDialogAndroid.show();
            }
        });
    }

    private boolean validateNewPassword() {
        if (isValidPassword()) {
            return true;
        } else {
            Toast.makeText(getContext(), "The new password is weak",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void getDetails() {
        Call<User> call = api.getUserDetails("Bearer " +
                sharedPreferencesToken.getString("token", null), sharedPreferencesToken
                .getLong("id", 0L));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    setView();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("server", t.toString());
            }
        });
    }

    private void setView() {
        fullNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        phoneTextView.setText(user.getPhoneNumber());
        emailTextView.setText(user.getEmail());
        addressTextView.setText(user.getAddress() + ", " +
                user.getCity() + " " + user.getPostalCode());

    }

    private void init() {
        api = RetrofitFactory.getApi();
        fullNameTextView = (TextView) view.findViewById(R.id.fullnameTextView);
        phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        addressTextView = (TextView) view.findViewById(R.id.addressTextView);
        changePasswordTextView = (TextView) view.findViewById(R.id.changePasswordTextView);
        sharedPreferencesToken = getActivity().getSharedPreferences(PREFERENCIES_ACCESS_TOKEN,
                Context.MODE_PRIVATE);

    }


    private boolean validateConfirmPassword() {
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getContext(), "The password fields do not match",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void checkOldPassword(String password) {
        Call<Boolean> call = api.checkPassword(sharedPreferencesToken.getLong("id", 0L),
                password);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    match = response.body().booleanValue();
                    if (match) {
                        changePassword(oldPassword, newPassword, confirmPassword);
                        alertDialogAndroid.dismiss();
                    } else {
                        Toast.makeText(getContext(), "The old password does not match",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("server", t.toString());
            }
        });
    }


    private void changePassword(String oldPassword, String newPassword, String confirmPassword) {
        Password password = new Password(oldPassword, newPassword, confirmPassword);
        Call<ResponseBody> call = api.changePassword(sharedPreferencesToken.getLong("id", 0L),
                password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Password changed",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("server", t.toString());
            }
        });
    }

    public boolean isValidPassword() {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(newPassword);
        return matcher.matches();
    }
}
