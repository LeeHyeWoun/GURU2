package lesson.swu.semiproject_leehw.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import lesson.swu.semiproject_leehw.Bean.MemberBean;
import lesson.swu.semiproject_leehw.Bean.SaveBean;
import lesson.swu.semiproject_leehw.MainActivity;
import lesson.swu.semiproject_leehw.R;
import lesson.swu.semiproject_leehw.util.PrefUtil;

public class ListTab2Fragment extends Fragment {

    private TextView txtId;
    private EditText edtPw, edtPw2, edtName, edtYyyy, edtMm, edtDd;
    private RadioButton rbWoman, rbMan;
    private Button btnChangePW, btnEdit, btnLogout, btnSecession;
    private boolean chagePw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_tab2,null);

        txtId = view.findViewById(R.id.txtId);
        edtPw = view.findViewById(R.id.edtPw);
        edtPw2 = view.findViewById(R.id.edtPw2);
        edtName = view.findViewById(R.id.edtName);
        edtYyyy = view.findViewById(R.id.edtYyyy);
        edtMm = view.findViewById(R.id.edtMm);
        edtDd = view.findViewById(R.id.edtDd);
        rbWoman = view.findViewById(R.id.rbWoman);
        rbMan = view.findViewById(R.id.rbMan);
        btnChangePW = view.findViewById(R.id.btnChagePW);
        btnEdit = view.findViewById(R.id.btnEdit);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnSecession = view.findViewById(R.id.btnSecession);

        edtPw.setEnabled(false);
        chagePw = false;

        String jsonStr = PrefUtil.getData(getActivity(), MemberBean.class.getName());
        Gson gson = new Gson();
        final MemberBean mbBean = gson.fromJson(jsonStr, MemberBean.class);

        //회원정보
        txtId.setText(mbBean.getId());
        edtPw.setText(mbBean.getPw());
        edtName.setText(mbBean.getName());
        edtYyyy.setText(mbBean.getYyyy());
        edtMm.setText(mbBean.getMm());
        edtDd.setText(mbBean.getDd());
        if(mbBean.getSex().toString().equals("woman")){
            rbWoman.setChecked(true);
        }
        else{rbMan.setChecked(true);}

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
                if(edtName.getText().toString().equals("")
                        || edtYyyy.getText().toString().equals("")
                        || edtMm.getText().toString().equals("")
                        || edtDd.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"정보를 전부 채워주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(edtPw.getText().toString().equals("")||!edtPw.getText().toString().equals(edtPw2.getText().toString())){
                    Toast.makeText(getActivity(),"패스워드를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(chagePw==true && edtPw.getText().toString().equals(mbBean.getPw())){
                    Toast.makeText(getActivity(),"이전 패스워드와 중복되므로 다른 패스워드를 이용해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(edtPw.getText().toString().equals(mbBean.getPw())
                        &&edtName.getText().toString().equals(mbBean.getName())
                        && edtYyyy.getText().toString().equals(mbBean.getYyyy())
                        && edtMm.getText().toString().equals(mbBean.getMm())
                        && edtDd.getText().toString().equals(mbBean.getDd())
                        && ((rbWoman.isChecked()==true&&mbBean.getSex().equals("woman"))||(rbMan.isChecked()==true&&mbBean.getSex().equals("man")))){
                    Toast.makeText(getActivity(),"변경사항이 없습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    mbBean.setPw(edtPw.getText().toString());
                    mbBean.setName(edtName.getText().toString());
                    mbBean.setYyyy(edtYyyy.getText().toString());
                    mbBean.setMm(edtMm.getText().toString());
                    mbBean.setDd(edtDd.getText().toString());
                    if(rbWoman.isChecked()){
                        mbBean.setSex("woman");
                    }
                    else{
                        mbBean.setSex("man");
                    }

                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(mbBean); //class -> json
                    PrefUtil.setData(getActivity(), MemberBean.class.getName(), jsonStr);

                    Toast.makeText(getActivity(), "회원정보를 수정했습니다.", Toast.LENGTH_SHORT).show();
                    chagePw = false;
                    edtPw.setEnabled(false);

                }
            }
        });

        //로그아웃
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        //회원탈퇴
        btnSecession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("경고");
                builder.setIcon(R.drawable.icon_caution);
                builder.setMessage("저장된 메모들을 잃게 됩니다.\n정말로 회원탈퇴를 하시겠습니까?");
                builder.setCancelable(false);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(),MainActivity.class);

                        //MemberBean 데이터 삭제...회원가입정보 삭제
                        PrefUtil.setData(getActivity(), MemberBean.class.getName(), null);
                        //SaveBean 데이터 삭제...메모 리스트 삭제
                        PrefUtil.setData(getActivity(), SaveBean.class.getName(), null);

                        Toast.makeText(getActivity(), "탈퇴처리 되었습니다.", Toast.LENGTH_SHORT).show();

                        startActivity(i);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
            }
        });

        return view;
    }//OnCreate()
}
