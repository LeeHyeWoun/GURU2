package lesson.swu.oreoz_final_project.Bean;

import java.io.Serializable;
import java.util.List;

public class MemberBean implements Serializable {

    private String uniqueId;            //게시글 고유 아이디
    private String id;                  //게시글 소유자 아이디
    private String pw;                  //패스워드
    private String name;                //유저 이름
    private List<CommentBean> commentList;    //활동 내용 저장

    //getter
    public String getUniqueId() { return uniqueId;}
    public String getId() { return id;}
    public String getPw() { return pw;}
    public String getName() { return name;}
    public List<CommentBean> getCommentList() { return commentList; }

    //setter
    public void setUniqueId(String userId) { this.uniqueId = userId;}
    public void setId(String id) { this.id = id;}
    public void setPw(String pw) { this.pw = pw;}
    public void setName(String name) { this.name = name;}
    public void setCommentList(List<CommentBean> commentList) { this.commentList = commentList; }
}
