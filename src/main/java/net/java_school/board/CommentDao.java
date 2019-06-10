package net.java_school.board;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@ApplicationScope
@Component
public class CommentDao {

    private final List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void removeComment(int no) {
        for (Comment comment : comments) {
            if (comment.getCommentNo() == no) {
                comments.remove(comment);
                break;
            }
        }
    }

    public void modifyComment(Comment comment) {
        for (Comment c : comments) {
            if (c.getCommentNo() == comment.getCommentNo()) {
                c = comment;
                break;
            }
        }
    }

    public Comment getComment(int no) {
        Comment ret = null;
        for (Comment comment : comments) {
            if (comment.getCommentNo() == no) {
                ret = comment;
                break;
            }
        }

        return ret;
    }

}
