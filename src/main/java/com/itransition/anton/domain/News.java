package com.itransition.anton.domain;

import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by qanto on 08.09.2019.
 */
@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field(index = org.hibernate.search.annotations.Index.YES, analyze=Analyze.YES, termVector = TermVector.YES, analyzer = @Analyzer(definition = "edgeNgram"), store = Store.NO)
    private String title;

    @Field(index = org.hibernate.search.annotations.Index.YES, analyze= Analyze.YES, termVector = TermVector.YES, analyzer = @Analyzer(definition = "edgeNgram"), store = Store.NO)
    @Column(length=10000)
    private String text;

    private String image;

    @Temporal(TemporalType.TIMESTAMP)
    private Date addDate;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }
}