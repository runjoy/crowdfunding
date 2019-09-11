package com.itransition.anton.controller;

import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.User;
import com.itransition.anton.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by qanto on 09.09.2019.
 */

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    public CommentService commentService;

    @PostMapping("add")
    public String addComment(@AuthenticationPrincipal User currentUser, @RequestParam Company company, @RequestParam String text, Model model) {
        commentService.addComment(currentUser, company, text);
        return "redirect:/company/" + company.getId();
    }
}
