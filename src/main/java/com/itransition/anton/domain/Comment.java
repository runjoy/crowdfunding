package com.itransition.anton.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by qanto on 09.09.2019.
 */

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(length=2000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CommentLike> commentLikeList;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    public Comment() {}

    public Comment(User author, Company company, String text, Date createdTime) {
        this.author = author;
        this.company = company;
        this.text = text;
        this.createdTime = createdTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<CommentLike> getCommentLikeList() {
        return commentLikeList;
    }

    public void setCommentLikeList(List<CommentLike> commentLikeList) {
        this.commentLikeList = commentLikeList;
    }

    public int getCountLike() {
        int count = 0;
        for(CommentLike like : commentLikeList) {
            if (like.isVote()) count++;
        }
        return count;
    }

    public int getCountDislike() {
        int count = 0;
        for(CommentLike like : commentLikeList) {
            if (!like.isVote()) count++;
        }
        return count;
    }


}
