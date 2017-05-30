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
import pro1a.ics.upjs.sk.the_heck.adapter.AppointmentRecyclerViewAdapter;
import pro1a.ics.upjs.sk.the_heck.model.Appointments;
import pro1a.ics.upjs.sk.the_heck.retrofit.RetrofitFactory;
import pro1a.ics.upjs.sk.the_heck.retrofit.HeckAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pro1a.ics.upjs.sk.the_heck.help.Constant.DEFAULT_ID_USER;
import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_ACCESS_TOKEN;

public class AppointmentFragment extends Fragment {
    private RecyclerView appointmentsView;
    private AppointmentRecyclerViewAdapter adapter;
    private List<Appointments> appointmentList;
    public SharedPreferences sharedPreferencesToken;
    public Long idUser;

    public AppointmentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        loadAppointments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        appointmentsView = (RecyclerView) view.findViewById(R.id.appointmentsRecyclerView);
        adapter = new AppointmentRecyclerViewAdapter(getContext(), appointmentList);
        appointmentsView.setAdapter(adapter);
        appointmentsView.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentsView.setItemAnimator(new DefaultItemAnimator());
        appointmentsView.setHasFixedSize(true);
        return view;
    }

    private void loadAppointments() {
        HeckAPI api = RetrofitFactory.getApi();
        Call<List<Appointments>> call = api.getAppointmentsForUser("Bearer " +
                        sharedPreferencesToken.getString("token", null),
                idUser);
        call.enqueue(new Callback<List<Appointments>>() {
            @Override
            public void onResponse(Call<List<Appointments>> call, Response<List<Appointments>> response) {
                if (response.isSuccessful()) {
                    appointmentList = response.body();
                    adapter = new AppointmentRecyclerViewAdapter(getContext(), appointmentList);
                    appointmentsView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Appointments>> call, Throwable t) {
                Log.e("server", t.toString());
            }
        });

    }

    private void init() {
        appointmentList = new ArrayList<>();
        sharedPreferencesToken = getActivity().getSharedPreferences(PREFERENCIES_ACCESS_TOKEN,
                Context.MODE_PRIVATE);
        idUser = sharedPreferencesToken.getLong("id", DEFAULT_ID_USER);

    }
}
