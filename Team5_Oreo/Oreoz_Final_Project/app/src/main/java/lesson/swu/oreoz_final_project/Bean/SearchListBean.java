package lesson.swu.oreoz_final_project.Bean;

import java.io.Serializable;
import java.util.List;

public class SearchListBean implements Serializable {
    private List<BookBean> bookBeanList;

    public List<BookBean> getBookBeanList() {
        return bookBeanList;
    }

    public void setBookBeanList(List<BookBean> bookBeanList) {
        this.bookBeanList = bookBeanList;
    }
}
