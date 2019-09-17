package com.itransition.anton.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;

/**
 * Created by qanto on 15.09.2019.
 */
public class SpringErrorController {
    /*@Controller
    public class MyErrorController implements ErrorController {

        @RequestMapping("/error")
        public String handleError() {
            return "error";
        }

        @Override
        public String getErrorPath() {
            return "/error";
        }
    }*/
}
