package com.telegram.bot.shazam.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.telegram.bot.shazam.api.interfaces.Search;
import org.json.JSONArray;
import org.json.JSONObject;



import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class SearchImpl implements Search {

    private String songName;
    private String requestLine;
    private SearchModel searchModel;

    public SearchImpl(String songName) {
        this.songName = songName;
        JSONArray hitsJSONArray = getAllHits();
        this.searchModel = new SearchModel(hitsJSONArray.length());
    }

    @Override
    public String getJSON() {
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(setRequestLine())
                    .header("x-rapidapi-host", "shazam.p.rapidapi.com")
                    .header("x-rapidapi-key", "43f430cbe9msh9e4475f1af070bap10be3ajsn59d526e3deab")
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response.getBody();
    }

    private JSONArray getAllHits() {
        JSONObject jsonObject = new JSONObject(getJSON());
        JSONObject tracksJSONObject = jsonObject.getJSONObject("tracks");
        JSONArray hitsJSONArray = tracksJSONObject.getJSONArray("hits");
        return hitsJSONArray;
    }

    @Override
    public SearchModel getNames() {
        JSONArray hitsJSONArray = getAllHits();
        for (int i = 0; i < hitsJSONArray.length(); i++) {
            JSONObject curJSONObject = hitsJSONArray.getJSONObject(i);
            JSONObject trackJSONObject = curJSONObject.getJSONObject("track");
            String subtitle = trackJSONObject.getString("subtitle");
            String title = trackJSONObject.getString("title");
            searchModel.setFullTitles(subtitle, title, i);
        }
        return searchModel;
    }

    @Override
    public void downloadImage() {
        JSONArray hitsJSONArray = getAllHits();
        for (int i = 0; i < hitsJSONArray.length(); i++) {
            JSONObject curJSONObject = hitsJSONArray.getJSONObject(i);
            JSONObject trackJSONObject = curJSONObject.getJSONObject("track");
            JSONObject imagesJSONObject = trackJSONObject.getJSONObject("images");
            try {
                BufferedImage image = ImageIO.read(new URL(imagesJSONObject.getString("coverart")));
                ImageIO.write(image, "jpg", new File("/Users/chornyi/DenysProjects/my-music-telegram-bot/src/main/resources/images/" + searchModel.getFullTitles()[i] + ".jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void downloadAudio() {
        JSONArray hitsJSONArray = getAllHits();
        for (int i = 0; i < hitsJSONArray.length(); i++) {
            JSONObject curJSONObject = hitsJSONArray.getJSONObject(i);
            JSONObject trackJSONObject = curJSONObject.getJSONObject("track");
            JSONObject hubJSONObject = trackJSONObject.getJSONObject("hub");
            JSONArray actionsJSONArray = hubJSONObject.getJSONArray("actions");
            JSONObject actionsJSONObject = actionsJSONArray.getJSONObject(1);
            try {
                URLConnection conn = new URL(actionsJSONObject.getString("uri")).openConnection();
                InputStream inputStream = conn.getInputStream();
                OutputStream outputStream = new FileOutputStream(new File("/Users/chornyi/DenysProjects/my-music-telegram-bot/src/main/resources/audio/" + searchModel.getFullTitles()[i] + ".mp3"));
                int curByte;
                while ((curByte = inputStream.read()) != -1) {
                    outputStream.write(curByte);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String setRequestLine() {
        String curWord = "";
        requestLine = "https://rapidapi.p.rapidapi.com/search?term=";
        for (int j = 0; j < songName.length(); j++) {
            if (songName.charAt(j) == ' ') {
                requestLine += curWord + "%20";
                curWord = "";
                continue;              
            }
            if (j == songName.length() - 1) {
                curWord += songName.charAt(j);
                requestLine += curWord + "&locale=en-US&offset=0&limit=5";
                break;
            }
            curWord += songName.charAt(j);
        }
        return requestLine;
    }

}
