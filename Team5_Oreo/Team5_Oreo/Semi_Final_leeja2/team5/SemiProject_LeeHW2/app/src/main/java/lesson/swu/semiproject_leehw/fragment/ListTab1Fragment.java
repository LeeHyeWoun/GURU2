package lesson.swu.semiproject_leehw.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import lesson.swu.semiproject_leehw.Bean.MemoBean;
import lesson.swu.semiproject_leehw.Bean.SaveBean;
import lesson.swu.semiproject_leehw.MemoActivity;
import lesson.swu.semiproject_leehw.R;
import lesson.swu.semiproject_leehw.adapter.ListAdapter;
import lesson.swu.semiproject_leehw.util.PrefUtil;

public class ListTab1Fragment extends Fragment {
    private Button btnMemo;
    private ListView list;
    private TextView txtEmpty;
    ListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_tab1,null);

        btnMemo = view.findViewById(R.id.btnMemo);
        list = view.findViewById(R.id.list);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        btnMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),MemoActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
            super.onResume();

        //리스트뷰를 갱신
        getList();
    }


    private void getList() {
        Gson gson = new Gson();
        String jsonStr = PrefUtil.getData(getActivity(),SaveBean.class.getName());
        SaveBean saveBean = new SaveBean();

        if(jsonStr.length() > 0) {
            saveBean = gson.fromJson(jsonStr, SaveBean.class);
        }

        if(saveBean.getMemoList() == null) {
            saveBean.setMemoList( new ArrayList<MemoBean>() );
        }

        //Adapter 생성
        listAdapter = new ListAdapter(getActivity(), saveBean.getMemoList(), txtEmpty);

        list.setAdapter(listAdapter);
    }

}
