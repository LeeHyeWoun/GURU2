package lesson.swu.oreoz_final_project.Bean;

import java.io.Serializable;

public class CommentBean implements Serializable {
    private String title;
    private String userId;
    private String userName;
    private String comment;
    private String day;

    //getter
    public String getTitle() { return title; }
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getComment() { return comment; }
    public String getDay() { return day; }

    //setter
    public void setTitle(String title) { this.title = title; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setComment(String comment) { this.comment = comment; }
    public void setDay(String day) { this.day = day; }
}
