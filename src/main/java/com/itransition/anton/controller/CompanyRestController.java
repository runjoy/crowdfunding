package com.itransition.anton.controller;

import com.itransition.anton.domain.CompanyRatingForm;
import com.itransition.anton.domain.User;
import com.itransition.anton.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qanto on 11.09.2019.
 */

@RestController
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;


    @RequestMapping(value = "/vote", //
            method = RequestMethod.POST, //
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("authenticated")
    public Double voteCompany(@RequestBody CompanyRatingForm companyRatingForm) {
        System.out.println("ВАУ " + companyRatingForm);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return companyService.voteCompany(user.getId(), companyRatingForm.getCompanyId(), companyRatingForm.getVote());
    }
}
