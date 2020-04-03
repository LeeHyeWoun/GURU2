package lesson.swu.semiproject_leehw.Bean;

import java.io.Serializable;

public class MemberBean implements Serializable {

    private String id;
    private String pw;
    private String name;
    private String yyyy;
    private String mm;
    private String dd;
    private String sex;

    //getter
    public String getId() { return id;}
    public String getPw() { return pw;}
    public String getName() { return name;}
    public String getYyyy() { return yyyy;}
    public String getMm() { return mm;}
    public String getDd() { return dd;}
    public String getSex() { return sex;}

    //setter
    public void setId(String id) { this.id = id;}
    public void setPw(String pw) { this.pw = pw;}
    public void setName(String name) { this.name = name;}
    public void setYyyy(String yyyy) { this.yyyy = yyyy;}
    public void setMm(String mm) { this.mm = mm;}
    public void setDd(String dd) { this.dd = dd;}
    public void setSex(String sex) { this.sex = sex;}
}
