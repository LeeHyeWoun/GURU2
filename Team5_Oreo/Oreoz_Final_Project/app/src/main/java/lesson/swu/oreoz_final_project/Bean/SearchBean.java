package lesson.swu.oreoz_final_project.Bean;

import java.io.Serializable;

public class SearchBean implements Serializable {

    //검색한 텍스트를 저장하기 위한 Bean
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
