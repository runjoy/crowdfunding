package com.itransition.anton.controller;

import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.Role;
import com.itransition.anton.domain.Topic;
import com.itransition.anton.domain.User;
import com.itransition.anton.repo.TagRepo;
import com.itransition.anton.service.CompanyService;
import com.itransition.anton.service.UserService;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Created by qanto on 02.09.2019.
 */
@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagRepo tagRepo;

    @PreAuthorize("authenticated")
    @GetMapping
    public String Company(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.getUserById(user.getId()));
        return "companyList";
    }

    @GetMapping("/{company}")
    public String showCompany(@PathVariable Company company, Model model) {
        Object objUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean show = false;

        if (objUser instanceof User) {
            show = userService.getShowRole((User)objUser, company.getOwner());
        }


        model.addAttribute("show", show);
        model.addAttribute("company", company);
        model.addAttribute("current_user", objUser);
        return "company";
    }

    @PreAuthorize("authenticated")
    @GetMapping("/add")
    public String editCompany(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.getUserById(user.getId()));
        model.addAttribute("topices", Topic.values());
        model.addAttribute("tags", tagRepo.findAll());
        return "addCompany";
    }

    @PreAuthorize("authenticated")
    @GetMapping("/edit/{company_id}") //заменить тип pathvariable company
    public String addCompany(@PathVariable Long company_id, @AuthenticationPrincipal User user, Model model) {
        Company company = companyService.getCompanyById(company_id);
        if((user.getRoles().contains(Role.ADMIN)) || (user.getId() == company.getOwner().getId())) {
            model.addAttribute("user", userService.getUserById(user.getId()));
            model.addAttribute("add", false);
            model.addAttribute("company", company);
            model.addAttribute("topices", Topic.getTopics());
            return "editorCompany";
        }
        return "redirect:/company/{company_id}";
    }

    @PreAuthorize("authenticated")
    @PostMapping("/change")
    public String changeData(@RequestParam Long user_id,
                             @RequestParam(defaultValue = "") String name,
                             @RequestParam(defaultValue = "") String description,
                             @RequestParam(defaultValue = "") String text,
                             @RequestParam(defaultValue = "") String video,
                             @RequestParam(defaultValue = "") Double goalAmount,
                             @RequestParam(defaultValue = "")
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date finalDate,
                             @RequestParam(defaultValue = "") Topic topic,
                             @RequestParam Map<String, String> form,
                             @RequestParam Long company_id,
                             @RequestParam String[] tags,
                             Model model) {
        System.out.println(Arrays.deepToString(tags));
        if (company_id == 0) {
            Company company = companyService.addCompany(user_id, name, description, video, text, goalAmount, topic, finalDate, tags);
            return "redirect:/company/" + company.getId();
        } else {
            companyService.editCompany(company_id, name, description, video, text,  goalAmount, topic, finalDate);
            return "redirect:/company/" + company_id;
        }

    }

    @GetMapping("/search")
    public String searchCompany(@RequestParam(required = false, defaultValue = "") String search, Model model) {
        companyService.searchCompany(search);
        return "redirect:/";
    }

}
