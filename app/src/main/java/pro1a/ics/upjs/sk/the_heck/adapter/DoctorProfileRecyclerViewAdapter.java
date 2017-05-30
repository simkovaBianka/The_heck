package pro1a.ics.upjs.sk.the_heck.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.activity.Main;
import pro1a.ics.upjs.sk.the_heck.help.MakeAppointment;
import pro1a.ics.upjs.sk.the_heck.model.AppointmentDoctor;
import pro1a.ics.upjs.sk.the_heck.model.AppointmentUser;
import pro1a.ics.upjs.sk.the_heck.model.Appointments;

import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_ACCESS_TOKEN;

public class DoctorProfileRecyclerViewAdapter extends RecyclerView
        .Adapter<DoctorProfileRecyclerViewAdapter.NewAppointmentViewHolder> {
    Context context;
    private List<Appointments> appointmentList = new ArrayList<>();
    ;


    public DoctorProfileRecyclerViewAdapter(List<Appointments> list, Context context) {
        this.context = context;
        this.appointmentList = list;
    }

    @Override
    public NewAppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_doctor_profil, parent, false);
        NewAppointmentViewHolder holder = new NewAppointmentViewHolder(v, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewAppointmentViewHolder holder, int position) {
        String fromDate = appointmentList.get(position).getFrom();
        String toDate = appointmentList.get(position).getTo();
        String originalStringFormat = "yyyy-MM-dd' 'HH:mm:ss";
        String desiredStringFormat = "dd.MM.yyyy' 'HH:mm";

        SimpleDateFormat readingFormat = new SimpleDateFormat(originalStringFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(desiredStringFormat);
        Date dateFrom = new Date();
        Date dateTo = new Date();
        try {
            dateFrom = readingFormat.parse(fromDate);
            dateTo = readingFormat.parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.dateFromTextView.setText(outputFormat.format(dateFrom));
        holder.dateToTextView.setText(outputFormat.format(dateTo));
    }

    @Override
    public long getItemId(int position) {
        return appointmentList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    class NewAppointmentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView dateFromTextView, dateToTextView;
        Context context;
        EditText nameEditText, noteEditText, subjectEditText;
        SharedPreferences sharedPreferencesToken;

        NewAppointmentViewHolder(View itemView, Context context) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView2);
            dateFromTextView = (TextView) itemView.findViewById(R.id.dateFromTextView);
            dateToTextView = (TextView) itemView.findViewById(R.id.dateToTextView);
            this.context = context;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
            View mView = layoutInflaterAndroid.inflate(R.layout.dialog_box_make_appointment, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
            alertDialogBuilderUserInput.setView(mView);

            final int position = getAdapterPosition();

            nameEditText = (EditText) mView.findViewById(R.id.patientEditText);
            noteEditText = (EditText) mView.findViewById(R.id.noteEditText);
            subjectEditText = (EditText) mView.findViewById(R.id.subjectEditText);
            sharedPreferencesToken = context.getSharedPreferences(PREFERENCIES_ACCESS_TOKEN,
                    Context.MODE_PRIVATE);

            final AppointmentUser user = new AppointmentUser(sharedPreferencesToken.getLong("id", 0L),
                    appointmentList.get(position).getUser().getFirstName(),
                    appointmentList.get(position).getUser().getLastName());
            final AppointmentDoctor doctor = new AppointmentDoctor(appointmentList.get(position).getDoctor().getId(),
                    appointmentList.get(position).getDoctor().getFirstName(),
                    appointmentList.get(position).getDoctor().getLastName(),
                    appointmentList.get(position).getDoctor().getOffice(),
                    appointmentList.get(position).getDoctor().getSpecialization());
            final String from = appointmentList.get(position).getFrom();
            final String to = appointmentList.get(position).getTo();


            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Send", null)
                    .setNegativeButton("Cancel", null);

            final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {

                    Button b = alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            String patient = nameEditText.getText().toString().trim();
                            String note = noteEditText.getText().toString().trim();
                            String subject = subjectEditText.getText().toString().trim();
                            if (!patient.equals("") && !subject.equals("")) {
                                MakeAppointment.makeAppointment(from, to, user, doctor, patient, note,
                                        subject, sharedPreferencesToken.getString("token", null));
                                if (MakeAppointment.result()) {
                                    Toast.makeText(context, "Added appointment",
                                            Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, Main.class);
                                    context.startActivity(intent);
                                    alertDialogAndroid.dismiss();
                                }
                            } else {
                                Toast.makeText(context, "Please enter a patient name and subject",
                                        Toast.LENGTH_LONG).show();

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
    }
}
