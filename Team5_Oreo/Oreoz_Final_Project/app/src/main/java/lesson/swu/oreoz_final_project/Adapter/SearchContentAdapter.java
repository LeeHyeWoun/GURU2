package lesson.swu.oreoz_final_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import lesson.swu.oreoz_final_project.Bean.BookBean;
import lesson.swu.oreoz_final_project.DetailActivity;
import lesson.swu.oreoz_final_project.DownImgTask;
import lesson.swu.oreoz_final_project.R;

public class SearchContentAdapter extends BaseAdapter {
    private Context mContext;
    private List<BookBean> mList;
    private String mSearchWord;

    //생성자
    public SearchContentAdapter(Context context, List<BookBean> list, String searchWord){
        mContext = context;
        mList = list;
        mSearchWord = searchWord;
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
        convertView = inflater.inflate(R.layout.view_search_content, null);

        //해당 ROW 의 데이터를 찾는 작업
        final BookBean bookBean = mList.get(position);

        //인플레이팅 된 뷰에서 ID 찾는작업
        ImageView imgBook = convertView.findViewById(R.id.imgBook);
        TextView txtTitle = convertView.findViewById(R.id.txtTitle);
        TextView txtAuthor = convertView.findViewById(R.id.txtWriter);
        TextView txtSign = convertView.findViewById(R.id.txtCode);
        TextView txtSign2 = convertView.findViewById(R.id.txtCode2);
        TextView txtRental = convertView.findViewById(R.id.txtBorrow);
        TextView txtCount = convertView.findViewById(R.id.txtCount);

        //데이터 셋팅
        //책 제목
        if(bookBean.getTitle().contains(mSearchWord)){
            //키워드를 색상으로 표시
            SpannableStringBuilder sTxtTitle = new SpannableStringBuilder(bookBean.getTitle());
            int start = bookBean.getTitle().indexOf(mSearchWord); //키워드가 시작하는 위치
            sTxtTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")),  start, start+mSearchWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtTitle.setText(sTxtTitle);
            txtAuthor.setText(bookBean.getAuthor());
        }
        //저자
        else {
            //키워드를 색상으로 표시
            SpannableStringBuilder sAuthor = new SpannableStringBuilder(bookBean.getAuthor());
            int start = bookBean.getAuthor().indexOf(mSearchWord); //키워드가 시작하는 위치
            sAuthor.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")),  start, start+mSearchWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtAuthor.setText(sAuthor);
            txtTitle.setText(bookBean.getTitle());
        }
        //청구기호
        txtSign.setText(bookBean.getSign().get(0));
        if(bookBean.getSign().size()>1){
            String count = "(" + String.valueOf(bookBean.getSign().size())+"권)";
            txtCount.setText(count);
        }
        else{ txtCount.setVisibility(View.INVISIBLE); }
        //대출여부
        if(bookBean.getRental()){
            txtRental.setText("대여\n가능");
            txtRental.setTextColor(Color.parseColor("#80888888"));
        }else{
            txtRental.setText("대여중");
            txtRental.setTextColor(Color.RED);
            if(bookBean.getSign().size()<=1){
                txtSign.setTextColor(Color.parseColor("#80888888"));
                txtSign2.setTextColor(Color.parseColor("#80888888"));
            }
        }
        //이미지 설정
        try{
            new DownImgTask(imgBook).execute(new URL(bookBean.getImgUrl()));
        }catch (Exception e){e.printStackTrace();}


        //이벤트 설정
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetailActivity.class);
                //선택된 ROW의 Bean 데이터를 싣는다.
                i.putExtra(BookBean.class.getName(), bookBean);
                mContext.startActivity(i); //화면이동
            }
        });

        return convertView;
    }
}
