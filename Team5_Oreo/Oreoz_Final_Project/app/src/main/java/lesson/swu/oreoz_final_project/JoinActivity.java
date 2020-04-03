package lesson.swu.oreoz_final_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;
import java.util.regex.Pattern;

import lesson.swu.oreoz_final_project.Bean.MemberBean;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class JoinActivity extends AppCompatActivity {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    //파이어베이스 인증 객체 생성
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private EditText edtId, edtPw, edtPw2, edtName;
    private Button btnJoin;

    private MemberBean mbBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //엑티비티를 전체화면으로 열기
        getSupportActionBar().hide();

        setContentView(R.layout.activity_join);

        //FireBase 관련 객체 생성
        mAuth = FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();

        //ID 찾기
        edtId = findViewById(R.id.edtId);
        edtPw = findViewById(R.id.edtPw);
        edtPw2 = findViewById(R.id.edtPw2);
        edtName = findViewById(R.id.edtName);
        btnJoin = findViewById(R.id.btnJoin);

        //이벤트
        //회원가입
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString();
                String pw = edtPw.getText().toString();
                String pw2 = edtPw2.getText().toString();

                //유효성 체크
                if(isValidEmail(id)&&isValidPasswd(pw)){

                    if (!pw.equals(pw2)) {
                        Toast.makeText(JoinActivity.this, "패스워드를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    } else if (edtName.getText().toString().equals("")) {
                        Toast.makeText(JoinActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent i = new Intent(JoinActivity.this, MainActivity.class);

                        //다이얼로그 보이기
                        PrefUtil.showProgress(JoinActivity.this);

                        //FireBase 에 회원가입
                        createUser(id,pw);
                    }
                }
            }
        });
    }//end OnCreate

    //회원가입 처리
    private void createUser(String email, String pass){
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //회원가입 성공
                    DatabaseReference firebaseRef = mDatabase.getReference();
                    String UniqId = firebaseRef.push().getKey();//Key 를 ID로 사용

                    //데이터를 Bean 에 저장
                    mbBean = new MemberBean();
                    mbBean.setUniqueId(UniqId);
                    mbBean.setId(mAuth.getCurrentUser().getEmail());
                    mbBean.setPw(edtPw.getText().toString());
                    mbBean.setName(edtName.getText().toString());

                    //고유키를 생성
                    String emailUUID = getUserIdFromUUID(mbBean.getId());

                    //서버에 저장처리
                    firebaseRef.child("user").child(emailUUID).setValue(mbBean);

                    //안내
                    Toast.makeText(JoinActivity.this,"회원가입에 성공했습니다.",Toast.LENGTH_SHORT).show();

                    //다이얼로그 숨기기
                    PrefUtil.hideProgress(JoinActivity.this);

                    finish();

                }else{
                    //회원가입 실패
                    //안내
                    Toast.makeText(JoinActivity.this,"이미 사용중인 아이디 입니다.",Toast.LENGTH_SHORT).show();
                    //다이얼로그 숨기기
                    PrefUtil.hideProgress(JoinActivity.this);

                }
            }
        });
    }//end createUser()

    // 이메일 유효성 검사
    private boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            // 이메일 공백
            Toast.makeText(JoinActivity.this,"아이디를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            Toast.makeText(JoinActivity.this,"아이디에 이메일 형식으로 작성해주세요.",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd(String password) {
        if (password.isEmpty()) {
            // 비밀번호 공백
            Toast.makeText(JoinActivity.this,"패스워드를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(JoinActivity.this,"패스워드는 최소 6자리 이상 입력해주세요.",Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }
    //이메일의 문자 기준으로 고유번호를 추출
    public static String getUserIdFromUUID(String userEmail){
        long val = UUID.nameUUIDFromBytes(userEmail.getBytes()).getMostSignificantBits();
        return val+"";
    }



}
