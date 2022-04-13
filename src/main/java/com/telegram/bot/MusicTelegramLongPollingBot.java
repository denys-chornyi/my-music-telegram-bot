package com.telegram.bot;

import com.telegram.bot.shazam.api.SearchImpl;
import com.telegram.bot.shazam.api.SearchModel;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.File;

public class MusicTelegramLongPollingBot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new MusicTelegramLongPollingBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        SearchImpl search = new SearchImpl(message.getText());
        SearchModel searchModel = search.getNames();
        for (String string : searchModel.getFullTitles()) {
            System.out.println(string);
        }
        search.downloadImage();
        search.downloadAudio();
        long chatId = message.getChatId();
        SendMessage sendMessage = new SendMessage();
        SendPhoto sendImage = new SendPhoto();
        SendAudio sendAudio = new SendAudio();
        sendMessage.setChatId(chatId);
        sendImage.setChatId(chatId);
        sendAudio.setChatId(chatId);
        for (int i = 0; i < searchModel.getFullTitles().length; i++) {
            sendMessage.setText(searchModel.getFullTitles()[i]);
            sendImage.setPhoto(new File("/Users/chornyi/DenysProjects/my-music-telegram-bot/src/main/resources/images/" + searchModel.getFullTitles()[i] + ".jpg"));
            sendAudio.setAudio(new File("/Users/chornyi/DenysProjects/my-music-telegram-bot/src/main/resources/audio/" + searchModel.getFullTitles()[i] + ".mp3"));
            try {
                execute(sendMessage);
                execute(sendImage);
                execute(sendAudio);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "my_music_telegram_bot";
    }

    @Override
    public String getBotToken() {
        return "1334196408:AAEv272nyLjUp7THlvvZkTR6EPKsV5hKy5g";
    }
}
