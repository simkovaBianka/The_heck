package pro1a.ics.upjs.sk.the_heck.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pro1a.ics.upjs.sk.the_heck.R;
import pro1a.ics.upjs.sk.the_heck.activity.DoctorProfile;
import pro1a.ics.upjs.sk.the_heck.model.Doctor;

public class FilterDoctorRecyclerViewAdapter extends RecyclerView
        .Adapter<FilterDoctorRecyclerViewAdapter.ChooseDoctorViewHolder> {
    Context context;
    private List<Doctor> doctorList = new ArrayList<>();
    ;

    public FilterDoctorRecyclerViewAdapter(Context context, List<Doctor> list) {
        this.context = context;
        this.doctorList = list;
    }

    @Override
    public ChooseDoctorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_filter_doctor,
                parent, false);
        ChooseDoctorViewHolder holder = new ChooseDoctorViewHolder(v, context);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChooseDoctorViewHolder holder, int position) {
        holder.doctorTextView.setText(doctorList.get(position).getFirstName() + " " +
                doctorList.get(position).getLastName());
        holder.cityTextView.setText(doctorList.get(position).getCity());
    }

    @Override
    public long getItemId(int position) {
        return doctorList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    class ChooseDoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        TextView doctorTextView, cityTextView;
        Context context;

        ChooseDoctorViewHolder(View itemView, Context context) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView3);
            doctorTextView = (TextView) itemView.findViewById(R.id.doctorTextView);
            cityTextView = (TextView) itemView.findViewById(R.id.cityTextView);
            this.context = context;
            itemView.setOnClickListener(this);
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
