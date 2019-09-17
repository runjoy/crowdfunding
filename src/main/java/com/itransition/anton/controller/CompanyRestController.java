package com.itransition.anton.controller;

import com.itransition.anton.domain.*;
import com.itransition.anton.repo.TagRepo;
import com.itransition.anton.service.CompanyService;
import com.itransition.anton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by qanto on 11.09.2019.
 */

@RestController
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagRepo tagRepo;

    @RequestMapping(value = "/vote",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("authenticated")
    public double voteCompany(@RequestBody CompanyRatingForm companyRatingForm) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return companyService.voteCompany(user.getId(), companyRatingForm.getCompanyId(), companyRatingForm.getVote());
    }

    @RequestMapping(value = "/company_edit",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("authenticated")
    public boolean editCompany(@RequestBody Company companyRest) {
        companyService.editCompany(companyRest.getId(), companyRest.getName(),
                companyRest.getDescription(), companyRest.getVideo(), companyRest.getText(), companyRest.getGoalAmount(), companyRest.getTopic(), companyRest.getFinalDate());
        return true;
    }

    @RequestMapping(value = "/tag_search",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("authenticated")
    public List<Tag> searchTags(@RequestBody String search) {
        return tagRepo.findByNameStartingWith(search);
    }
}
