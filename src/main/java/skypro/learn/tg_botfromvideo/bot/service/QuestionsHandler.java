package skypro.learn.tg_botfromvideo.bot.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import skypro.learn.tg_botfromvideo.model.QuestionFromUser;
import skypro.learn.tg_botfromvideo.repository.QuestionsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class QuestionsHandler {
    final private QuestionsRepository questionsRepository;

    public QuestionsHandler(QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
    }

    public String getAllActiveQuestions(){
        List <QuestionFromUser> listOfQuestions = new ArrayList<>();
        String resultListForView = "";
        listOfQuestions = (List<QuestionFromUser>) questionsRepository.findAll();
        for (QuestionFromUser question: listOfQuestions) {
            if (question.isActive()){
                resultListForView = resultListForView + question;
            }
        }
        return resultListForView;
    }
}
