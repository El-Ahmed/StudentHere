package ma.ac.emi.studenthere.login;

public class LoginResponse {
    private String jwttoken;

    public LoginResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }
    public String getToken() {
        return jwttoken;
    }

    public void setToken(String token) {
        this.jwttoken = token;
    }
}
