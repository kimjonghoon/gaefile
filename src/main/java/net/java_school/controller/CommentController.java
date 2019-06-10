package net.java_school.controller;

import java.util.List;

import net.java_school.board.Comment;
import net.java_school.board.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService service;

    @PostMapping
    public void addComment(Comment comment) {
        service.addComment(comment);
    }

    @GetMapping
    public List<Comment> getComments() {
        return service.getComments();
    }

    @DeleteMapping("/{no}")
    public void removeComment(@PathVariable Integer no) {
        service.removeComment(no);
    }

    @PutMapping("/{no}")
    public void modifyComment(String content, @PathVariable Integer no) {
        Comment comment = this.getComment(no);
        comment.setContent(content);
        service.modifyComment(comment);
    }

    @RequestMapping(value = "/{no}", method = RequestMethod.GET)
    public Comment getComment(@PathVariable Integer no) {
        return service.getComment(no);
    }

}
