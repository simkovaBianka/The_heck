package pro1a.ics.upjs.sk.the_heck.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.adapter.FavDoctorRecyclerViewAdapter;
import pro1a.ics.upjs.sk.the_heck.model.Doctor;
import pro1a.ics.upjs.sk.the_heck.retrofit.HeckAPI;
import pro1a.ics.upjs.sk.the_heck.retrofit.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_ACCESS_TOKEN;

public class FavouriteDoctorFragment extends Fragment {
    private static final Long DEFAULT_ID_USER = 0L;
    private RecyclerView doctorView;
    private FavDoctorRecyclerViewAdapter adapter;
    private List<Doctor> doctorList;
    public SharedPreferences sharedPreferencesToken;
    public Long idUser;

    public FavouriteDoctorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        loadDoctors();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);
        doctorView = (RecyclerView) view.findViewById(R.id.doctorRecyclerView);
        adapter = new FavDoctorRecyclerViewAdapter(getContext(), doctorList);
        doctorView.setAdapter(adapter);
        doctorView.setLayoutManager(new LinearLayoutManager(getContext()));
        doctorView.setItemAnimator(new DefaultItemAnimator());
        doctorView.setHasFixedSize(true);
        return view;
    }

    private void loadDoctors() {
        HeckAPI api = RetrofitFactory.getApi();
        Call<List<Doctor>> call = api.getFavouriteDoctors("Bearer " +
                sharedPreferencesToken.getString("token", null), idUser);
        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful()) {
                    doctorList = response.body();
                    adapter = new FavDoctorRecyclerViewAdapter(getContext(), doctorList);
                    doctorView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Log.e("server", t.toString());
            }
        });

    }

    private void init() {
        doctorList = new ArrayList<>();
        sharedPreferencesToken = getContext().getSharedPreferences(PREFERENCIES_ACCESS_TOKEN,
                Context.MODE_PRIVATE);
        idUser = sharedPreferencesToken.getLong("id", DEFAULT_ID_USER);


    }

    @Override
    public void onResume() {
        super.onResume();
        loadDoctors();
    }
}
