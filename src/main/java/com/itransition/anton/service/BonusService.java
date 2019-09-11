package com.itransition.anton.service;

import com.itransition.anton.domain.Bonus;
import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.User;
import com.itransition.anton.repo.BonusRepo;
import com.itransition.anton.repo.CompanyRepo;
import com.itransition.anton.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Created by qanto on 08.09.2019.
 */

@Service
@PreAuthorize("authenticated")
public class BonusService {

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private BonusRepo bonusRepo;

    @Autowired
    private UserRepo userRepo;

    public Bonus addBonus(Long company_id, String title, String description, Double amount) {
        Bonus bonus = new Bonus();
        bonus.setTitle(title);
        bonus.setDescription(description);
        bonus.setAmount(amount);

        Company company = companyRepo.findById(company_id).get();
        bonus.setCompany(company);

        bonusRepo.save(bonus);

        return bonus;
    }

    public void editBonus(Long newsId, String title, String description, Double amount) {
        Bonus bonus = bonusRepo.findById(newsId).get();
        bonus.setTitle(title);
        bonus.setDescription(description);
        bonus.setAmount(amount);

        bonusRepo.save(bonus);
    }

    public void buyBonus(Long userId, Bonus bonus) {
        User user = userRepo.findById(userId).get();
        user.getBonusList().add(bonus);
        userRepo.save(user);
    }
}
