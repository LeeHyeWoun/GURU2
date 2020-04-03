package lesson.swu.semiproject_leehw.fragment;

import android.content.Context;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.zip.Inflater;

import lesson.swu.semiproject_leehw.Bean.MemoBean;
import lesson.swu.semiproject_leehw.Bean.SaveBean;
import lesson.swu.semiproject_leehw.R;
import lesson.swu.semiproject_leehw.util.PrefUtil;

public class MemoTab1Fragment extends Fragment {

    private EditText edtMemo;
    private MemoBean memoBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo_tab1,null);

        edtMemo = view.findViewById(R.id.edtMemo);

        //넘겨준 데이터를 받는다.
        memoBean = (MemoBean) getActivity().getIntent().getSerializableExtra(MemoBean.class.getName());

        if(memoBean != null){
            edtMemo.setText(memoBean.getMemo_txt());
        }

        return view;
    }

}
