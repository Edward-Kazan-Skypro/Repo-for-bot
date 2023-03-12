package skypro.learn.tg_bot.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import skypro.learn.tg_bot.repository.UsersRepository;
import java.io.*;

@Slf4j
@Component
public class CommandUtil {

    private String pathToFiles = "src/main/resources/txt_files_for_menu";

    final private QuestionsUtil questionsUtil;

    final private AdminsUtil adminsUtil;

    final private UserUtil userUtil;

    final private ReportUtil reportUtil;

    final private UsersRepository usersRepository;

    final private BotMenuSelector botMenuSelector;

    public CommandUtil(QuestionsUtil questionsUtil,
                       AdminsUtil adminsUtil,
                       UserUtil userUtil,
                       ReportUtil reportUtil, UsersRepository usersRepository,
                       BotMenuSelector botMenuSelector) {
        this.questionsUtil = questionsUtil;
        this.adminsUtil = adminsUtil;
        this.userUtil = userUtil;
        this.reportUtil = reportUtil;
        this.usersRepository = usersRepository;
        this.botMenuSelector = botMenuSelector;
    }

    /**
     * Метод для обработки сообщения от пользователя телеграмм-бота.
     * В качестве аргумента принимает текст.     *
     * @param update
     * @return
     */

    public String selectBotCommand(Update update) {
        String inputText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        //Отдельная обработка кодового слова для входа для администратора
        if (inputText.equals("/кодовое_слово")){
            adminsUtil.addAdmin(update);
            return readTextFromFile("/admins_command");
        }

        //Здесь сделаем обработку "админских" команд
        //На данный момент список возможных пунктов меню следующий:
        // /view_questions - посмотреть вопросы пользователей
        // /add_pet_to_user - добавить питомца к профилю пользователя
        // /view_actual_reports - посмотреть свежие/актуальные отчеты
        // /view_all_reports - посмотреть все отчеты
        //Обработка команды /view_questions - просмотр всех активных (актуальных) вопросов
        if (inputText.equals("/view_questions")){
            return questionsUtil.getAllActiveQuestions();
        }
        //Обработка команды /view_actual_reports - просмотр текущих отчетов
        if (inputText.equals("/view_actual_reports")){
            return reportUtil.viewCurrentReports();
        }

        //Обработка команды /view_all_reports - посмотреть все отчеты
        if (inputText.equals("/view_all_reports")){
            return reportUtil.viewAllReports();
        }

        //Когда пользователь выбирает приют, то сохраняем в БД под его chatId этот выбор
        if (inputText.equals("/dog_shelter") || inputText.equals("/cat_shelter")){
            userUtil.addShelterToUser(update);
        }

        //Обычная обработка команд
        return readTextFromFile(inputText);
    }

    /**
     * Метод для чтения текстовых данных из файла.
     *
     * @param fileName
     * @return
     */
    private String readTextFromFile(String fileName) {
        StringBuilder content = new StringBuilder();
        String line;
        String pathWithName = pathToFiles + fileName + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(pathWithName))) {
            while ((line = br.readLine()) != null) content.append(line).append("\n");
        } catch (IOException e) {
            log.error("Ошибка чтения файла! " + e.getMessage());
        }
        return new String(content);
    }
}