package com.itransition.anton.controller;

import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.News;
import com.itransition.anton.domain.Role;
import com.itransition.anton.domain.User;
import com.itransition.anton.service.NewsService;
import com.itransition.anton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qanto on 08.09.2019.
 */

@Controller
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @GetMapping("{news}")
    public String editNews(@AuthenticationPrincipal User user, @PathVariable News news, Model model) {
        Object objUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Boolean show = false;

        if (objUser instanceof User) {
            show = userService.getShowRole((User)objUser, news.getCompany().getOwner());
        }


        model.addAttribute("show", show);
        model.addAttribute("auth_id", user.getId());
        model.addAttribute("news", news);
        return "news";
    }

    /*@PostMapping("add")
    public String addNews(@RequestParam Long company_id, Model model) {
        model.addAttribute("company_id", company_id);
        model.addAttribute("add", true);
        return "editorNews";
    }*/

    @GetMapping("add/{company_id}")
    public String addNews(@PathVariable Long company_id, Model model) {
        model.addAttribute("company_id", company_id);
        model.addAttribute("add", true);
        return "editorNews";
    }

    @GetMapping("edit/{news}")
    public String editNews(@PathVariable News news, @AuthenticationPrincipal User user, Model model) {

        if((user.getRoles().contains(Role.ADMIN)) || (user.getId() == news.getCompany().getOwner().getId())) {
            model.addAttribute("company_id", news.getCompany().getId());
            model.addAttribute("add", false);
            model.addAttribute("news", news);

            return "editorNews";
        }
        return "redirect:/news/{news}";
    }

    @PostMapping("change")
    public String changeNews(@RequestParam Long company_id,
                             @RequestParam(defaultValue = "") String title,
                             @RequestParam(defaultValue = "") String text,
                             @RequestParam Long news_id,
                             Model model) {
        if (news_id == 0) {
            News news = newsService.addNews(company_id, title, text);
            return "redirect:/news/" + news.getId();
        } else {
            newsService.editNews(news_id, title, text);
            return "redirect:/news/" + news_id;
        }
    }

    @PostMapping("delete")
    public String delete(@RequestParam News news) {
        newsService.deleteBonus(news);
        return "redirect:/company/" + news.getCompany().getId();
    }

}
