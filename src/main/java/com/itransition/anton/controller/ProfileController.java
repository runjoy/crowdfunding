package com.itransition.anton.controller;

import com.itransition.anton.domain.Role;
import com.itransition.anton.domain.User;
import com.itransition.anton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by qanto on 03.09.2019.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @PreAuthorize("authenticated")
    @GetMapping
    public String getProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("auth_id", user.getId());
        model.addAttribute("user", userService.getUserById(user.getId()));
        return "profile";
    }

    @GetMapping("{user}")
    public String getProfileUser(@PathVariable User user, Model model) {
        Object objUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean show = false;

        if (objUser instanceof User) {
            show = userService.getShowRole((User)objUser, user);
        }

        model.addAttribute("show", show);
        model.addAttribute("user", user);
        return "profile";
    }

    @PreAuthorize("authenticated")
    @GetMapping("/edit")
    public String getEditProfile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", userService.getUserById(user.getId()));
        return "editorprofile";
    }

    @PreAuthorize("authenticated")
    @GetMapping("/edit/{user}")
    public String getUserEditProfile(@AuthenticationPrincipal User authenticationUser, @PathVariable User user, Model model) {
        if((authenticationUser.getRoles().contains(Role.ADMIN)) || (authenticationUser.getId() == user.getId())) {
            model.addAttribute("user", user);
            return "editorprofile";
        }
        return "redirect:/profile" + user.getId();
    }

    @PreAuthorize("authenticated")
    @PostMapping("/change")
    public String editProfile(@RequestParam Long id,
                              @RequestParam(name="firstname", defaultValue = "") String firstname,
                              @RequestParam(name="lastname", defaultValue = "") String lastname,
                              @RequestParam(name="city", defaultValue = "") String city,
                              Model model) {
        userService.editProfile(id, firstname, lastname, city);
        return "redirect:/profile/" + id;
    }

    @PreAuthorize("authenticated")
    @PostMapping("/changepassword")
    public String changePassword(@RequestParam Long id,
                                 @RequestParam String password,
                                 @RequestParam String repeatpassword,
                                 Model model) {
        userService.changePassword(id, password, repeatpassword);
        return "redirect:/profile/edit/" + id;
    }

    @PreAuthorize("authenticated")
    @PostMapping("/image")
    public String changeImage(@RequestParam Long id, @RequestParam("file") MultipartFile file) {
        userService.loadImage(id, file);
        return "redirect:/profile/edit/" + id;
    }
}
