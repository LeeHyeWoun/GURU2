package lesson.swu.oreoz_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class BookpositionActivity extends AppCompatActivity {

    private ImageView imgBack;
    private Button btnHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //엑티비티를 전체화면으로 열기
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_bookposition);

        //ID 찾기
        imgBack = findViewById(R.id.imgBack);

        btnHere = findViewById(R.id.btnHere);

        //이전 페이지(책 목록 페이지)로
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });//end imgBack clickEvent


        //책 찾으러 가기
        btnHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookpositionActivity.this, Bookposition2Activiry.class);
                startActivity(i);
            }
        });//end btnMap clickEvent

    }//end OnCreate ===============================================================================

}
