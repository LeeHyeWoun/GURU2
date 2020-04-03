package lesson.swu.oreoz_final_project.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import lesson.swu.oreoz_final_project.Bean.BookBean;
import lesson.swu.oreoz_final_project.BookpositionActivity;
import lesson.swu.oreoz_final_project.R;
import lesson.swu.oreoz_final_project.SearchContentActivity;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class Detail1Fragment extends Fragment {
    private TextView txtTitle, txtAuthor, txtPublisher, txtSign, txtSummary;
    private Button btnMap;
    private BookBean bookBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail1,null);

        //넘겨준 데이터를 받는다.
        bookBean = (BookBean) getActivity().getIntent().getSerializableExtra(BookBean.class.getName());

        //검색어 받기
        String searchWord = PrefUtil.getData(getActivity(),"edtSearch");

        //ID찾기
        txtTitle = view.findViewById(R.id.txtTitle);
        txtAuthor = view.findViewById(R.id.txtAuthor);
        txtPublisher = view.findViewById(R.id.txtPublisher);
        txtSign = view.findViewById(R.id.txtSign);
        txtSummary = view.findViewById(R.id.txtSummary);
        btnMap = view.findViewById(R.id.btnMap);

        //프레그먼트 안 데이터 설정
        //책 제목
        if(bookBean.getTitle().contains(searchWord)){
            //키워드를 색상으로 표시
            SpannableStringBuilder sTxtTitle = new SpannableStringBuilder(bookBean.getTitle());
            int start = bookBean.getTitle().indexOf(searchWord); //키워드가 시작하는 위치
            sTxtTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")),  start, start+searchWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtTitle.setText(sTxtTitle);
            txtAuthor.setText(bookBean.getAuthor());
        }
        //저자
        else {
            //키워드를 색상으로 표시
            SpannableStringBuilder sAuthor = new SpannableStringBuilder(bookBean.getAuthor());
            int start = bookBean.getAuthor().indexOf(searchWord); //키워드가 시작하는 위치
            sAuthor.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")),  start, start+searchWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtAuthor.setText(sAuthor);
            txtTitle.setText(bookBean.getTitle());
        }
        //출판사
        txtPublisher.setText(bookBean.getPublisher());
        //청구기호
        txtSign.setText(bookBean.getSign().get(0));
        if(bookBean.getSign().size()>1){
            txtSign.append("\n"+bookBean.getSign().get(1));
        }
        txtSummary.setText(bookBean.getSummary());


        //이벤트
        //책 찾으러 가기
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),BookpositionActivity.class);
                Gson gson = new Gson();
                PrefUtil.setData(getActivity(),BookBean.class.getName(),gson.toJson(bookBean));
                startActivity(i);
            }
        });//end btnMap clickEvent

        return view;
    }
}
