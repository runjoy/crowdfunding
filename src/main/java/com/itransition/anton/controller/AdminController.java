package com.itransition.anton.controller;

import com.itransition.anton.domain.User;
import com.itransition.anton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by qanto on 01.09.2019.
 */
@Controller
@RequestMapping("/panel")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String userList(Model model) {

        model.addAttribute("users", userService.findAll());
        //userService.updateLoginDate(user);
        return "adminpanel";
    }

    @PostMapping(params={"lock"})
    public String lockUsers(@RequestParam(defaultValue = "") List<Long> userList, Model model) {
        userService.lockUsers(true, userList);
        return "redirect:/panel";
    }

    @PostMapping(params={"unlock"})
    public String unlockUsers(@RequestParam(defaultValue = "") List<Long> userList, Model model) {
        userService.lockUsers(false, userList);
        return "redirect:/panel";
    }

    @PostMapping(params={"delete"})
    public String deleteUsers(@RequestParam(defaultValue = "") List<Long> userList, Model model) {
        userService.deleteUsers(userList);
        return "redirect:/panel";
    }

    @PostMapping(params={"add_admin"})
    public String addAdmin(@RequestParam(defaultValue = "") List<Long> userList, Model model) {
        userService.adminUsers(true, userList);
        return "redirect:/panel";
    }

    @PostMapping(params={"delete_admin"})
    public String deleteAdmin(@RequestParam(defaultValue = "") List<Long> userList, Model model) {
        userService.adminUsers(false, userList);
        return "redirect:/panel";
    }
}
