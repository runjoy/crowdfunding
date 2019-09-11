package com.itransition.anton.domain;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qanto on 05.09.2019.
 */

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private double goalAmount;

    private String video;

    private Topic topic;

    private Date finalDate;

    @Column(length=10000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CompanyRating> companyRatingList;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<News> newsList;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Bonus> bonusList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(double goalAmount) {
        this.goalAmount = goalAmount;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<CompanyRating> getCompanyRatingList() {
        return companyRatingList;
    }

    public void setCompanyRatingList(List<CompanyRating> companyRatingList) {
        this.companyRatingList = companyRatingList;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Bonus> getBonusList() {
        return bonusList;
    }

    public void setBonusList(List<Bonus> bonusList) {
        this.bonusList = bonusList;
    }

    public double getAverageRating() {
        int sum = 0;
        for (CompanyRating cr : companyRatingList) {
            sum += cr.getRating();
        }
        double avarage = 0;
        if (sum != 0) {
            avarage = (double)sum/companyRatingList.size();
        }
        System.out.println(avarage);
        return avarage;
    }

    public double getPledgedSum() {
        double sum = 0;
        for (Bonus bonus : getBonusList()) {
            sum += (double) bonus.getUserList().size() * bonus.getAmount();
        }
        return sum;
    }

    public double getPercent() {
        return (getPledgedSum() / goalAmount * 100);
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }


}
