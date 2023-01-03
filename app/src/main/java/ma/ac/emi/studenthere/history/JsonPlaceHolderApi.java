package ma.ac.emi.studenthere.history;

import java.util.List;

import ma.ac.emi.studenthere.history.History;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    //example url/posts
    @GET("history")
    Call<List<History>> getHistories(@Header("Authorization") String authHeader);

    @GET("lastHistory")
    Call<History> getLastHistory(@Header("Authorization") String authHeader);

    @GET("attend")
    Call<History> attend(@Query("qrCode") String qrCode, @Header("Authorization") String authHeader);
}
