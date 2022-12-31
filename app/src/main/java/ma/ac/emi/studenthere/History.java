package ma.ac.emi.studenthere;


import com.google.gson.annotations.SerializedName;

public class History {

    private int id;

    @SerializedName("courseName")
    private String courseName;

    @SerializedName("date")
    private String date;

    @SerializedName("presence")
    private String presence;

    @SerializedName("teacherName")
    private String teacherName;

    public History(String courseName, String date, String presence, String teacherName) {
        this.courseName = courseName;
        this.date = date;
        this.presence = presence;
        this.teacherName = teacherName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPresence() {
        return presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
