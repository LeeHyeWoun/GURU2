package lesson.swu.semiproject_leehw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import lesson.swu.semiproject_leehw.Bean.MemberBean;
import lesson.swu.semiproject_leehw.Bean.SaveBean;
import lesson.swu.semiproject_leehw.util.PrefUtil;

public class MainActivity extends AppCompatActivity {

    private EditText edtId, edtPw;
    private Button btnLogin, btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //엑티비티를 전체화면으로 열기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        edtId = findViewById(R.id.edtId);
        edtPw = findViewById(R.id.edtPw);
        btnLogin = findViewById(R.id.btnLogin);
        btnJoin = findViewById(R.id.btnJoin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonData = PrefUtil.getData(getApplicationContext(),MemberBean.class.getName());

                if(jsonData == null || jsonData.length()==0){
                    Toast.makeText(MainActivity.this,"회원가입이 되어있지 않습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Gson gson = new Gson();
                    MemberBean mbBean = gson.fromJson(jsonData,MemberBean.class);

                    String ipId = edtId.getText().toString();
                    String ipPw = edtPw.getText().toString();

                    if(ipId.equals(mbBean.getId())&& ipPw.equals(mbBean.getPw())){
                        //로그인 성공
                        Toast.makeText(MainActivity.this,"로그인에 성공했습니다.",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, ListActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        //로그인 실패
                        Toast.makeText(MainActivity.this,"로그인에 실패했습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(i);
            }
        });
    }
}
