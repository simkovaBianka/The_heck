package pro1a.ics.upjs.sk.the_heck.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.activity.DoctorProfile;
import pro1a.ics.upjs.sk.the_heck.help.SpecializationIcon;
import pro1a.ics.upjs.sk.the_heck.help.UserSharedPreferences;
import pro1a.ics.upjs.sk.the_heck.model.Doctor;
import pro1a.ics.upjs.sk.the_heck.retrofit.HeckAPI;
import pro1a.ics.upjs.sk.the_heck.retrofit.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pro1a.ics.upjs.sk.the_heck.help.Constant.PREFERENCIES_ACCESS_TOKEN;

public class FavDoctorRecyclerViewAdapter extends
        RecyclerView.Adapter<FavDoctorRecyclerViewAdapter.FavDoctorViewAdapter> {
    Context context;
    private List<Doctor> doctorList = new ArrayList<>();


    public FavDoctorRecyclerViewAdapter(Context context, List<Doctor> list) {
        this.context = context;
        this.doctorList = list;
    }

    public FavDoctorRecyclerViewAdapter() {
    }

    @Override
    public FavDoctorViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_fav_doctor, parent, false);
        FavDoctorViewAdapter holder = new FavDoctorViewAdapter(view, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(FavDoctorViewAdapter holder, int position) {
        holder.nameTextView.setText(doctorList.get(position).getLastName());
        holder.specializationTextView.setText(doctorList.get(position).getSpecializationName());

        int iconNumber = SpecializationIcon.getIconNumber(doctorList
                .get(position).getSpecializationName());
        holder.iconTextView.setImageDrawable(context.getResources().getDrawable(iconNumber));

    }

    @Override
    public long getItemId(int position) {
        return doctorList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }


    class FavDoctorViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView specializationTextView, nameTextView;
        ImageView iconTextView;
        Context context;
        Button removeButton;


        FavDoctorViewAdapter(View itemView, final Context context) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.doctorCardView);
            specializationTextView = (TextView) itemView.findViewById(R.id.detailTextView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            iconTextView = (ImageView) itemView.findViewById(R.id.iconTextView);
            removeButton = (Button) itemView.findViewById(R.id.removeButton);
            this.context = context;
            itemView.setOnClickListener(this);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HeckAPI api = RetrofitFactory.getApi();
                    UserSharedPreferences.initPrefs(context, PREFERENCIES_ACCESS_TOKEN, Context.MODE_PRIVATE);
                    Call<ResponseBody> call = api.deleteFavouriteDoctor("Bearer " +
                                    UserSharedPreferences.getString("token", null),
                            UserSharedPreferences.getLong("id", 0L),
                            (Long) doctorList.get(getAdapterPosition()).getId());
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
            doctorList.remove(position);
            notifyItemRemoved(position);
        }


        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            Intent intent = new Intent(context, DoctorProfile.class);
            intent.putExtra("id", (Long) doctorList.get(position).getId());
            intent.putExtra("specialization", doctorList.get(position).getSpecializationName());
            intent.putExtra("firstName", doctorList.get(position).getFirstName());
            intent.putExtra("lastName", doctorList.get(position).getLastName());
            intent.putExtra("phone", doctorList.get(position).getPhoneNumber());
            intent.putExtra("email", doctorList.get(position).getEmail());
            intent.putExtra("city", doctorList.get(position).getCity());
            intent.putExtra("postalCode", doctorList.get(position).getPostalCode());
            intent.putExtra("address", doctorList.get(position).getAddress());
            context.startActivity(intent);

        }
    }
}
