package lesson.swu.oreoz_final_project.Bean;

import java.io.Serializable;
import java.util.List;

public class BookScrapListBean implements Serializable {
    private List<BookBean> bookScrapList;

    public List<BookBean> getBookScrapList() {
        return bookScrapList;
    }

    public void setBookScrapList(List<BookBean> bookScrapList) {
        this.bookScrapList = bookScrapList;
    }
}