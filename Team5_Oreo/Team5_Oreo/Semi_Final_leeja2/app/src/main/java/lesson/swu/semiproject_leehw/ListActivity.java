package lesson.swu.semiproject_leehw;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import lesson.swu.semiproject_leehw.adapter.ListPagerAdapter;
import lesson.swu.semiproject_leehw.fragment.ListTab2Fragment;

public class ListActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mPager;
    private ListPagerAdapter lPageradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //엑티비티를 전체화면으로 열기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_list);

        mTabLayout = findViewById(R.id.tabLayout);
        mPager = findViewById(R.id.pager);

        //탭을 동적으로 추가하기
        mTabLayout.addTab(mTabLayout.newTab().setText("메모"));
        mTabLayout.addTab(mTabLayout.newTab().setText("회원정보"));

        //탭의 가로 전체 사이즈
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        //ViewPager 는 Adapter 를 통해서 Page(=fragment)로 관리한다.
        ListPagerAdapter adapter = new ListPagerAdapter(getSupportFragmentManager(),mTabLayout.getTabCount());
        mPager.setAdapter(adapter);

        //TabLayout 과 ViewPager 를 서로 연결 시킨다.
        //ViewPager 가 움직였을 때, 탭이 바뀌게끔한다.
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        //TabLayout 이 움직였을 때 ViewPager 가 움직이도록 연결시킨다.
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //현재 사용자가 클릭한 탭의 이벤틀르 실행된다.
                mPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }//OnCreate()
}
