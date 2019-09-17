package com.itransition.anton.domain;

/**
 * Created by qanto on 05.09.2019.
 */
public enum Topic {
    BUSINESS, DESIGN, GAMES, ART, MUSIC, EDUCATION, TECH;

    public static String getTopics() {
        return "'" + BUSINESS + "','" + DESIGN + "','" + GAMES + "','" + ART + "','" + MUSIC + "','" + EDUCATION + "','" + TECH + "'";
    }
}