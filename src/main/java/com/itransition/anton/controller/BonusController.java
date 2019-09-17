package com.itransition.anton.controller;

import com.itransition.anton.domain.Bonus;
import com.itransition.anton.domain.News;
import com.itransition.anton.domain.Role;
import com.itransition.anton.domain.User;
import com.itransition.anton.repo.BonusRepo;
import com.itransition.anton.service.BonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by qanto on 08.09.2019.
 */
@Controller
@RequestMapping("/bonus")
public class BonusController {

    @Autowired
    private BonusService bonusService;

    @GetMapping("add/{company_id}")
    public String addBonus(@PathVariable Long company_id, Model model) {
        model.addAttribute("company_id", company_id);
        model.addAttribute("add", true);
        return "editorBonus";
    }

    @GetMapping("edit/{bonus}")
    public String editBonus(@PathVariable Bonus bonus, @AuthenticationPrincipal User user, Model model) {

        if((user.getRoles().contains(Role.ADMIN)) || (user.getId() == bonus.getCompany().getOwner().getId())) {
            model.addAttribute("company_id", bonus.getCompany().getId());
            model.addAttribute("add", false);
            model.addAttribute("news", bonus);

            return "editorBonus";
        }
        return "redirect:/company/{company_id}";
    }

    @PostMapping("change")
    public String changeBonus(@RequestParam Long company_id,
                             @RequestParam(defaultValue = "") String title,
                             @RequestParam(defaultValue = "") String description,
                             @RequestParam(defaultValue = "") Double amount,
                             @RequestParam Long bonus_id,
                             Model model) {
        if (bonus_id == 0) {
            Bonus bonus = bonusService.addBonus(company_id, title, description, amount);
        } else {
            bonusService.editBonus(bonus_id, title, description, amount);
        }
        return "redirect:/company/" + company_id;
    }

    @PostMapping("confirmation")
    public String confirmationBonus(@RequestParam Bonus bonus, Model model) {
        model.addAttribute("bonus", bonus);
        return "paymentConfirmation";
    }

    @PostMapping("buy")
    public String buy(@AuthenticationPrincipal User currentUser, @RequestParam Bonus bonus) {
        bonusService.buyBonus(currentUser.getId(), bonus);
        return "redirect:/company/" + bonus.getCompany().getId();
    }

    @PostMapping("delete")
    public String delete(@RequestParam Bonus bonus) {
        bonusService.deleteBonus(bonus);
        return "redirect:/company/" + bonus.getCompany().getId();
    }
}
