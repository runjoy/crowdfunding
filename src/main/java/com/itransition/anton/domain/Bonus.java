package com.itransition.anton.domain;

import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.util.List;

/**
 * Created by qanto on 08.09.2019.
 */

@Entity
public class Bonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field(index = org.hibernate.search.annotations.Index.YES, analyze=Analyze.YES, termVector = TermVector.YES, analyzer = @Analyzer(definition = "edgeNgram"), store = Store.NO)
    private String title;

    @Field(index = Index.YES, analyze=Analyze.YES, termVector = TermVector.YES, analyzer = @Analyzer(definition = "edgeNgram"), store = Store.NO)
    @Column(length=1000)
    private String description;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToMany(mappedBy = "bonusList")
    private List<User> userList;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
