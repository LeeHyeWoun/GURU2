package lesson.swu.oreoz_final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.UUID;

import lesson.swu.oreoz_final_project.Bean.MemberBean;
import lesson.swu.oreoz_final_project.Bean.SaveBean;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class MemberActivity extends AppCompatActivity {
    private TextView txtId,txtBook;
    private ImageView imgBook1,imgBook2,imgBook3;
    private EditText edtPw, edtPw2, edtName;
    private Button btnChangePW, btnEdit, btnLogout, btnSecession;
    private boolean chagePw;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    private MemberBean mbBean;


    @Override
    protected void onCreate(Bundle savedInstanceState){
     super.onCreate(savedInstanceState);

        //액션바 삭제
        getSupportActionBar().hide();

        setContentView(R.layout.activity_member);

        //FireBase 관련 객체 생성
        mAuth = FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();

        //ID 찾기
        txtId = findViewById(R.id.txtId);
        txtBook = findViewById(R.id.txtBook);
        edtPw = findViewById(R.id.edtPw);
        edtPw2 = findViewById(R.id.edtPw2);
        edtName = findViewById(R.id.edtName);
        btnChangePW = findViewById(R.id.btnChagePW);
        btnEdit = findViewById(R.id.btnEdit);
        btnLogout = findViewById(R.id.btnLogout);
        btnSecession = findViewById(R.id.btnSecession);
        imgBook1 = findViewById(R.id.imgBook1);
        imgBook2 = findViewById(R.id.imgBook2);
        imgBook3 = findViewById(R.id.imgBook3);

        //초기 설정
        //회원정보 데이터 받기
        String jsonData = PrefUtil.getData(MemberActivity.this,MemberBean.class.getName());
        Gson gSon = new Gson();
        mbBean = gSon.fromJson(jsonData, MemberBean.class);
        //회원정보 설정
        txtId.setText(mbBean.getId());
        edtPw.setText(mbBean.getPw());
        edtName.setText(mbBean.getName());
        //패스워드 변경 불가 설정
        edtPw.setEnabled(false);
        chagePw = false;

        //이벤트
        //패스워드 변경
        btnChangePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //변경
                if(!chagePw){
                    chagePw = true;
                    edtPw.setEnabled(true);
                    edtPw.setText("");
                    edtPw2.setText("");
                    btnChangePW.setText("취소");
                }
                //변경 취소
                else{
                    chagePw = false;
                    edtPw.setEnabled(false);
                    edtPw.setText(mbBean.getPw());
                    edtPw2.setText("");
                    btnChangePW.setText("변경");
                }
            }
        });

        //수정된 회원 정보 저장
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //패스워드 확인과 이름 중에 빈칸이 있는 경우
                if(edtPw.getText().toString().equals("")||edtName.getText().toString().equals("")){
                    Toast.makeText(MemberActivity.this,"정보를 모두 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                //패스워드 확인이 틀렸을 경우
                else if(!edtPw.getText().toString().equals(edtPw2.getText().toString())){
                    Toast.makeText(MemberActivity.this,"패스워드를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                }
                //패스워드 변경 시 이전 패스워드와 중복될 경우
                else if(chagePw==true && edtPw.getText().toString().equals(mbBean.getPw())){
                    Toast.makeText(MemberActivity.this,"이전 패스워드와 중복되므로 다른 패스워드를 이용해주세요.",Toast.LENGTH_SHORT).show();
                }
                //수정데이터에 변화가 없을 경우
                else if(edtPw.getText().toString().equals(mbBean.getPw())
                        &&edtName.getText().toString().equals(mbBean.getName())){
                    Toast.makeText(MemberActivity.this,"변경 사항이 없습니다.",Toast.LENGTH_SHORT).show();
                }
                //수정
                else{
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    String emailUUID = JoinActivity.getUserIdFromUUID(email);

                    //수정할 데이터를 Bean 에 바꿔 넣는다
                    mbBean.setPw(edtPw.getText().toString());
                    mbBean.setName(edtName.getText().toString());

                    //PrefUtil 에 저장
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(mbBean);
                    PrefUtil.setData(MemberActivity.this,MemberBean.class.getName(),jsonStr);

                    //서버에 수정처리
                    mDatabase.getReference().child("user").child(emailUUID).setValue(mbBean);

                    //안내
                    Toast.makeText(MemberActivity.this, "회원정보를 수정했습니다.", Toast.LENGTH_SHORT).show();
                    chagePw = false;
                    edtPw.setEnabled(false);

                }
            }
        });

        //로그아웃
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //구글에서 로그아웃
                mAuth.signOut();

                PrefUtil.setData(MemberActivity.this,"login",false);

                //로그인 화면으로 이동
                Intent i = new Intent(MemberActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        //회원탈퇴
        btnSecession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이얼로그
                AlertDialog.Builder builder = new AlertDialog.Builder(MemberActivity.this);
                builder.setTitle("경고");
                builder.setIcon(R.drawable.icon_caution);
                builder.setMessage("정말로 회원탈퇴를 하시겠습니까?");
                builder.setCancelable(false);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PrefUtil.showProgress(MemberActivity.this);

                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                        String emailUUID = JoinActivity.getUserIdFromUUID(email);

                        //데이터 베이스의 사용자 정보와 MemberBean 의 데이터 삭제
                        FirebaseDatabase.getInstance().getReference().child("user").child(emailUUID).removeValue();
                        PrefUtil.setData(MemberActivity.this, MemberBean.class.getName(), "");
                        PrefUtil.setData(MemberActivity.this,"login",false);

                        //Authentication 에서의 사용자 삭제
                        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //다이얼로그 숨기기
                                    PrefUtil.hideProgress(MemberActivity.this);

                                    //안내
                                    Toast.makeText(MemberActivity.this, "탈퇴처리 되었습니다.", Toast.LENGTH_SHORT).show();

                                    //로그인 화면으로 이동
                                    Intent i = new Intent(MemberActivity.this,MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
            }
        });
    }//OnCreate()

    //이메일의 문자 기준으로 고유번호를 추출
    public static String getUserIdFromUUID(String userEmail){
        long val = UUID.nameUUIDFromBytes(userEmail.getBytes()).getMostSignificantBits();
        return val+"";
    }

}
