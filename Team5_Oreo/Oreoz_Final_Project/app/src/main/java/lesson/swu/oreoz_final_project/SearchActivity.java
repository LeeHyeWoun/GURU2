package lesson.swu.oreoz_final_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lesson.swu.oreoz_final_project.Adapter.SearchAdapter;
import lesson.swu.oreoz_final_project.Bean.BookBean;
import lesson.swu.oreoz_final_project.Bean.CommentBean;
import lesson.swu.oreoz_final_project.Bean.MemberBean;
import lesson.swu.oreoz_final_project.Bean.SaveBean;
import lesson.swu.oreoz_final_project.Bean.SearchBean;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class SearchActivity extends AppCompatActivity {

    private ImageView imgUser,imgScrapPage, imgDelete;
    private TextView edtSearch, txtRecord;
    private Button btnSearch;
    private ListView lstSearch;
    ListAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //키보드 실행 시 화면고정
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //액션바 삭제
        getSupportActionBar().hide();

        setContentView(R.layout.activity_search);

        //ID 찾기
        imgUser = findViewById(R.id.imgUser);
        imgScrapPage =findViewById(R.id.imgScrapPage);
        imgDelete=findViewById(R.id.imgDelete);
        edtSearch = findViewById(R.id.edtSearch);
        txtRecord = findViewById(R.id.txtRecord);
        btnSearch = findViewById(R.id.btnSearch);
        lstSearch = findViewById(R.id.lstSearch);

        //이벤트
        //검색 내용 삭제
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText(null);
            }
        });
        //회원정보 수정창 이동
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonData = PrefUtil.getData(SearchActivity.this,MemberBean.class.getName());
                Boolean login = PrefUtil.getDataBoolean(SearchActivity.this,"login");
                Gson gson = new Gson();
                MemberBean memberBean = gson.fromJson(jsonData,MemberBean.class);
                //로그인이 안 되어있을 경우
                if(memberBean == null||login==null||login==false){
                    Intent i = new Intent(SearchActivity.this, MainActivity.class);
                    startActivity(i);
                }
                //로그인 되어있을 경우
                else{
                    Intent i = new Intent(SearchActivity.this, MemberActivity.class);
                    startActivity(i);
                }
            }
        });

        //스크랩창으로 이동
        imgScrapPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchActivity.this, ScrapActivity.class);
                startActivity(i);
            }
        });

        // 검색 버튼 클릭
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearch.getText().toString().equals("")) {
                    Toast.makeText(SearchActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    search();
                }
            }
        }); //end btnSearch click

        //키보드 상의 검색버튼 클릭
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId==EditorInfo.IME_ACTION_SEARCH){
                    if (edtSearch.getText().toString().equals("")) {
                        Toast.makeText(SearchActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        search();
                    }
                    handled=true;
                }
                return handled;
            }
        });

        //검색 입력값이 없을 경우 delete 아이콘 숨기기
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //입력값에 변화가 있을 때
                if (edtSearch.isFocused() && s.length()>0) {
                    imgDelete.setVisibility(View.VISIBLE);
                }
                else{
                    imgDelete.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    } //end onCreate()

    @Override
    protected void onResume() {
        super.onResume();

        getList();
    }

    // list 업데이트
    private void getList() {
        Gson gson = new Gson();
        String jsonStr = PrefUtil.getData(getApplicationContext(),SaveBean.class.getName());
        SaveBean saveBean = new SaveBean();

        if(jsonStr.length() > 0) {
            saveBean = gson.fromJson(jsonStr, SaveBean.class);
        }

        if(saveBean.getSearchList() == null) {
            saveBean.setSearchList( new ArrayList<SearchBean>() );
        }

        if(saveBean.getSearchList().size()>0) {
            txtRecord.setText("검색 기록");
        }else{
            txtRecord.setText("검색 기록이 없습니다.");
        }

        //Adapter 생성
        searchAdapter = new SearchAdapter(getApplicationContext(), saveBean.getSearchList(), txtRecord, edtSearch);

       lstSearch.setAdapter(searchAdapter);
    }

    //검색...리스트 형성 및 넘어가기
    private void search(){
        //다이얼로그 보이기
        PrefUtil.showProgress(SearchActivity.this);

        // 검색어 저장 Bean 생성
        SearchBean sb = new SearchBean();
        sb.setText(edtSearch.getText().toString());

        //데이터를 저장
        Gson gson = new Gson();
        //클래스를 json화 시키기
        SaveBean saveBean = new SaveBean();

        //기존에 저장된 savebean을 찾는다.
        String jsonTit = PrefUtil.getData(SearchActivity.this, SaveBean.class.getName());
        if (jsonTit.length() > 0) {
            saveBean = gson.fromJson(jsonTit, SaveBean.class);
        }
        if (saveBean.getSearchList() == null) {
            saveBean.setSearchList(new ArrayList<SearchBean>());
        }

        // 중복된 단어 처리
        for(int j =0 ; j < saveBean.getSearchList().size(); j++){
            SearchBean sBean = saveBean.getSearchList().get(j);
            if(edtSearch.getText().toString().equals(sBean.getText())){
                saveBean.getSearchList().remove(j);
            }
        }

        saveBean.getSearchList().add(0, sb);

        //json 데이터를 저장
        PrefUtil.setData(getApplicationContext(), SaveBean.class.getName(), gson.toJson(saveBean));
        PrefUtil.setData(getApplicationContext(),"edtSearch",edtSearch.getText().toString());

        Intent i = new Intent(SearchActivity.this, SearchContentActivity.class);
        startActivity(i);

        //프로그래스 숨기기
        PrefUtil.hideProgress(SearchActivity.this);

    }//end search



}
