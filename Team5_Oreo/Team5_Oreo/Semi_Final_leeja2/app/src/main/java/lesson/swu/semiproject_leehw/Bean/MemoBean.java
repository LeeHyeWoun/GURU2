package lesson.swu.semiproject_leehw.Bean;

import java.io.Serializable;

public class MemoBean implements Serializable {
    private String memo_txt;
    private String memo_img;
    private String date;

    //getter
    public String getMemo_txt() {return memo_txt;}
    public String  getMemo_img() { return memo_img;}
    public String getDate() { return date;}

    //setter
    public void setMemo_txt(String memo_txt) {this.memo_txt = memo_txt;}
    public void setMemo_img(String memo_img) { this.memo_img = memo_img;}
    public void setDate(String date) { this.date = date;}
}
