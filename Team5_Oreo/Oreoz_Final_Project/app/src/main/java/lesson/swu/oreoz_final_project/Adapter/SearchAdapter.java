package lesson.swu.oreoz_final_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import lesson.swu.oreoz_final_project.Bean.SaveBean;
import lesson.swu.oreoz_final_project.Bean.SearchBean;
import lesson.swu.oreoz_final_project.R;
import lesson.swu.oreoz_final_project.ScrapActivity;
import lesson.swu.oreoz_final_project.SearchActivity;
import lesson.swu.oreoz_final_project.SearchContentActivity;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class SearchAdapter extends BaseAdapter {


    private Context mContext;
    private List<SearchBean> mList;
    private TextView mTxtRecord;
    private TextView mEdtSearch;

    //생성자
    public SearchAdapter(Context context, List<SearchBean> list, TextView txtRecord, TextView edtSearch) {
        mContext = context;
        mList = list;
        mTxtRecord = txtRecord;
        mEdtSearch = edtSearch;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //인플레이팅 하는 작업
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.view_search_history, null);
        Button btnDeleteHistory = convertView.findViewById(R.id.btnDeleteHistory);

        //해당 ROW 의 데이터를 찾는 작업
         final SearchBean searchBean = mList.get(position);

        //인플레이팅 된 뷰에서 ID 찾는작업
        final TextView txtSearch = convertView.findViewById(R.id.txtSearch);

        //데이터 셋팅
        txtSearch.setText(searchBean.getText());


        //이벤트 설정
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, SearchContentActivity.class);
                //선택된 ROW의 Bean 데이터를 싣는다.
                i.putExtra(SearchBean.class.getName(), searchBean);
                mEdtSearch.setText(txtSearch.getText());
                PrefUtil.setData(mContext,"edtSearch", txtSearch.getText().toString());
                mContext.startActivity(i); //화면이동
            }
        });

        // 기록삭제 버튼
        btnDeleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //json 받아오기
                String jsonStr = PrefUtil.getData(mContext, SaveBean.class.getName());
                Gson gson = new Gson();
                SaveBean saveBean = gson.fromJson(jsonStr, SaveBean.class);

                saveBean.getSearchList().remove(position);

                // PrefUtill 저장
                PrefUtil.setData(mContext, SaveBean.class.getName(), gson.toJson(saveBean));

                //List
                mList.remove(position);

                //기록 존재 여부 보여주기
                if(mList.size()<=0){
                    mTxtRecord.setText("검색 기록이 없습니다.");
                }
                // notify
                notifyDataSetChanged();

            }
        });

        return convertView;

    }//end convertView
}
