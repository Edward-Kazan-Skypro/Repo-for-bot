package skypro.learn.tg_botfromvideo.bot.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import skypro.learn.tg_botfromvideo.model.QuestionFromUser;
import skypro.learn.tg_botfromvideo.model.Report;
import skypro.learn.tg_botfromvideo.repository.ReportsRepository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class ReportUtil {

    final private ReportsRepository reportsRepository;

    public ReportUtil(ReportsRepository reportsRepository) {
        this.reportsRepository = reportsRepository;
    }

    public String getAllReports() {
        List<Report> listOfReports;
        StringBuilder resultListForView = new StringBuilder();
        listOfReports = (List<Report>) reportsRepository.findAll();
        for (Report report : listOfReports) {
            resultListForView.append(report);
        }
        return resultListForView.toString();
    }
}






