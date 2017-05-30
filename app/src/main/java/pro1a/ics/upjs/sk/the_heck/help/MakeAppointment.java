package pro1a.ics.upjs.sk.the_heck.help;

import android.util.Log;

import okhttp3.ResponseBody;
import pro1a.ics.upjs.sk.the_heck.model.AppointmentUser;
import pro1a.ics.upjs.sk.the_heck.model.Appointments;
import pro1a.ics.upjs.sk.the_heck.model.AppointmentDoctor;
import pro1a.ics.upjs.sk.the_heck.retrofit.HeckAPI;
import pro1a.ics.upjs.sk.the_heck.retrofit.RetrofitFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class MakeAppointment {
    static boolean result;

    public static void makeAppointment(String from, String to, AppointmentUser user,
                                       AppointmentDoctor doctor, String patient, String note,
                                       String subject, String token) {

        Appointments newAppointment = new Appointments((Long) null, from,
                to, note, subject, true, user, doctor, patient, false, false);
        HeckAPI api = RetrofitFactory.getApi();
        Call<ResponseBody> call = api.makeAppointment("Bearer " +
                token, newAppointment);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    result = true;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("server", t.toString());
            }
        });
    }

    public static boolean result() {
        return result;
    }

}
