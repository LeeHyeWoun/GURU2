package lesson.swu.semiproject_leehw;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lesson.swu.semiproject_leehw.Bean.MemoBean;
import lesson.swu.semiproject_leehw.Bean.SaveBean;
import lesson.swu.semiproject_leehw.adapter.MemoPagerAdapter;
import lesson.swu.semiproject_leehw.fragment.MemoTab1Fragment;
import lesson.swu.semiproject_leehw.fragment.MemoTab2Fragment;
import lesson.swu.semiproject_leehw.util.PrefUtil;

public class MemoActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mPager;
    private Button btnSave;
    private MemoPagerAdapter mPagerdapter;
    private String savedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //엑티비티를 전체화면으로 열기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_memo);

        mTabLayout = findViewById(R.id.tabLayout);
        mPager = findViewById(R.id.pager);
        btnSave = findViewById(R.id.btnSave);

        //탭을 동적으로 추가하기
        mTabLayout.addTab(mTabLayout.newTab().setText("글쓰기"));
        mTabLayout.addTab(mTabLayout.newTab().setText("사진"));

        //탭의 가로 전체 사이즈
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        //ViewPager 는 Adapter 를 통해서 Page(=fragment)로 관리한다.
        mPagerdapter = new MemoPagerAdapter(getSupportFragmentManager(),mTabLayout.getTabCount());
        mPager.setAdapter(mPagerdapter);

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
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

    }// end onCreate()

    private void save(){

        MemoTab1Fragment f1 = (MemoTab1Fragment) mPagerdapter.instantiateItem(mPager, 0);
        MemoTab2Fragment f2 = (MemoTab2Fragment) mPagerdapter.instantiateItem(mPager, 1);
        EditText edtMemo = f1.getView().findViewById(R.id.edtMemo);

        if(edtMemo.getText().toString().equals("")){
            Toast.makeText(MemoActivity.this,"메모를 작성해주세요.",Toast.LENGTH_SHORT).show();
        }
        else if(f2.noImage==true){
            Toast.makeText(MemoActivity.this,"사진을 촬영해주세요.",Toast.LENGTH_SHORT).show();
        }
        else{
            savedTime = new SimpleDateFormat("yyyy년 M월 d일 HH시 mm분").format(new Date());

            //메모 내용 저장
            MemoBean memoBean = new MemoBean();
            memoBean.setMemo_txt(edtMemo.getText().toString()); //메모 글 저장
            memoBean.setMemo_img(f2.mCurrentPhotoPath); //사진 저장
            memoBean.setDate(savedTime); //날짜 저장

            Gson gson = new Gson();
            SaveBean saveBean = new SaveBean();
            //기존에 저장된 saveBean 을 찾는다.
            String jsonStr = PrefUtil.getData(this, SaveBean.class.getName());
            if(jsonStr.length() > 0) {
                saveBean = gson.fromJson(jsonStr, SaveBean.class);
            }
            if( saveBean.getMemoList() == null ) {
                saveBean.setMemoList( new ArrayList<MemoBean>() );
            }

            saveBean.getMemoList().add( memoBean );

            //savebean 을 다시 저장
            PrefUtil.setData(getApplicationContext(), SaveBean.class.getName(), gson.toJson(saveBean));

            finish();
        }//end save()
    }
}
