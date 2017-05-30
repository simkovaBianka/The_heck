package pro1a.ics.upjs.sk.the_heck.retrofit;

import java.util.List;

import okhttp3.ResponseBody;
import pro1a.ics.upjs.sk.the_heck.model.Password;
import pro1a.ics.upjs.sk.the_heck.model.Token;
import pro1a.ics.upjs.sk.the_heck.model.Appointments;
import pro1a.ics.upjs.sk.the_heck.model.Doctor;
import pro1a.ics.upjs.sk.the_heck.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HeckAPI {

    @POST("login/user")
    Call<Token> loginTo(@Body User user);

    @POST("users")
    Call<Token> registerTo(@Body Object user);

    @GET("users/future/{id}")
    Call<List<Appointments>> getAppointmentsForUser(@Header("Authorization") String authHeader,
                                                    @Path("id") Long id);

    @GET("doctors/search")
    Call<List<Doctor>> getDoctors(@Header("Authorization") String authHeader,
                                  @Query("specialization") String specialization,
                                  @Query("firstName") String firstName,
                                  @Query("lastName") String lastName,
                                  @Query("city") String city,
                                  @Query("from") String from,
                                  @Query("to") String to);

    @GET("users/appointments")
    Call<List<Appointments>> getAppointmentsForDoctor(@Header("Authorization") String authHeader,
                                                      @Query("idDoc") Long idDoc,
                                                      @Query("idUser") Long idUser,
                                                      @Query("dateFrom") String from,
                                                      @Query("dateTo") String to);

    @POST("appointments")
    Call<ResponseBody> makeAppointment(@Header("Authorization") String authHeader,
                                       @Body Appointments appointments);

    @GET("users/favourite/{id}")
    Call<List<Doctor>> getFavouriteDoctors(@Header("Authorization") String authHeader,
                                           @Path("id") Long idUser);

    @GET("users/deleteFavourite")
    Call<ResponseBody> deleteFavouriteDoctor(@Header("Authorization") String authHeader,
                                             @Query("idUser") Long idUser,
                                             @Query("idDoc") Long idDoctor);

    @GET("users/addFavourite")
    Call<ResponseBody> addFavouriteDoctor(@Header("Authorization") String authHeader,
                                          @Query("idUser") Long idUser,
                                          @Query("idDoc") Long idDoctor);

    @GET("appointments/delete/{id}")
    Call<ResponseBody> cancelAppointment(@Header("Authorization") String authHeader,
                                         @Path("id") Long idAppointment);

    @GET("users/{id}")
    Call<User> getUserDetails(@Header("Authorization") String authHeader,
                              @Path("id") Long idUser);

    @GET("users/checkFavourite")
    Call<Boolean> isFavouriteDoctor(@Header("Authorization") String authHeader,
                                    @Query("idUser") Long idUser,
                                    @Query("idDoc") Long idDoctor);

    @POST("users/{id}/changePassword")
    Call<ResponseBody> changePassword(@Path("id") Long idUser, @Body Password password);


    @POST("users/{id}/checkPassword")
    Call<Boolean> checkPassword(@Path("id") Long idUser, @Query("currentModalPassword")
            String password);

    @POST("user/checkLogin/{login}")
    Call<Boolean> checkLogin(@Path("login") String login);
}
