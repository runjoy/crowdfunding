package com.itransition.anton.service;

import com.itransition.anton.domain.Comment;
import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.CommentLike;
import com.itransition.anton.domain.User;
import com.itransition.anton.repo.CommentRepo;
import com.itransition.anton.repo.CommentLikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by qanto on 09.09.2019.
 */

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private CommentLikeRepo commentLikeRepo;

    public Comment addComment(User author, Company company, String text) {
        Comment comment = new Comment(author, company, text, new Date());
        commentRepo.save(comment);
        return comment;
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepo.findById(commentId).get();
        commentRepo.delete(comment);
    }

    public void editComment(Long commentId, String text) {
        Comment comment = commentRepo.findById(commentId).get();
        comment.setText(text);
        commentRepo.save(comment);
    }

    public void addLike(Comment comment, User user, boolean vote) {
        if (!commentLikeRepo.findByUserIdAndComment(user.getId(), comment).isPresent()) {
            CommentLike commentLike = new CommentLike(comment, user, vote);
            commentLikeRepo.save(commentLike);
        }
    }

    public List<Comment> getComment(Company company) {
        return company.getCommentList();
    }
}
