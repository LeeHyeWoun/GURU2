package lesson.swu.oreoz_final_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import lesson.swu.oreoz_final_project.Bean.MemberBean;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class MainActivity extends AppCompatActivity {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    //파이어베이스 인증 객체 생성
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private EditText edtId, edtPw;
    private Button btnLogin, btnJoin;
    private CheckBox chbAuto;

    private MemberBean mbBean;
    private List<MemberBean> mMemberList = new ArrayList<MemberBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //액션바 삭제
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        //FireBase 관련 객체 생성
        mAuth = FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();

        //ID 찾기
        edtId = findViewById(R.id.edtId);
        edtPw = findViewById(R.id.edtPw);
        btnLogin = findViewById(R.id.btnLogin);
        btnJoin = findViewById(R.id.btnJoin);
        chbAuto = findViewById(R.id.chbAuto);

        //자동로그인이 체크되어있을 경우 자동 설정
        if(!PrefUtil.getData(MainActivity.this, MemberBean.class.getName()).equals("")
                &&PrefUtil.getDataBoolean(MainActivity.this, "bool" )){
            //데이터를 'Preference'로 저장한다.
            // 'preference'에 저장된 회원데이터를 읽어와서 회원데이터의 ID,PW 값과 현재 입력받은 ID,PW를 비교한다.
            String jsonData = PrefUtil.getData(MainActivity.this, MemberBean.class.getName());
            //json ->LoginBean
            Gson gSon = new Gson();
            MemberBean lbBean = gSon.fromJson(jsonData, MemberBean.class);
            edtId.setText(lbBean.getId());
            edtPw.setText(lbBean.getPw());

            chbAuto.setChecked(true);
        }

        //클릭 이벤트
        //자동로그인
        chbAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chbAuto.isChecked()){
                    Toast.makeText(MainActivity.this, "자동로그인이 체크 되었습니다.", Toast.LENGTH_SHORT).show();
                    PrefUtil.setData(MainActivity.this, "bool", true);
                }
                else{
                    Toast.makeText(MainActivity.this, "자동로그인이 해제 되었습니다.", Toast.LENGTH_SHORT).show();
                    PrefUtil.setData(MainActivity.this, "bool", false);
                }
            }
        });

        //로그인
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //자동 로그인을 위해 현재 로그인하는 정보를 저장
                //로그인 데이터 객체 생성
                MemberBean lb = new MemberBean();
                //로그인 데이터 설정
                lb.setId(edtId.getText().toString());
                lb.setPw(edtPw.getText().toString());
                //데이터를 'Preference'로 저장한다.
                Gson gson = new Gson();
                String jsonStr = gson.toJson(lb);
                PrefUtil.setData(MainActivity.this, MemberBean.class.getName(),jsonStr);

                //유효성 체크
                if(isValidEmail(lb.getId())&&isValidPassword(lb.getPw())){

                    //다이얼로그 보이기
                    PrefUtil.showProgress(MainActivity.this);

                    //로그인
                    loginUser(lb.getId(),lb.getPw());
                }
            }
        });

        //회원가입
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(i);
            }
        });
    }//end OnCreate--------------------------------------------------------------------------------

    //로그인 처리
    private void loginUser(final String email, String pass){
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //로그인 성공
                if(task.isSuccessful()){
                    //회원정보 데이터를 FireBase 로부터 가져온다.
                    mDatabase.getReference().child("user").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //실시간으로 서버가 변경된 내용이 있을 경우 호출된다.
                            //기존의 리스트는 날려버린다.
                            mMemberList.clear();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                MemberBean bean = snapshot.getValue(MemberBean.class);
                                mMemberList.add(bean);
                            }

                            //데이터 베이스에서 로그인된 아이디의 회원정보 불러오기
                            int count = 0;
                            while (count<mMemberList.size()){
                                mbBean = mMemberList.get(count);
                                if(mbBean.getId().equals(email)){

                                    //회원정보 데이터를 PrefUtil 로 저장
                                    Gson gson = new Gson();
                                    String jsonStr = gson.toJson(mbBean);
                                    PrefUtil.setData(MainActivity.this,MemberBean.class.getName(),jsonStr);
                                    PrefUtil.setData(MainActivity.this,"login",true);

                                    //로그인 성공 안내 후 엑티비티 이동
                                    Toast.makeText(MainActivity.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                                    goSearchActivity();
                                    //다이얼로그 숨기기
                                    PrefUtil.hideProgress(MainActivity.this);

                                    break;
                                }else{count++;}
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                //로그인 실패
                else{
                    Toast.makeText(MainActivity.this,"아이디와 비밀번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                    //다이얼로그 숨기기
                    PrefUtil.hideProgress(MainActivity.this);
                }
            }
        });
    }//end loginUser()

    // 이메일 유효성 검사
    private boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(MainActivity.this,"이메일 공백",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            Toast.makeText(MainActivity.this,"이메일 형식 불일치",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }//end isValidEmail

    // 비밀번호 유효성 검사
    private boolean isValidPassword(String password) {
        if (password.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(MainActivity.this,"패스워드 공백",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(MainActivity.this,"패스워드 형식 불일치",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }//end isValidPassword()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }//end onActivityResult

    private void goSearchActivity(){
        finish();
    }//end onActivityResult()


}//end class
