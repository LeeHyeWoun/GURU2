package lesson.swu.oreoz_final_project;


import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import lesson.swu.oreoz_final_project.Bean.BookBean;
import lesson.swu.oreoz_final_project.util.PrefUtil;


public class Bookposition2Activiry extends AppCompatActivity {

    private TextView txtTitleBar, txtSign;
    private ImageView imgBack, imgHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_bookposition2);

        String jsonData = PrefUtil.getData(Bookposition2Activiry.this,BookBean.class.getName());
        Gson gson = new Gson();
        BookBean bean = gson.fromJson(jsonData,BookBean.class);

        //ID 찾기
        txtTitleBar = findViewById(R.id.txtTitleBar);
        txtSign = findViewById(R.id.txtSign);
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);


        //데이터 설정
        txtTitleBar.setText(bean.getTitle());
        txtSign.setText(bean.getSign().get(0));
        if(bean.getSign().size()>1){
            txtSign.append(" ("+bean.getSign().size()+"권)");
        }

        //이벤트------------------------------------------------------------------------------------
        //이전 페이지(책 목록 페이지)로
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });//end imgBack clickEvent


        //책 찾으러 가기
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Bookposition2Activiry.this,SearchActivity .class);
                startActivity(i);
            }
        });//end btnMap clickEvent

        TextView txtTitlebar = (TextView) findViewById(R.id.txtTitleBar); txtTitleBar.setSelected(true);

        TextView Text1 = findViewById(R.id.blinking_animation);
        startBlinkingAnimation(Text1);
    }

    public void startBlinkingAnimation(View view) {
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_animation);
        view.startAnimation(startAnimation);
    }
}
