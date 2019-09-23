package com.itransition.anton.controller;

import com.itransition.anton.domain.Tag;
import com.itransition.anton.domain.User;
import com.itransition.anton.repo.TagRepo;
import com.itransition.anton.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by qanto on 01.09.2019.
 */
@Controller
public class MainController {

    @Autowired
    TagRepo tagRepo;

    @Autowired
    MessageSource messageSource;

    @Autowired
    CompanyService companyService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("lastCompany", companyService.getLastCompany());
        model.addAttribute("topCompany", companyService.getTopCompany());
        return "index";
    }
}
