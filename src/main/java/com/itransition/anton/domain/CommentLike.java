package com.itransition.anton.domain;

import javax.persistence.*;

/**
 * Created by qanto on 09.09.2019.
 */
@Entity
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //vote: false - dislike, true - like
    private boolean vote;

    public CommentLike() {}

    public CommentLike(Comment comment, User user, boolean vote) {
        this.comment = comment;
        this.user = user;
        this.vote = vote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public boolean isVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }


}
