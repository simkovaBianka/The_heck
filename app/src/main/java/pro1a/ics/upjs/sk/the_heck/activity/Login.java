package pro1a.ics.upjs.sk.the_heck.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.retrofit.RetrofitFactory;
import pro1a.ics.upjs.sk.the_heck.model.Token;
import pro1a.ics.upjs.sk.the_heck.retrofit.HeckAPI;
import pro1a.ics.upjs.sk.the_heck.retrofit.SessionManager;
import pro1a.ics.upjs.sk.the_heck.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_ACCESS_TOKEN;

public class Login extends AppCompatActivity {
    private Button loginButton,linkToRegisterButton;
    private EditText inputLoginEditText, inputPasswordEditText;
    private TextView textViewHeck;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    public SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(Login.this, Main.class);
            startActivity(intent);
            finish();
        }
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputLoginEditText.getText().toString().trim();
                String password = inputPasswordEditText.getText().toString().trim();
                if (!username.isEmpty() && !password.isEmpty()) {
                    login(username, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        linkToRegisterButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        Register.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void init() {
        inputLoginEditText = (EditText) findViewById(R.id.loginEditText);
        inputPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        linkToRegisterButton = (Button) findViewById(R.id.linkToRegisterButton);
        this.sharedPreferences = getSharedPreferences(PREFERENCIES_ACCESS_TOKEN, Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(getApplicationContext());

        textViewHeck = (TextView) findViewById(R.id.heck);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/heck.ttf");
        textViewHeck.setTypeface(font);
    }

    private void login(String username, String password) {
        progressDialog.setMessage("Logging in ...");
        showDialog();
        HeckAPI api = RetrofitFactory.getApi();
        User user = new User(username, password);
        Call<Token> call = api.loginTo(user);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    Token token = response.body();
                    saveToPreferencies(token);
                    sessionManager.setLogin(true);
                    Intent intent = new Intent(Login.this,
                            Main.class);
                    startActivity(intent);
                    hideDialog();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong password or login", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e("error", t.toString());
            }
        });

    }

    private void saveToPreferencies(Token token) {
        this.sharedPreferences
                .edit()
                .putLong("id", token.getId())
                .putString("token", token.getToken())
                .putString("role", token.getRole())
                .putString("login", token.getLogin())
                .apply();
    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}

