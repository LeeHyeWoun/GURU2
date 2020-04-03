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

import java.net.URL;
import java.util.List;

import lesson.swu.oreoz_final_project.Bean.BookBean;
import lesson.swu.oreoz_final_project.Bean.SaveBean;
import lesson.swu.oreoz_final_project.Bean.SearchListBean;
import lesson.swu.oreoz_final_project.DetailActivity;
import lesson.swu.oreoz_final_project.DownImgTask;
import lesson.swu.oreoz_final_project.R;
import lesson.swu.oreoz_final_project.ScrapActivity;
import lesson.swu.oreoz_final_project.SearchContentActivity;
import lesson.swu.oreoz_final_project.util.PrefUtil;

public class ScrapAdapter extends BaseAdapter {


    private Context mContext;
    private List<BookBean> mList;

    //생성자
    public ScrapAdapter(Context context, List<BookBean> list) {
        mContext = context;
        mList = list;
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
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.view_scrap, null);

        //해당 ROW 의 데이터를 찾는 작업
        final BookBean bookBean = mList.get(position);

        //인플레이팅 된 뷰에서 ID 찾는작업
        ImageView imgBook = convertView.findViewById( R.id.imgBook );
        final TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtWriter = convertView.findViewById(R.id.txtWriter);
        TextView txtSign = convertView.findViewById(R.id.txtSign);

        //화면에 표시한다.
        txtTitle.setText(bookBean.getTitle());
        txtWriter.setText(bookBean.getAuthor());
        txtSign.setText(bookBean.getSign().get(0));
        if(bookBean.getSign().size()>1){
            txtSign.append("\n"+bookBean.getSign().get(1));
        }
        //책 이미지 설정
        try{
            new DownImgTask(imgBook).execute(new URL(bookBean.getImgUrl()));
        }catch (Exception e){e.printStackTrace();}

        //이벤트 설정
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailActivity.class);
                //선택된 ROW의 Bean 데이터를 싣는다.
                i.putExtra(BookBean.class.getName(),bookBean);
                mContext.startActivity(i); //화면이동
            }
        });

        return convertView;
    }//end getView
}
