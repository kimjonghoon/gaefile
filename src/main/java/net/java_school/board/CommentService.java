package net.java_school.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentDao dao;

    public void addComment(Comment comment) {
        dao.addComment(comment);
    }

    public List<Comment> getComments() {
        return dao.getComments();
    }

    public void removeComment(int no) {
        dao.removeComment(no);
    }

    public Comment getComment(int no) {
        return dao.getComment(no);
    }

    public void modifyComment(Comment comment) {
        dao.modifyComment(comment);
    }
}
