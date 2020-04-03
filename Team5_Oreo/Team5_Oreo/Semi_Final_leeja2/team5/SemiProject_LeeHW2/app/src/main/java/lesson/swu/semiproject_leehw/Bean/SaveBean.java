package lesson.swu.semiproject_leehw.Bean;

import java.io.Serializable;
import java.util.List;

public class SaveBean implements Serializable {
    private MemberBean memberBean;
    private List<MemoBean> memoList;

    //getter
    public MemberBean getMemberBean() {return memberBean;}
    public List<MemoBean> getMemoList() { return memoList;}

    //setter
    public void setMemberBean(MemberBean memberBean) { this.memberBean = memberBean;}
    public void setMemoList(List<MemoBean> memoList) { this.memoList = memoList;}
}
