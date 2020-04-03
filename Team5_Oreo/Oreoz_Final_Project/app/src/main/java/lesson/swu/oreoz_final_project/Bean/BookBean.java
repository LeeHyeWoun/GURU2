package lesson.swu.oreoz_final_project.Bean;

import java.io.Serializable;
import java.util.List;

public class BookBean implements Serializable {

    private String author;
    private String imgUrl;
    private String publisher;
    private Boolean rental;
    private List<String> sign;
    private String summary;
    private String title;

    //getter
    public String getAuthor() {
        return author;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public Boolean getRental() {
        return rental;
    }

    public List<String> getSign() {
        return sign;
    }

    public String getSummary() {
        return summary;
    }

    public String getTitle() {
        return title;
    }

    //setter
    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setRental(Boolean rental) {
        this.rental = rental;
    }

    public void setSign(List<String> sign) {
        this.sign = sign;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
