package lesson.swu.oreoz_final_project.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lesson.swu.oreoz_final_project.Adapter.CommentAdapter;
import lesson.swu.oreoz_final_project.Bean.BookBean;
import lesson.swu.oreoz_final_project.Bean.CommentBean;
import lesson.swu.oreoz_final_project.Bean.CommentListBean;
import lesson.swu.oreoz_final_project.Bean.MemberBean;
import lesson.swu.oreoz_final_project.Bean.SearchListBean;
import lesson.swu.oreoz_final_project.MainActivity;
import lesson.swu.oreoz_final_project.MemberActivity;
import lesson.swu.oreoz_final_project.R;
import lesson.swu.oreoz_final_project.SearchActivity;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class Detail2Fragment extends Fragment {
    private EditText edtComment;
    private Button btnUpload;
    private ListView commentList;
    private ListAdapter commentAdapter;
    private BookBean bookBean;
    private MemberBean memberBean;
    private CommentBean commentBean;
    private CommentListBean commentListBean;
    private List<CommentBean> mCommentList = new ArrayList<CommentBean>();
    private List<CommentBean> mAllCommentList = new ArrayList<CommentBean>();

    private FirebaseDatabase mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail2,null);

        //ID 찾기
        edtComment = view.findViewById(R.id.edtComment);
        btnUpload = view.findViewById(R.id.btnUpload);
        commentList = view.findViewById(R.id.commentList);

        //파이어베이스 객체 생성
        mDatabase=FirebaseDatabase.getInstance();

        commentListBean = new CommentListBean();


        String jsonData = PrefUtil.getData(getActivity(),MemberBean.class.getName());
        Gson gson = new Gson();
        memberBean = gson.fromJson(jsonData,MemberBean.class);

        final Boolean login = PrefUtil.getDataBoolean(getActivity(),"login");
        if(login==null||login==false){
            edtComment.setHint("로그인 후 이용해주세요.");
        }else{
            edtComment.setHint("댓글을 작성해주세요.");
        }

        //이벤트
        //댓글 입력
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(memberBean == null||login==null||login==false){
                    //다이얼로그
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("로그인이 되어있지 않습니다.");
                    builder.setIcon(R.drawable.icon_caution);
                    builder.setMessage("로그인 창으로 이동하시겠습니까?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getActivity(),MainActivity.class);
                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("취소", null);
                    builder.show();
                }
                //로그인이 되어있을 시
                else{
                    if(edtComment.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"댓글을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //다이얼로그 보이기
                        PrefUtil.showProgress(getActivity());

                        DatabaseReference firebaseRef = mDatabase.getReference();

                        //넘겨준 데이터를 받는다.
                        bookBean = (BookBean) getActivity().getIntent().getSerializableExtra(BookBean.class.getName());

                        //CommentBean 설정
                        CommentBean commentBean = new CommentBean();
                        commentBean.setTitle(bookBean.getTitle());              //책 제목 설정
                        commentBean.setUserId(memberBean.getId());              //유저 아이디(이메일) 설정
                        commentBean.setUserName(memberBean.getName());          //유저 이름 설정
                        commentBean.setComment(edtComment.getText().toString());//댓글 설정
                        commentBean.setDay(new Date().toString());              //댓글 쓴 날짜 설정

                        //서버에 저장처리
                        String titleUUID = getUserIdFromUUID(bookBean.getTitle());
                        firebaseRef.child("comment").child(titleUUID).child(new Date().toString()).setValue(commentBean);

                        //입력란은 초기화
                        edtComment.setText(null);

                        //안내
                        Toast.makeText(getActivity(),"댓글을 등록했습니다.",Toast.LENGTH_SHORT).show();

//                        commentAdapter.notify();
                        //다이얼로그 숨기기
                        PrefUtil.hideProgress(getActivity());

                    }
                }
            }
        });
        return view;


    }//end onCreateView

    @Override
    public void onResume() {
        super.onResume();

        getList();
    }//end onResume

    // list 업데이트
    private void getList() {
        //넘겨준 데이터를 받는다.
        bookBean = (BookBean) getActivity().getIntent().getSerializableExtra(BookBean.class.getName());

        //파이어베이스로부터 데이터 가져오기
        final String titleUUID = getUserIdFromUUID(bookBean.getTitle());
        mDatabase.getReference().child("comment").child(titleUUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //실시간으로 서버가 변경된 내용이 있을 경우 호출됩니다.
                mAllCommentList.clear();//기존의 리스트 초기화
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    CommentBean bean = snapshot.getValue(CommentBean.class);
                    mAllCommentList.add(bean);
                }

                //현재 책제목으로 묶인 댓글들만 모으기
                int count = 0;
                mCommentList.clear();
                while (count<mAllCommentList.size()){
                    commentBean = mAllCommentList.get(count);
                    if(commentBean.getTitle().equals(bookBean.getTitle())){
                        mCommentList.add(commentBean);
                    }
                    count++;
                }

                //Adapter 생성
                commentAdapter = new CommentAdapter(getContext(), mCommentList,titleUUID, memberBean);
                commentList.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }//end getList

    //이메일의 문자 기준으로 고유번호를 추출
    public static String getUserIdFromUUID(String title){
        long val = UUID.nameUUIDFromBytes(title.getBytes()).getMostSignificantBits();
        return val+"";
    }


}//end class
