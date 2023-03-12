package skypro.learn.tg_bot.bot.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import skypro.learn.tg_bot.model.QuestionFromUser;
import skypro.learn.tg_bot.repository.QuestionsRepository;

import java.time.LocalDate;
import java.util.List;


/**
 * Класс для обработки вопросов пользователей
 */

@Slf4j
@Component
public class QuestionsUtil {
    final private QuestionsRepository questionsRepository;

    public QuestionsUtil(QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
    }

    public String getAllActiveQuestions(){
        List <QuestionFromUser> listOfQuestions;
        StringBuilder resultListForView = new StringBuilder();
        listOfQuestions = (List<QuestionFromUser>) questionsRepository.findAll();
        for (QuestionFromUser question: listOfQuestions) {
            if (question.isActive()){
                resultListForView.append(question);
            }
        }
        if (resultListForView.isEmpty()){
            resultListForView.append("Список вопросов пуст");
        }
        return resultListForView.toString();
    }

    public boolean addQuestion(Update update) {
        boolean questionIsAdded = false;
        //Получаем строку из сообщения
        String text = update.getMessage().getText();
        Long questionerChatId = update.getMessage().getChatId();
        //Уберем из строки уже ненужные символы
        text = text.replaceAll("вопрос:", "");
        QuestionFromUser question = new QuestionFromUser();
        question.setChatId(update.getMessage().getChatId());
        question.setQuestioner(update.getMessage().getChat().getFirstName());
        question.setQuestion(text);
        question.setQuestionDate(LocalDate.now());
        question.setQuestionerChatId(questionerChatId);
        questionsRepository.save(question);
        Long idSavedQuestion = question.getId();
        if (questionsRepository.existsById(idSavedQuestion)){
            questionIsAdded = true;
        }
        if (questionIsAdded) {
            log.info("Вопрос сохранен успешно!");
        } else {
            log.error("Вопрос не сохранен...");
        }
        return questionIsAdded;
    }
}
