package com.itransition.anton.service;

import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.News;
import com.itransition.anton.domain.User;
import com.itransition.anton.repo.CompanyRepo;
import com.itransition.anton.repo.NewsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qanto on 08.09.2019.
 */

@Service
public class NewsService {

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private NewsRepo newsRepo;

    public News addNews(Long company_id, String title, String text) {
        News news = new News();
        news.setTitle(title);
        news.setText(text);

        Company company = companyRepo.findById(company_id).get();
        news.setCompany(company);

        newsRepo.save(news);

        return news;
    }

    public void editNews(Long news_id, String title, String text) {
        News news = newsRepo.findById(news_id).get();
        news.setTitle(title);
        news.setText(text);

        newsRepo.save(news);
    }

    public void deleteBonus(News news) {
        News DBnews = newsRepo.findById(news.getId()).get();
        newsRepo.delete(DBnews);
    }
}
