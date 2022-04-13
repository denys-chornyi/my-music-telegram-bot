package com.telegram.bot.shazam.api.interfaces;

import com.telegram.bot.shazam.api.SearchModel;

public interface Search {

    String getJSON();

    SearchModel getNames();

    void downloadImage();

    void downloadAudio();

    String setRequestLine();

}
