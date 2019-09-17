package com.itransition.anton.service;

import com.itransition.anton.domain.*;
import com.itransition.anton.repo.CompanyRepo;
import com.itransition.anton.repo.CompanyRatingRepo;
import com.itransition.anton.repo.TagRepo;
import com.itransition.anton.repo.UserRepo;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by qanto on 05.09.2019.
 */

@Service
public class CompanyService {

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private CompanyRatingRepo companyRatingRepo;

    @Autowired
    private TagRepo tagRepo;

    @Autowired
    private UserRepo userRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public Company addCompany(Long user_id, String name, String description, String video, String text, double goalAmount, Topic topic, Date finalDate, String[] tagsText) {
        Company company = new Company();
        company.setName(name);
        company.setDescription(description);
        company.setText(text);
        company.setGoalAmount(goalAmount);
        company.setVideo(convertVideoURL(video));
        company.setRating(0.0);
        company.setFinalDate(finalDate);
        company.setTopic(topic);
        List<Tag> tags = new ArrayList<>();
        for(String tagText : tagsText) {
            Tag tag;
            Optional<Tag> optionalTag = tagRepo.findByName(tagText);
            if (!optionalTag.isPresent()) {
                tag = new Tag();
                tag.setName(tagText);
            } else {
                tag = optionalTag.get();
            }
            tags.add(tag);
        }
        company.setTags(tags);
        User user = userRepo.findById(user_id).get();
        company.setOwner(user);

        if (finalDate.after(new Date())) {
            companyRepo.save(company);
        }
        return company;
    }

    public void editCompany(Long company_id, String name, String description, String video, String text, double goalAmount, Topic topic, Date finalDate) {
        Company company = companyRepo.findById(company_id).get();
        company.setName(name);
        company.setDescription(description);
        company.setText(text);
        company.setGoalAmount(goalAmount);
        company.setVideo(convertVideoURL(video));
        company.setTopic(topic);
        company.setFinalDate(finalDate);

        if (finalDate.after(new Date())) {
            companyRepo.save(company);
        }
    }

    public Company getCompanyById(Long id) {
        return companyRepo.findById(id).get();
    }

    public double voteCompany(Long user_id, Long company_id, Integer rating) {
        double averageRating = 0;
        Company company = companyRepo.findById(company_id).get();
        if (!companyRatingRepo.findByUserIdAndCompany(user_id, company).isPresent()) {
            CompanyRating cp = new CompanyRating(company, user_id, rating);
            companyRatingRepo.save(cp);
            averageRating = countRating(company.getCompanyRatingList());
            company.setRating(averageRating);
            companyRepo.save(company);
        }
        return averageRating;
    }

    public String convertVideoURL(String convertURL) {
        String result;
        URL url = null;
        try {
            url = new URL(convertURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String[] mass;
        String[] subMass;
        HashMap<String, String> hm = new HashMap<>();
        String s = url.getQuery();
        mass = s.split("&");
        for (String t : mass) {
            subMass = t.split("=");
            hm.put(subMass[0], subMass[1]);
        }
        return hm.get("v");
    }


    public List<Company> getLastCompany() {
        return (List<Company>) companyRepo.findAll();
    }

    public List<Company> getTopCompany() {
        return companyRepo.findAllByOrderByRatingDesc();
    }

    public double countRating(List<CompanyRating> companyRatingList) {
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



    public void searchCompany(String searchText) { //ПОЛНОТЕКСТОВЫЙ ПОИСК
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Company.class)
                .get();

        org.apache.lucene.search.Query luceneQuery = queryBuilder
                .keyword()
                .onFields("name", "description", "text")
                .matching(searchText)
                .createQuery();

        javax.persistence.Query jpaQuery
                = fullTextEntityManager.createFullTextQuery(luceneQuery, Company.class);

        List<Company> results = jpaQuery.getResultList();

        for (Company result : results) {
            System.out.println(result.getId());
        }
        entityManager.close();
    }


}
