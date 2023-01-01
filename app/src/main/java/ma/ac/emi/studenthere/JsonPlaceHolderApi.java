package ma.ac.emi.studenthere;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface JsonPlaceHolderApi {

    //example url/posts
    @GET("history")
    Call<List<History>> getHistories(@Header("Authorization") String authHeader);

    @GET("lastHistory")
    Call<History> getLastHistory(@Header("Authorization") String authHeader);

}
