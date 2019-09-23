package com.itransition.anton.Model;

import com.itransition.anton.domain.Company;
import com.itransition.anton.domain.User;

/**
 * Created by qanto on 22.09.2019.
 */
public class ChatComment {
    private String text;
    private Long companyId;
    private Long userId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ChatComment{" +
                "text='" + text + '\'' +
                ", companyId=" + companyId +
                ", userId=" + userId +
                '}';
    }
}
