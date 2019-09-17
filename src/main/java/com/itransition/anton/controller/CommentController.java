package com.itransition.anton.controller;

import com.itransition.anton.domain.Comment;
import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.User;
import com.itransition.anton.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PreAuthorize("authenticated")
    @PostMapping("add")
    public String addComment(@RequestParam Company company, @RequestParam String text) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentService.addComment(currentUser, company, text);
        return "redirect:/company/" + company.getId();
    }

    @PreAuthorize("authenticated")
    @PostMapping("vote")
    public String voteComment(@RequestParam Comment comment, @RequestParam Boolean vote) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentService.addLike(comment, currentUser, vote);
        return "redirect:/company/" + comment.getCompany().getId();
    }
}
