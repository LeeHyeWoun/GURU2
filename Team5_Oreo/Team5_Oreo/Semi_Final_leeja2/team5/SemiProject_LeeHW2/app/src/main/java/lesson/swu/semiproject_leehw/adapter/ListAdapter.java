package lesson.swu.semiproject_leehw.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
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

import lesson.swu.semiproject_leehw.Bean.MemoBean;
import lesson.swu.semiproject_leehw.Bean.SaveBean;
import lesson.swu.semiproject_leehw.MemoDetailActivity;
import lesson.swu.semiproject_leehw.R;
import lesson.swu.semiproject_leehw.fragment.ListTab1Fragment;
import lesson.swu.semiproject_leehw.util.PrefUtil;

public class ListAdapter extends BaseAdapter {
    private Context mContext;
    private List<MemoBean> mList;
    private TextView mTxtEmpty;

    public ListAdapter(Context context, List<MemoBean> list, TextView txtEmpty){
        mContext = context;
        mList = list;
        mTxtEmpty = txtEmpty;

        if(mList.size() > 0) {
            mTxtEmpty.setVisibility(View.INVISIBLE);
        } else{
            mTxtEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getCount() { return mList.size();}
    @Override
    public Object getItem(int position) {return position;}
    @Override
    public long getItemId(int position) { return position;}
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //인플레이팅하는 작업
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_memo,null);

        //해당 ROW 의 데이터를 찾는 작업
        final MemoBean memoBean = mList.get(position);

        //인플레이팅 된 뷰에서 ID를 찾는 작업
        ImageView imgMemo = convertView.findViewById(R.id.imageView);
        TextView txtMemo = convertView.findViewById(R.id.txtMemo);
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnDetail = convertView.findViewById(R.id.btnDetail);

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(memoBean.getMemo_img());
            imgMemo.setImageBitmap(bitmap);
        }catch(OutOfMemoryError oom){
            oom.printStackTrace();
        }

        //데어터 세팅
        imgMemo.setImageBitmap(bitmap);
        txtMemo.setText(memoBean.getMemo_txt());
        txtDate.setText(memoBean.getDate());//시험중

        //이벤트 설정-------------------------------------------------------------------------------
        //수정
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, MemoDetailActivity.class);
                //선택된 ROW 의 Bean 데이터를 싣는다.
                i.putExtra(MemoBean.class.getName(),memoBean);
                i.putExtra("position",position);
                mContext.startActivity(i);//화면이동
            }
        });
        //삭제
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(memoBean.getMemo_txt());
                builder.setIcon(R.drawable.icon_delete);
                builder.setMessage("해당 메모를 삭제하시겠습니까?");
                builder.setCancelable(false);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //PrefUtil 에서 saveBean 취득
                        String jsonStr = PrefUtil.getData(mContext, SaveBean.class.getName());
                        Gson gson = new Gson();
                        SaveBean saveBean = gson.fromJson(jsonStr, SaveBean.class);

                        //2~3
                        saveBean.getMemoList().remove(position);

                        //SaveBean 저장
                        PrefUtil.setData(mContext, SaveBean.class.getName(), gson.toJson(saveBean));

                        //리스트
                        mList.remove(position);
                        //notify
                        notifyDataSetChanged();

                        if(mList.size() > 0) {
                            mTxtEmpty.setVisibility(View.INVISIBLE);
                        } else{
                            mTxtEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();
            }
        });
        //상세보기
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, MemoDetailActivity.class);
                //선택된 ROW 의 Bean 데이터를 싣는다.
                i.putExtra(MemoBean.class.getName(),memoBean);
                i.putExtra("position",position);
                mContext.startActivity(i);//화면이동
            }
        });

        return convertView;

    }//end getView();
}
