package pro1a.ics.upjs.sk.the_heck.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.adapter.DoctorProfileRecyclerViewAdapter;
import pro1a.ics.upjs.sk.the_heck.adapter.FavDoctorRecyclerViewAdapter;
import pro1a.ics.upjs.sk.the_heck.model.Appointments;
import pro1a.ics.upjs.sk.the_heck.retrofit.RetrofitFactory;
import pro1a.ics.upjs.sk.the_heck.retrofit.SessionManager;
import pro1a.ics.upjs.sk.the_heck.retrofit.HeckAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_ACCESS_TOKEN;
import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_FILTERS;

public class DoctorProfile extends AppCompatActivity {
    private TextView specializationTextView, fullNameTextView,
            phoneTextView, emailTextView,
            ambulanciaTextView;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private DoctorProfileRecyclerViewAdapter adapter;
    private FavDoctorRecyclerViewAdapter adapterFavDoc;
    private List<Appointments> appointmentList;
    private SharedPreferences sharedPreferencesToken;
    private SharedPreferences sharedPreferencesDate;
    private Long idDoctor;
    private Intent intent;
    private HeckAPI api;
    public ImageView starImageView;
    private boolean fav;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorprofile);
        init();
        recyclerView = (RecyclerView) findViewById(R.id.workingTimeRecyclerView);
        String fromDate = sharedPreferencesDate.getString("fromDate", null);
        String toDate = sharedPreferencesDate.getString("toDate", null);
        Call<List<Appointments>> call = api.getAppointmentsForDoctor("Bearer " +
                        sharedPreferencesToken.getString("token", null),
                idDoctor, sharedPreferencesToken.getLong("id", 0L), fromDate, toDate);
        call.enqueue(new Callback<List<Appointments>>() {
            @Override
            public void onResponse(Call<List<Appointments>> call,
                                   Response<List<Appointments>> response) {
                if (response.isSuccessful()) {
                    appointmentList = new ArrayList<>();
                    appointmentList = response.body();
                    initAdapter();
                }
            }

            @Override
            public void onFailure(Call<List<Appointments>> call, Throwable t) {
                Log.e("Server", t.toString());
            }
        });
    }

    private void initAdapter() {
        adapter = new DoctorProfileRecyclerViewAdapter(appointmentList, this);
        adapterFavDoc = new FavDoctorRecyclerViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    private void init() {
        intent = getIntent();
        idDoctor = (Long) intent.getLongExtra("id", 0L);
        api = RetrofitFactory.getApi();
        specializationTextView = (TextView) findViewById(R.id.specializationTextView);
        fullNameTextView = (TextView) findViewById(R.id.fullnameTextView);
        phoneTextView = (TextView) findViewById(R.id.phoneEditText);
        emailTextView = (TextView) findViewById(R.id.emailEditText);
        ambulanciaTextView = (TextView) findViewById(R.id.ambulanceTextView);
        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferencesToken = getSharedPreferences(PREFERENCIES_ACCESS_TOKEN, Context.MODE_PRIVATE);
        sharedPreferencesDate = getSharedPreferences(PREFERENCIES_FILTERS, Context.MODE_PRIVATE);
        setButton();
        setView();
        isFavourite();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setButton() {
        starImageView = (ImageView) findViewById(R.id.fab);
        starImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (starImageView.isActivated()) {
                    starImageView.setActivated(!starImageView.isActivated());
                    Call<ResponseBody> call = api.deleteFavouriteDoctor("Bearer " +
                                    sharedPreferencesToken.getString("token", null),
                            sharedPreferencesToken.getLong("id", 0L), idDoctor);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call,
                                               Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Deleted from 'My doctors'",
                                        Toast.LENGTH_LONG).show();
                                adapterFavDoc.notifyItemInserted(adapterFavDoc.getItemCount());

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("server", t.toString());
                        }
                    });

                } else {
                    starImageView.setActivated(!starImageView.isActivated());
                    Call<ResponseBody> call = api.addFavouriteDoctor("Bearer " +
                                    sharedPreferencesToken.getString("token", null),
                            sharedPreferencesToken.getLong("id", 0L), idDoctor);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call,
                                               Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Added to 'My doctors'",
                                        Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("server", t.toString());
                        }
                    });
                }
            }
        });

    }

    private void setView() {
        specializationTextView.setText(intent.getStringExtra("specialization"));
        fullNameTextView.setText(intent.getStringExtra("firstName") + " " +
                intent.getStringExtra("lastName"));
        phoneTextView.setText(intent.getStringExtra("phone"));
        emailTextView.setText(intent.getStringExtra("email"));
        ambulanciaTextView.setText(intent.getStringExtra("address") + ", " +
                intent.getStringExtra("city")
                + " " + intent.getStringExtra("postalCode"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                return true;

            case R.id.action_logout:
                SharedPreferences.Editor editorToken = sharedPreferencesToken.edit();
                editorToken.clear();
                editorToken.commit();
                SharedPreferences.Editor editorDate = sharedPreferencesDate.edit();
                editorDate.clear();
                editorDate.commit();
                sessionManager.setLogin(false);
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void isFavourite() {
        Call<Boolean> call = api.isFavouriteDoctor("Bearer " +
                        sharedPreferencesToken.getString("token", null),
                sharedPreferencesToken.getLong("id", 0L), idDoctor);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    fav = response.body();
                    if (fav) {
                        starImageView.setActivated(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("error", t.toString());
            }
        });
    }
}
