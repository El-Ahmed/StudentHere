package ma.ac.emi.studenthere.history;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class History {

    private int id;

    @SerializedName("courseName")
    private String courseName;

    @SerializedName("date")
    private Date date;

    @SerializedName("present")
    private Boolean presence;

    @SerializedName("teacherName")
    private String teacherName;

    public History(String courseName, Date date, Boolean presence, String teacherName) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getPresence() {
        return presence;
    }

    public void setPresence(Boolean presence) {
        this.presence = presence;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
