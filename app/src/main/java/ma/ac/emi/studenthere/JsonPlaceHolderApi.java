package ma.ac.emi.studenthere;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    //example url/posts
    @GET("03e9768e-0a40-4023-ac2b-3724899b1d62")
    Call<List<History>> getHistories();
}
