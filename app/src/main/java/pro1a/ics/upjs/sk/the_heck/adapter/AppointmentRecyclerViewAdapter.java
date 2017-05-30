package pro1a.ics.upjs.sk.the_heck.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.help.SpecializationIcon;
import pro1a.ics.upjs.sk.the_heck.help.UserSharedPreferences;
import pro1a.ics.upjs.sk.the_heck.model.Appointments;
import pro1a.ics.upjs.sk.the_heck.retrofit.HeckAPI;
import pro1a.ics.upjs.sk.the_heck.retrofit.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_ACCESS_TOKEN;


public class AppointmentRecyclerViewAdapter extends
        RecyclerView.Adapter<AppointmentRecyclerViewAdapter.MainViewHolder> {
    Context context;
    private List<Appointments> appointmentsList = new ArrayList<>();


    public AppointmentRecyclerViewAdapter(Context context, List<Appointments> list) {
        this.context = context;
        this.appointmentsList = list;
    }


    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_appointment, parent, false);
        MainViewHolder holder = new MainViewHolder(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.specializationTextView.setText(appointmentsList.get(position).getDoctor().getSpecialization());
        holder.nameTextView.setText(appointmentsList.get(position).getDoctor().getFirstName() +
                " " + appointmentsList.get(position).getDoctor().getLastName());

        String fromDate = appointmentsList.get(position).getFrom();
        String toDate = appointmentsList.get(position).getTo();
        String originalStringFormat = "yyyy-MM-dd' 'HH:mm:ss";
        String dayFormat = "dd.MM.yyyy";
        String timeFormat = "HH:mm";

        SimpleDateFormat readingFormat = new SimpleDateFormat(originalStringFormat);
        SimpleDateFormat outputFormatDate = new SimpleDateFormat(dayFormat);
        SimpleDateFormat outputFormatTime = new SimpleDateFormat(timeFormat);
        Date dateFrom = new Date();
        Date dateTo = new Date();
        try {
            dateFrom = readingFormat.parse(fromDate);
            dateTo = readingFormat.parse(toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.dateTextView.setText(outputFormatDate.format(dateFrom));
        holder.timeTextView.setText(outputFormatTime.format(dateFrom));
        int iconNumber = SpecializationIcon.getIconNumber(appointmentsList
                .get(position).getDoctor().getSpecialization());
        holder.iconTextView.setImageDrawable(context.getResources().getDrawable(iconNumber));


    }

    @Override
    public long getItemId(int position) {
        return appointmentsList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }


    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView specializationTextView, nameTextView, dateTextView, timeTextView;
        ImageView iconTextView;
        Context context;
        Button cancelButton;

        MainViewHolder(View itemView, final Context context) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            specializationTextView = (TextView) itemView.findViewById(R.id.detailTextView);
            timeTextView = (TextView) itemView.findViewById(R.id.timeTextView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            iconTextView = (ImageView) itemView.findViewById(R.id.iconTextView);
            cancelButton = (Button) itemView.findViewById(R.id.cancelButton);
            this.context = context;
            itemView.setOnClickListener(this);

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HeckAPI api = RetrofitFactory.getApi();
                    UserSharedPreferences.initPrefs(context, PREFERENCIES_ACCESS_TOKEN,
                            Context.MODE_PRIVATE);
                    Call<ResponseBody> call = api.cancelAppointment("Bearer " +
                                    UserSharedPreferences.getString("token", null),
                            (Long) appointmentsList.get(getAdapterPosition()).getId());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                delete(getAdapterPosition());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("server", t.toString());
                        }
                    });
                }
            });
        }

        public void delete(int position) {
            appointmentsList.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String fromDate = appointmentsList.get(position).getFrom();
            String toDate = appointmentsList.get(position).getTo();
            String originalStringFormat = "yyyy-MM-dd' 'HH:mm:ss";
            String dayFormat = "MMM d, ''yy";
            String timeFormat = "HH:mm";

            SimpleDateFormat readingFormat = new SimpleDateFormat(originalStringFormat);
            SimpleDateFormat outputFormatDate = new SimpleDateFormat(dayFormat);
            SimpleDateFormat outputFormatTime = new SimpleDateFormat(timeFormat);
            Date dateFrom = new Date();
            Date dateTo = new Date();
            try {
                dateFrom = readingFormat.parse(fromDate);
                dateTo = readingFormat.parse(toDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Details");
            String message = "<br>You have an appointment with <br>Dr. <b>" +
                    appointmentsList.get(position).getDoctor().getFirstName() + " "
                    + appointmentsList.get(position).getDoctor().getLastName() +
                    "</b>, " + appointmentsList.get(position).getDoctor()
                    .getSpecialization() + "<br>on <b>" + outputFormatDate.format(dateFrom) +
                    "</b> at " + outputFormatTime.format(dateFrom) + "<br><br><b>Note : </b>" +
                    appointmentsList.get(position).getNote() + "<br><br><b>Subject : </b>" + appointmentsList
                    .get(position).getSubject();
            alertDialog.setMessage(Html.fromHtml(message));

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }
}