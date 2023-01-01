package ma.ac.emi.studenthere.login;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserService {
    //endpoint
    @GET("5fd3a84c-5bb9-4a48-a1c3-4fb0e09f5680")
    Call<LoginResponse> login(@Query("username") String username, @Query("password") String password);
}
