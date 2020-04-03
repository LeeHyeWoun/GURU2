//5조 이혜원 디지털미디어학과
package lesson.swu.oreoz_final_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class CommentActivity extends AppCompatActivity {

    //이전 페이지로 가기, 스크랩함으로 가기
    private ImageView imgBack, imgScrapPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //엑티비티를 전체화면으로 열기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_comment);

        //ID 찾기
        imgBack = findViewById(R.id.imgBack);
        imgScrapPage = findViewById(R.id.imgScrapPage);

        //이벤트------------------------------------------------------------------------------------
        //이전 페이지(책 목록 페이지)로
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(CommentActivity.this,ListActivity.class);
//                startActivity(i);
//                finish();
            }
        });//end imgBack clickEvent

        //스크랩함가기
        imgScrapPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(CommentActivity.this,Scrap.class);
//                startActivity(i);
            }
        });//end imgScrapPage clickEvent

    }//end OnCreate
}//end class
