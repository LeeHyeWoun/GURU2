package lesson.swu.oreoz_final_project.Bean;

import java.io.Serializable;
import java.util.List;

public class SaveBean implements Serializable {
    private List<SearchBean> searchList;

    //getter
    public List<SearchBean> getSearchList() {
        return searchList;
    }

    //setter
    public void setSearchList(List<SearchBean> searchList) {
        this.searchList = searchList;
    }
}
