package pro1a.ics.upjs.sk.the_heck.fragment;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.adapter.FilterDoctorRecyclerViewAdapter;
import pro1a.ics.upjs.sk.the_heck.retrofit.RetrofitFactory;
import pro1a.ics.upjs.sk.the_heck.model.Doctor;
import pro1a.ics.upjs.sk.the_heck.retrofit.HeckAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_ACCESS_TOKEN;
import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_FILTERS;

public class FilterDoctorFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
        AdapterView.OnItemSelectedListener {
    private Button setDateButton;
    private TextView textViewFrom, textViewTo, showDoctorsTextView;
    private SimpleDateFormat dateFormatter;
    private Calendar calendar, fromDateCalendar, toDateCalendar;
    private EditText cityEditText, doctorFirstNameEditText, doctorLastNameEditText;
    private Spinner spinner;
    public SharedPreferences sharedPreferencesToken, sharedPreferencesFilter;
    private ProgressDialog progressDialog;
    private RecyclerView appointmentsView;
    private FilterDoctorRecyclerViewAdapter adapterDoctors;
    private List<Doctor> doctorList;
    private String specializationSpinner;
    private boolean setDate;
    private View view;

    public FilterDoctorFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filterdoctor, container, false);
        init();
        setUpCalendar();
        setButton();
        return view;
    }

    private void setButton() {
        showDoctorsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeckAPI api = RetrofitFactory.getApi();
                String specialization = specializationSpinner;

                String doctorFirstName = null;
                if (!doctorFirstNameEditText.getText().toString().trim().equals("")) {
                    doctorFirstName = doctorFirstNameEditText.getText().toString().trim();
                }
                String doctorLastName = null;
                if (!doctorLastNameEditText.getText().toString().trim().equals("")) {
                    doctorLastName = doctorLastNameEditText.getText().toString().trim();
                }
                String city = null;
                if (!cityEditText.getText().toString().trim().equals("")) {
                    city = cityEditText.getText().toString().trim();
                }
                String fromDate = null;
                String toDate = null;

                if (setDate) {
                    fromDate = dateFormatter.format(fromDateCalendar.getTime());
                    toDate = dateFormatter.format(toDateCalendar.getTime());
                }
                Call<List<Doctor>> call = api.getDoctors("Bearer " + sharedPreferencesToken
                                .getString("token", "null"), specialization,
                        doctorFirstName, doctorLastName, city, fromDate, toDate);
                saveToPreferencies(fromDate, toDate);

                call.enqueue(new Callback<List<Doctor>>() {
                    @Override
                    public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                        if (response.isSuccessful()) {
                            doctorList = new ArrayList<>();
                            doctorList = response.body();
                            initAdapter();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Doctor>> call, Throwable t) {
                        Log.e("server", t.toString());
                    }
                });
            }
        });
    }

    private void setUpCalendar() {
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker = DatePickerDialog.newInstance(
                        listener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                picker.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    DatePickerDialog.OnDateSetListener listener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,
                                      int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
                    fromDateCalendar = Calendar.getInstance();
                    toDateCalendar = Calendar.getInstance();
                    fromDateCalendar.set(year, monthOfYear, dayOfMonth);
                    toDateCalendar.set(yearEnd, monthOfYearEnd, dayOfMonthEnd);
                    textViewFrom.setText(dateFormatter.format(fromDateCalendar.getTime()));
                    textViewTo.setText(dateFormatter.format(toDateCalendar.getTime()));
                    setDate = true;
                }
            };

    private void initAdapter() {
        appointmentsView = (RecyclerView) getView().findViewById(R.id.listDoctors);
        appointmentsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        appointmentsView.setItemAnimator(new DefaultItemAnimator());
        appointmentsView.setHasFixedSize(true);
        adapterDoctors = new FilterDoctorRecyclerViewAdapter(getActivity(), doctorList);
        appointmentsView.setAdapter(adapterDoctors);
        if (doctorList.size() == 0) {
            Toast.makeText(getActivity(), "No doctors found",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        sharedPreferencesToken = getActivity().getSharedPreferences(PREFERENCIES_ACCESS_TOKEN,
                Context.MODE_PRIVATE);
        sharedPreferencesFilter = getActivity().getSharedPreferences(PREFERENCIES_FILTERS,
                Context.MODE_PRIVATE);
        setDateButton = (Button) view.findViewById(R.id.setDateButton);
        showDoctorsTextView = (TextView) view.findViewById(R.id.showDoctorsTextView);
        textViewFrom = (TextView) view.findViewById(R.id.dateFromText);
        textViewTo = (TextView) view.findViewById(R.id.dateToText);
        cityEditText = (EditText) view.findViewById(R.id.cityEditText);
        doctorFirstNameEditText = (EditText) view.findViewById(R.id.doctorFirstNameEditText);
        doctorLastNameEditText = (EditText) view.findViewById(R.id.doctorLastNameEditText);
        spinner = (Spinner) view.findViewById(R.id.specialization_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.specializations_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd" +
                "", Locale.US);
        calendar = Calendar.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear,
                          int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

    }


    private void saveToPreferencies(String from, String to) {
        this.sharedPreferencesFilter
                .edit()
                .putString("fromDate", from)
                .putString("toDate", to)
                .apply();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        specializationSpinner = (String) parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
