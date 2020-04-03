package lesson.swu.oreoz_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import lesson.swu.oreoz_final_project.Adapter.ScrapAdapter;
import lesson.swu.oreoz_final_project.Adapter.SearchContentAdapter;
import lesson.swu.oreoz_final_project.Bean.BookBean;
import lesson.swu.oreoz_final_project.Bean.BookScrapListBean;
import lesson.swu.oreoz_final_project.Bean.SearchListBean;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class ScrapActivity extends AppCompatActivity {

    private TextView txtNoBook;
    private ImageView imgBack,imgHome;
    private ListView lstScrap;
    private ListAdapter scrapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //액션바 삭제
        getSupportActionBar().hide();

        setContentView(R.layout.activity_scrap);

        lstScrap = findViewById(R.id.lstScrap);

        //View를 찾는다.
        txtNoBook = findViewById(R.id.txtNoBook);
        imgBack =findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);


        //이전 페이지(책 목록 페이지)로
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });//end imgBack clickEvent

        // 홈 페이지로
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScrapActivity.this, SearchActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }); //end imgHome clickEvent

    }

    protected void onResume() {
        super.onResume();

        getList();
    }

    private void getList(){
        Gson gson = new Gson();

        String jsonScrapList = PrefUtil.getData(getApplicationContext(),"scrapList");
        SearchListBean scrapList = new SearchListBean();
        if(jsonScrapList.length() > 0) {
            scrapList = gson.fromJson(jsonScrapList,SearchListBean.class);
        }

        if(scrapList.getBookBeanList() == null) {
            scrapList.setBookBeanList( new ArrayList<BookBean>() );
        }

        //리스트가 비어있다면 "스크랩한 책이 없습니다." 띄우기
        if(scrapList.getBookBeanList().size()>0){
            txtNoBook.setVisibility(View.INVISIBLE);}
        else{ txtNoBook.setVisibility(View.VISIBLE);}

        //Adapter 생성
        scrapAdapter = new ScrapAdapter(getApplicationContext(), scrapList.getBookBeanList());

        lstScrap.setAdapter(scrapAdapter);
    }
}

