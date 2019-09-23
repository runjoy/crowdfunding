package com.itransition.anton.controller;

import com.itransition.anton.Model.ChatComment;
import com.itransition.anton.domain.Comment;
import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.User;
import com.itransition.anton.repo.CommentRepo;
import com.itransition.anton.repo.CompanyRepo;
import com.itransition.anton.service.CommentService;
import com.itransition.anton.service.CompanyService;
import com.itransition.anton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by qanto on 22.09.2019.
 */

@Controller
public class SocketController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyService companyRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserService userService;

    @Transactional
    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public Comment addMessage(@Payload ChatComment chatComment) {
        System.out.println(chatComment);
        User user = userService.getUserById(chatComment.getUserId());
        Company company = companyService.getCompanyById(chatComment.getUserId());
        return commentService.addComment(user, company, chatComment.getText());
    }
}
