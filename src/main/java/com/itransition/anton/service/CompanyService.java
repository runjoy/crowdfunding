package com.itransition.anton.service;

import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.CompanyRating;
import com.itransition.anton.domain.User;
import com.itransition.anton.repo.CompanyRepo;
import com.itransition.anton.repo.CompanyRatingRepo;
import com.itransition.anton.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

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
    private UserRepo userRepo;

    public Company addCompany(Long user_id, String name, String description, String video, String text, double goalAmount) {
        Company company = new Company();
        company.setName(name);
        company.setDescription(description);
        company.setText(text);
        company.setGoalAmount(goalAmount);
        company.setVideo(convertVideoURL(video));

        User user = userRepo.findById(user_id).get();
        company.setOwner(user);

        companyRepo.save(company);

        return company;
    }

    public void editCompany(Long company_id, String name, String description, String video, String text, double goalAmount) {
        Company company = companyRepo.findById(company_id).get();
        company.setName(name);
        company.setDescription(description);
        company.setText(text);
        company.setGoalAmount(goalAmount);
        company.setVideo(convertVideoURL(video));

        companyRepo.save(company);
    }

    public Company getCompanyById(Long id) {
        return companyRepo.findById(id).get();
    }

    public Double voteCompany(Long user_id, Long company_id, Integer rating) {
        Company company = companyRepo.findById(company_id).get();
        if (companyRatingRepo.findByUserIdAndCompany(user_id, company).isPresent()) {
            return 0.0;
        }

        CompanyRating cp = new CompanyRating(company, user_id, rating);
        companyRatingRepo.save(cp);
        return company.getAverageRating();
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
}
