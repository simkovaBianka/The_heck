package pro1a.ics.upjs.sk.the_heck.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.retrofit.RetrofitFactory;
import pro1a.ics.upjs.sk.the_heck.retrofit.SessionManager;
import pro1a.ics.upjs.sk.the_heck.model.Token;
import pro1a.ics.upjs.sk.the_heck.model.User;
import pro1a.ics.upjs.sk.the_heck.retrofit.HeckAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private Button registerButton;
    private Button linkToLoginButton;
    private EditText inputLoginEditText;
    private EditText inputNameEditText;
    private EditText inputLastNameEditText;
    private EditText inputPhoneEditText;
    private EditText inputEmailEditText;
    private EditText inputPostalCodeEditText;
    private EditText inputCityEditText;
    private EditText inputAddressEditText;
    private EditText inputPasswordEditText;
    private EditText inputConfirmPasswordEditText;
    private TextInputLayout inputEmailText;
    private TextInputLayout inputPasswordText;
    private TextInputLayout inputConfirmPasswordText;
    private SessionManager sessionManager;
    String username, name, surname, phone, email, password, confirmPassword, address,
            city, postalcode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        initButton();

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(Register.this, Main.class);
            startActivity(intent);
            finish();
        }
    }

    private void initButton() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = inputLoginEditText.getText().toString().trim();
                name = inputNameEditText.getText().toString().trim();
                surname = inputLastNameEditText.getText().toString().trim();
                phone = inputPhoneEditText.getText().toString().trim();
                email = inputEmailEditText.getText().toString().trim();
                password = inputPasswordEditText.getText().toString().trim();
                confirmPassword = inputConfirmPasswordEditText.getText().toString().trim();
                address = inputAddressEditText.getText().toString().trim();
                city = inputCityEditText.getText().toString().trim();
                postalcode = inputPostalCodeEditText.getText().toString().trim();

                if (!validateEmail()) {
                    return;
                }

                if (!validatePassword()) {
                    return;
                }
                if (!validateConfirmPassword()) {
                    return;
                }
                if (filledEditTexts(username, name, surname, phone, email, password, confirmPassword,
                        address, city, postalcode)) {
                    existLogin();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter all details", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        linkToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void existLogin() {
        HeckAPI api = RetrofitFactory.getApi();
        Call<Boolean> call = api.checkLogin(username);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    boolean valid = response.body().booleanValue();
                    if (valid) {
                        register(username, name, surname, email, phone, password, address,
                                city, postalcode);
                    } else {
                        inputLoginEditText.setError("The username already exists");
                        requestFocus(inputLoginEditText);
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("error", t.toString());
            }
        });
    }

    private boolean filledEditTexts(String username, String name, String surname, String phone,
                                    String email, String password, String confirmPassword,
                                    String address, String city, String postalcode) {
        return !username.isEmpty() && !email.isEmpty() && !password.isEmpty() &&
                !name.isEmpty() && !surname.isEmpty() && !phone.isEmpty() && !confirmPassword.isEmpty()
                && !address.isEmpty() && !city.isEmpty() && !postalcode.isEmpty();
    }


    private void init() {
        inputLoginEditText = (EditText) findViewById(R.id.usernameEditText);
        inputNameEditText = (EditText) findViewById(R.id.nameTextView);
        inputLastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        inputPhoneEditText = (EditText) findViewById(R.id.phoneEditText);
        inputEmailEditText = (EditText) findViewById(R.id.emailEditText);
        inputPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        inputPostalCodeEditText = (EditText) findViewById(R.id.postalCodeEditText);
        inputCityEditText = (EditText) findViewById(R.id.cityEditText);
        inputAddressEditText = (EditText) findViewById(R.id.addressEditText);
        inputConfirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
        registerButton = (Button) findViewById(R.id.registerButton);
        linkToLoginButton = (Button) findViewById(R.id.linkToLoginScreenButton);
        inputEmailText = (TextInputLayout) findViewById(R.id.register_email_input);
        inputPasswordText = (TextInputLayout) findViewById(R.id.register_password_input);
        inputConfirmPasswordText = (TextInputLayout) findViewById(R.id.register_confirmpassword_input);
        inputEmailEditText.addTextChangedListener(new MyTextWatcher(inputEmailEditText));
        inputPasswordEditText.addTextChangedListener(new MyTextWatcher(inputPasswordEditText));
        inputLoginEditText.addTextChangedListener(new MyTextWatcher(inputLoginEditText));
        //progressDialog = new ProgressDialog(this);
        //progressDialog.setCancelable(false);
        sessionManager = new SessionManager(getApplicationContext());
    }

    public void register(String username, String name, String surname, String email,
                         String phone, String password, String address, String city,
                         String postalCode) {
        HeckAPI api = RetrofitFactory.getApi();
        User user = new User(username, name, surname, password, address, email, phone, postalCode, city);
        Call<Token> call = api.registerTo(user);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, retrofit2.Response<Token> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration successful",
                            Toast.LENGTH_LONG).show();
                    Token accessToken = response.body();
                    Intent intent = new Intent(Register.this,
                            Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Check the details",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e("error", t.toString());
            }
        });
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public static boolean isValidEmail(String target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean validateEmail() {
        String email = inputEmailEditText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputEmailText.setError("Not valid email!");
            requestFocus(inputEmailEditText);
            return false;
        } else {
            inputEmailText.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPasswordEditText.getText().toString().trim().isEmpty() ||
                !isValidPassword(inputPasswordEditText.getText().toString().trim())) {
            inputPasswordText.setError("The password must consist of one uppercase, one lowercase, "
                    + "one digit, one special character");
            requestFocus(inputPasswordEditText);
            return false;
        } else {
            inputPasswordText.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateConfirmPassword() {
        String password = inputPasswordEditText.getText().toString().trim();
        String passwordConfirm = inputConfirmPasswordEditText.getText().toString().trim();

        if (!password.equals(passwordConfirm)) {
            inputConfirmPasswordText.setError("The password fields do not match");
            requestFocus(inputConfirmPasswordEditText);
            return false;
        } else {
            inputConfirmPasswordText.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.register_email_input:
                    validateEmail();
                    break;
                case R.id.register_password_input:
                    validatePassword();
                    break;
                case R.id.register_username_input:
                    validatePassword();
                    break;
            }
        }
    }
}
