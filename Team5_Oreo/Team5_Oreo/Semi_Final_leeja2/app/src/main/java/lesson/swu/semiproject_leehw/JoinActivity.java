package lesson.swu.semiproject_leehw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;

import lesson.swu.semiproject_leehw.Bean.MemberBean;
import lesson.swu.semiproject_leehw.Bean.SaveBean;
import lesson.swu.semiproject_leehw.util.PrefUtil;

public class JoinActivity extends AppCompatActivity {

    private EditText edtId, edtPw, edtPw2, edtName, edtYyyy, edtMm, edtDd;
    private RadioButton rbWoman, rbMan;
    private Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //엑티비티를 전체화면으로 열기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_join);

        edtId = findViewById(R.id.edtId);
        edtPw = findViewById(R.id.edtPw);
        edtPw2 = findViewById(R.id.edtPw2);
        edtName = findViewById(R.id.edtName);
        edtYyyy = findViewById(R.id.edtYyyy);
        edtMm = findViewById(R.id.edtMm);
        edtDd = findViewById(R.id.edtDd);
        rbWoman = findViewById(R.id.rbWoman);
        rbMan = findViewById(R.id.rbMan);
        btnJoin = findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtId.getText().toString().equals("")){
                    Toast.makeText(JoinActivity.this,"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();}
                else if(edtPw.getText().toString().equals("")){
                    Toast.makeText(JoinActivity.this,"패스워드를 입력해주세요.",Toast.LENGTH_SHORT).show();}
                else if(edtName.getText().toString().equals("")){
                    Toast.makeText(JoinActivity.this,"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();}
                else if(edtYyyy.getText().toString().equals("")||edtMm.getText().toString().equals("")||edtDd.getText().toString().equals("")){
                    Toast.makeText(JoinActivity.this,"생년월일을 입력해주세요.",Toast.LENGTH_SHORT).show();}
                else if(!rbWoman.isChecked()&&!rbMan.isChecked()){
                    Toast.makeText(JoinActivity.this,"성별을 선택해주세요.",Toast.LENGTH_SHORT).show();}
                else if(!edtPw.getText().toString().equals(edtPw2.getText().toString())){
                    Toast.makeText(JoinActivity.this, "패스워드를 다시 확인해 주세요.",Toast.LENGTH_SHORT).show();}
                else if(Integer.parseInt(edtYyyy.getText().toString())<1900 || Integer.parseInt(edtYyyy.getText().toString())>2100 ){
                    Toast.makeText(JoinActivity.this, "생년을 다시 확인해 주세요.",Toast.LENGTH_SHORT).show();}
                else if(Integer.parseInt(edtMm.getText().toString())<1 || Integer.parseInt(edtMm.getText().toString())>12 ){
                    Toast.makeText(JoinActivity.this, "생월을 다시 확인해 주세요.",Toast.LENGTH_SHORT).show();}
                else if(Integer.parseInt(edtDd.getText().toString())<1 || Integer.parseInt(edtDd.getText().toString())>31 ){
                    Toast.makeText(JoinActivity.this, "생일을 다시 확인해 주세요.",Toast.LENGTH_SHORT).show();}
                else{
                    Intent i = new Intent(JoinActivity.this, MainActivity.class);

                    MemberBean mb = new MemberBean();
                    mb.setId(edtId.getText().toString());
                    mb.setPw(edtPw.getText().toString());
                    mb.setName(edtName.getText().toString());
                    mb.setYyyy(edtYyyy.getText().toString());
                    mb.setMm(edtMm.getText().toString());
                    mb.setDd(edtDd.getText().toString());

                    if(rbWoman.isChecked()){ mb.setSex("woman");}
                    else if(rbMan.isChecked()) { mb.setSex("man");}

                    i.putExtra(MemberBean.class.getName(), mb);

                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(mb); //class -> json

                    PrefUtil.setData(getApplicationContext(), MemberBean.class.getName(), jsonStr);
                    PrefUtil.setData(JoinActivity.this,SaveBean.class.getName(),null);

                    Toast.makeText(JoinActivity.this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show();
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });
    }
}
