package com.telegram.bot.shazam.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class TestRequest {

    public void testRequest1() {
        BufferedImage image;
        try {
            image = ImageIO.read(new URL("http://google.com.jpg"));
            if (image != null) {
                ImageIO.write(image, "jpg", new File("/Users/chornyi/DenysProjects/my-music-telegram-bot/src/main/java/com/telegram/bot/image.jpg"));
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testRequest() {
        try {
            HttpResponse<String> response = Unirest.get("https://rapidapi.p.rapidapi.com/search?term=Kiss%20the%20rain&locale=en-US&offset=0&limit=5")
                    .header("x-rapidapi-host", "shazam.p.rapidapi.com")
                    .header("x-rapidapi-key", "43f430cbe9msh9e4475f1af070bap10be3ajsn59d526e3deab")
                    .asString();
            System.out.println(response.getBody());
            BufferedImage image = ImageIO.read(new URL("https://images.shazam.com/coverart/t40099833-b1437481268_s400.jpg"));
            ImageIO.write(image, "jpg", new File("/Users/chornyi/DenysProjects/my-music-telegram-bot/src/main/java/com/telegram/bot/picture.jpg"));
            URLConnection conn = new URL("https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview128/v4/90/9d/16/909d1609-ef2e-1d6a-2c7e-ccc0b0e2260c/mzaf_3861364776364190429.plus.aac.p.m4a").openConnection();
            InputStream inputStream = conn.getInputStream();
            OutputStream outputStream = new FileOutputStream(new File("/Users/chornyi/DenysProjects/my-music-telegram-bot/src/main/java/com/telegram/bot/music.mp3"));
            int cur;
            while ((cur = inputStream.read()) != -1) {
                outputStream.write(cur);
            }
        } catch (UnirestException | IOException e) {
            e.printStackTrace();
        }
    }


}
