package lesson.swu.oreoz_final_project.Bean;

import java.io.Serializable;
import java.util.List;

public class CommentListBean implements Serializable {
    private List<CommentBean> commentList;

    public List<CommentBean> getCommentList() { return commentList; }

    public void setCommentList(List<CommentBean> commentList) { this.commentList = commentList; }
}
