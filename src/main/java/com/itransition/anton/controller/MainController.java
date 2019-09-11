package com.itransition.anton.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by qanto on 01.09.2019.
 */
@Controller
public class MainController {


    @Autowired
    MessageSource messageSource;

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
