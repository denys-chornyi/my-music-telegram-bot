package com.telegram.bot.shazam.api;

public class SearchModel {

    private String subtitle;
    private String title;
    private String[] fullTitles;

    public SearchModel( int length) {
        this.fullTitles = new String[length];
    }

    public String[] getFullTitles() {
        return fullTitles;
    }

    public void setFullTitles(String subtitle, String title, int index) {
        this.fullTitles[index] = subtitle + " - " + title;
    }
}
