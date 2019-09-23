package com.itransition.anton.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;

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
@Indexed
@AnalyzerDef(name = "edgeNgram",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class)
        })

public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field(index = Index.YES, termVector = TermVector.YES, analyze=Analyze.YES, analyzer = @Analyzer(definition = "edgeNgram"), store = Store.NO)
    private String name;

    @Field(index = Index.YES, termVector = TermVector.YES, analyze=Analyze.YES, analyzer = @Analyzer(definition = "edgeNgram"), store = Store.NO)
    @Column(length=500)
    private String description;

    private double goalAmount;

    private String video;

    @Column(name = "topic")
    @Enumerated(EnumType.STRING)
    private Topic topic;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finalDate;

    @Field(index = Index.YES, analyze=Analyze.YES, termVector = TermVector.YES, analyzer = @Analyzer(definition = "edgeNgram"), store = Store.NO)
    @Column(length=10000)
    private String text;

    @IndexedEmbedded
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name="COMPANY_TAGS",
            joinColumns = @JoinColumn(name="COMPANY_ID"), inverseJoinColumns = @JoinColumn(name="TAG_ID"))
    private List<Tag> tags;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @IndexedEmbedded
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CompanyImage> companyImageList;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CompanyRating> companyRatingList;

    private Double rating;

    @IndexedEmbedded
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<News> newsList;

    @IndexedEmbedded
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

    /*public double getAverageRating() {
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
    }*/

    public int getIntAverageRating() {
        return (int) rating.doubleValue();
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<CompanyImage> getCompanyImageList() {
        return companyImageList;
    }

    public void setCompanyImageList(List<CompanyImage> companyImageList) {
        this.companyImageList = companyImageList;
    }
}
