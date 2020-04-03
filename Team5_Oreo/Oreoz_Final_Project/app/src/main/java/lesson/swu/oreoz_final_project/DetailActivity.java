//5조 이혜원 디지털미디어학과
package lesson.swu.oreoz_final_project;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lesson.swu.oreoz_final_project.Adapter.DetailPagerAdapter;
import lesson.swu.oreoz_final_project.Bean.BookBean;
import lesson.swu.oreoz_final_project.Bean.SearchListBean;
import lesson.swu.oreoz_final_project.Fragment.Detail1Fragment;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class DetailActivity extends AppCompatActivity {

    //이전 페이지로 가기, 스크랩함으로 가기, 책 이미지
    private ImageView imgBack, imgScrapPage, imgTitle, imgScrap;
    //탭레이아웃과 뷰페이저
    private TabLayout mTabLayout;
    private ViewPager mPager;
    //뷰페이저를 관리할 어답터
    private DetailPagerAdapter mPagerdapter;
    //선택된 콘텐츠의 정보를 받을 bean 객체
    private BookBean bookBean;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //키보드 실행 시 화면고정
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //엑티비티를 전체화면으로 열기
        getSupportActionBar().hide();

        setContentView(R.layout.activity_detail);

        //넘겨준 데이터를 받는다.
        bookBean = (BookBean) getIntent().getSerializableExtra(BookBean.class.getName());

        //ID 찾기
        imgBack = findViewById(R.id.imgBack);
        imgScrapPage = findViewById(R.id.imgScrapPage);
        imgTitle = findViewById(R.id.imgTitle);
        imgScrap = findViewById(R.id.imgScrap);
        mTabLayout = findViewById(R.id.tabLayout);
        mPager = findViewById(R.id.pager);

        //책 이미지 설정
        try{
            new DownImgTask(imgTitle).execute(new URL(bookBean.getImgUrl()));
        }catch (Exception e){e.printStackTrace();}


        //탭을 동적으로 추가하기
        mTabLayout.addTab(mTabLayout.newTab().setText("상세정보"));
        mTabLayout.addTab(mTabLayout.newTab().setText("댓글달기"));

        //탭의 가로 전체 사이즈
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        //ViewPager 는 Adapter 를 통해서 Page(=fragment)로 관리
        mPagerdapter = new DetailPagerAdapter(getSupportFragmentManager(),mTabLayout.getTabCount());
        mPager.setAdapter(mPagerdapter);

        //TabLayout 과 ViewPager 를 서로 연결
        //ViewPager 가 움직였을 때, 탭이 변경
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //TabLayout 이 움직였을 때 ViewPager 가 움직이도록 연결시킨다.
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //현재 사용자가 클릭한 탭의 이벤틀르 실행된다.
                mPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        //이벤트------------------------------------------------------------------------------------
        //이전 페이지(책 목록 페이지)로
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });//end imgBack clickEvent

        //스크랩함가기
        imgScrapPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this,ScrapActivity.class);
                startActivity(i);
            }
        });//end imgScrapPage clickEvent

        imgScrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //스크랩 리스트 불러오기
                String jsonScrapList = PrefUtil.getData(getApplicationContext(),"scrapList");
                SearchListBean scrapList = new SearchListBean();
                if(jsonScrapList.length() > 0) {
                    scrapList = gson.fromJson(jsonScrapList,SearchListBean.class);
                }
                if(scrapList.getBookBeanList() == null) {
                    scrapList.setBookBeanList( new ArrayList<BookBean>() );
                }

                Boolean getScrap = PrefUtil.getDataBoolean(getApplicationContext(),"scrap");

                //즐겨찾기 설정
                if (!getScrap){
                    imgScrap.setImageResource(R.drawable.scrap_on);
                    PrefUtil.setData(getApplicationContext(),"scrap",true);
                    //리스트에 추가 후 저장
                    scrapList.getBookBeanList().add(0,bookBean);
                    PrefUtil.setData(getApplicationContext(),"scrapList",gson.toJson(scrapList));
                }
                //즐겨찾기 해제
                else{
                    imgScrap.setImageResource(R.drawable.scrap_off);
                    PrefUtil.setData(getApplicationContext(),"scrap",false);
                    //리스트에서 제거하기
                    if(scrapList.getBookBeanList().size()>0){
                        int i = 0;
                        while (i<scrapList.getBookBeanList().size()){
                            //만약 스크랩 리스트에 해당 책이 있다면
                            if(scrapList.getBookBeanList().get(i).getTitle().equals(bookBean.getTitle())){
                                //제거 후 저장
                                scrapList.getBookBeanList().remove(i);
                                PrefUtil.setData(getApplicationContext(),"scrapList",gson.toJson(scrapList));
                                break;
                            }else{ i++; }
                        }
                    }
                }
            }
        });//end imgScrap ClickEvent

    }//end OnCreate ===============================================================================

    @Override
    protected void onResume() {
        super.onResume();

        //스크랩 리스트를 불러옵니다.
        gson = new Gson();
        String jsonScrapList = PrefUtil.getData(getApplicationContext(),"scrapList");
        SearchListBean scrapList = new SearchListBean();
        if(jsonScrapList.length() > 0) {
            scrapList = gson.fromJson(jsonScrapList,SearchListBean.class);
        }
        if(scrapList.getBookBeanList() == null) {
            scrapList.setBookBeanList( new ArrayList<BookBean>() );
        }

        //스크랩 리스트 중에 현재의 책이 존재한다면 즐겨찾기 표시
        int i = 0;
        while (i<scrapList.getBookBeanList().size()){
            if(bookBean.getTitle().equals(scrapList.getBookBeanList().get(i).getTitle())){
                imgScrap.setImageResource(R.drawable.scrap_on);
                PrefUtil.setData(getApplicationContext(),"scrap",true);
                break;
            }else{ i++; }
        }

    }
}//end class
