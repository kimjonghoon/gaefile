package net.java_school.board;

import java.util.Date;

public class Comment {

    private static int COMMENT_NO = 1;
    private int commentNo;
    private String username;
    private String content;
    private Date creation;
    private boolean editable;

    public Comment() {
        username = "John Doe";
        commentNo = COMMENT_NO++;
        creation = new Date();
        editable = true;
    }

    public int getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(int commentNo) {
        this.commentNo = commentNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
