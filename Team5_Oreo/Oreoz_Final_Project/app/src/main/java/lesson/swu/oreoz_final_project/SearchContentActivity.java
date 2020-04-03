package lesson.swu.oreoz_final_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lesson.swu.oreoz_final_project.Adapter.SearchContentAdapter;
import lesson.swu.oreoz_final_project.Bean.BookBean;
import lesson.swu.oreoz_final_project.Bean.SaveBean;
import lesson.swu.oreoz_final_project.Bean.SearchBean;
import lesson.swu.oreoz_final_project.Bean.SearchListBean;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class SearchContentActivity extends AppCompatActivity {

    private EditText edtSearch;
    private ImageView imgBack,imgScrapPage, imgDelete;
    private Button btnSearch;
    private ListView lstContent;
    ListAdapter bookAdapter;

    //파이어 베이스 객체 생성
    private FirebaseDatabase mDatabaseBook;
    private List<BookBean> mBookList = new ArrayList<BookBean>();
    private List<BookBean> mSearchList = new ArrayList<BookBean>();
    private BookBean bBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //키보드 실행 시 화면고정
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //액션바 삭제
        getSupportActionBar().hide();

        setContentView(R.layout.activity_search_content);

        edtSearch = findViewById(R.id.edtSearch);
        imgBack = findViewById(R.id.imgBack);
        imgScrapPage =findViewById(R.id.imgScrapPage);
        imgDelete = findViewById(R.id.imgDelete);
        btnSearch = findViewById(R.id.btnSearch);
        lstContent = findViewById(R.id.lstContent);

        //파이어 베이스 객체 생성
        mDatabaseBook = FirebaseDatabase.getInstance();

        //검색어 받기
        String searchWord = PrefUtil.getData(SearchContentActivity.this,"edtSearch");
        edtSearch.setText(searchWord);
        edtSearch.setSelection(edtSearch.getText().length());

        //이전 페이지(책 목록 페이지)로
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });//end imgBack clickEvent

        //스크랩창으로 이동
        imgScrapPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchContentActivity.this, ScrapActivity.class);
                startActivity(i);
            }
        }); //end imgScrapPage clickEvent

        // 검색 버튼 클릭
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearch.getText().toString().equals("")) {
                    Toast.makeText(SearchContentActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    search(edtSearch.getText().toString());
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
                        Toast.makeText(SearchContentActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        search(edtSearch.getText().toString());
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
                if (edtSearch.isFocused() && s.length()>0 && !s.toString().equals("")) {
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

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText(null);
            }
        });

    } //end onCreate


    @Override
    protected void onResume() {
        super.onResume();

        search(edtSearch.getText().toString());
    }

    private void getList(String searchWord){
        Gson gson = new Gson();
        String jsonStr = PrefUtil.getData(getApplicationContext(),SearchListBean.class.getName());
        SearchListBean searchListBean = new SearchListBean();

        if(jsonStr.length() > 0) {
            searchListBean = gson.fromJson(jsonStr, SearchListBean.class);
        }

        if(searchListBean.getBookBeanList() == null) {
            searchListBean.setBookBeanList( new ArrayList<BookBean>() );
        }

        //Adapter 생성
        bookAdapter = new SearchContentAdapter(getApplicationContext(), searchListBean.getBookBeanList(), searchWord);

        lstContent.setAdapter(bookAdapter);
    }

    private void search(final String searchWord){

        //파이어베이스로부터 데이터 가져오기
        mDatabaseBook.getReference().child("book").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mBookList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    BookBean bean = snapshot.getValue(BookBean.class);
                    mBookList.add(bean);
                }

                //searchWord 키워드가 있는 제목 또는 저자의 책은 검색목록에 추가하기
                int count = 0;
                mSearchList.clear();
                while (count<mBookList.size()){
                    bBean = mBookList.get(count);
                    if(bBean.getTitle().contains(searchWord)
                            ||bBean.getAuthor().contains(searchWord)){
                        mSearchList.add(bBean);
                    }
                    count++;
                }


                // 검색어 저장 Bean 생성
                SearchBean sb = new SearchBean();
                sb.setText(edtSearch.getText().toString());

                //데이터를 저장
                Gson gson = new Gson();
                //클래스를 json화 시키기
                String jsonStr = gson.toJson(sb);
                SaveBean saveBean = new SaveBean();
                SearchListBean slBean = new SearchListBean();
                slBean.setBookBeanList(new ArrayList<BookBean>());

                //기존에 저장된 saveBean 을 찾는다.
                String jsonTit = PrefUtil.getData(SearchContentActivity.this, SaveBean.class.getName());
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
                slBean.getBookBeanList().clear();
                slBean.getBookBeanList().addAll(mSearchList);

                //json 데이터를 저장
                PrefUtil.setData(getApplicationContext(), SaveBean.class.getName(), gson.toJson(saveBean));
                PrefUtil.setData(getApplicationContext(),SearchListBean.class.getName(),gson.toJson(slBean));

                getList(searchWord);

//                //프로그래스 숨기기
//                PrefUtil.hideProgress(SearchContentActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
