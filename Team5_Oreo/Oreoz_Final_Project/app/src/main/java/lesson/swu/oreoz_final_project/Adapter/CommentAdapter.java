package lesson.swu.oreoz_final_project.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import lesson.swu.oreoz_final_project.Bean.CommentBean;
import lesson.swu.oreoz_final_project.Bean.MemberBean;
import lesson.swu.oreoz_final_project.R;
import lesson.swu.oreoz_final_project.util.PrefUtil;


public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<CommentBean> mList;
    private String mUUID;
    private MemberBean mMemberBean;

    //생성자
    public CommentAdapter(Context context, List<CommentBean> list, String UUID, MemberBean memberBean) {
        mContext = context;
        mList = list;
        mUUID = UUID;
        mMemberBean = memberBean;
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
        convertView = inflater.inflate(R.layout.view_detail_comment, null);

        //해당 ROW 의 데이터를 찾는 작업
        final CommentBean commentBean = mList.get(position);

        //인플레이팅 된 뷰에서 ID 찾는작업
        LinearLayout background = convertView.findViewById(R.id.background);
        TextView txtUserName = convertView.findViewById(R.id.txtUserName);
        TextView txtComment = convertView.findViewById(R.id.txtComment);
        ImageView imgDelete = convertView.findViewById(R.id.imgDelete);

        //데이터 셋팅
        txtUserName.setText(commentBean.getUserName());
        txtComment.setText(commentBean.getComment());

        //사용자가 작성한 댓글 차별화
        final Boolean login = PrefUtil.getDataBoolean(mContext,"login");
        if(mMemberBean!=null&&login==true){
            if (commentBean.getUserId().equals(mMemberBean.getId())) {
                //사용자가 작성한 댓글은 글쓴이의 이름이 빨간색이 되고 삭제가 가능해집니다.
                background.setBackgroundColor(Color.parseColor("#FEF9E7"));
                txtUserName.append("(자신의 닉네임)");
                imgDelete.setVisibility(View.VISIBLE);
            }
        }

        //이벤트
        //삭제
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Date = mList.get(position).getDay();

                //데이터 삭제
                FirebaseDatabase.getInstance().getReference().child("comment").child(mUUID).child(Date).removeValue();
            }
        });

        return convertView;
    }
}
