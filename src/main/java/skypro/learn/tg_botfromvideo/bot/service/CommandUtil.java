package skypro.learn.tg_botfromvideo.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import skypro.learn.tg_botfromvideo.repository.UsersRepository;
import java.io.*;

@Slf4j
@Component
public class CommandUtil {

    private String pathToFiles = "src/main/resources/txt_files_for_menu";

    final private QuestionsUtil questionsUtil;

    final private AdminsUtil adminsUtil;

    final private UserUtil userUtil;

    final private UsersRepository usersRepository;

    final private BotMenuSelector botMenuSelector;

    public CommandUtil(QuestionsUtil questionsUtil,
                       AdminsUtil adminsUtil,
                       UserUtil userUtil,
                       UsersRepository usersRepository,
                       BotMenuSelector botMenuSelector) {
        this.questionsUtil = questionsUtil;
        this.adminsUtil = adminsUtil;
        this.userUtil = userUtil;
        this.usersRepository = usersRepository;
        this.botMenuSelector = botMenuSelector;
    }

    /**
     * Метод для обработки сообщения от пользователя телеграмм-бота.
     * В качестве аргумента принимает текст.
     *
     * @param update
     * @return
     */

    public String selectBotCommand(Update update) {
        String inputText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        //Отдельная обработка кодового слова для входа для администратора
        if (inputText.equals("/кодовое_слово")){
            adminsUtil.addAdmin(update);
            //Если пользователь чата стал админом, то и Меню ему надо обновит
            return "Добро пожаловать, Администратор!";
        }

        //Здесь сделаем обработку "админских" команд
        //На данный момент список возможных пунктов меню следующий (! требует обсуждения и доработки! ):
        // /view_questions - посмотреть вопросы пользователей
        // /add_pet_to_user - добавить питомца к профилю пользователя
        // !!!!!  эти команды пока не обработаны, и не ясно будут ли они в итоге !!!!
        // /view_actual_reports - посмотреть свежие/актуальные отчеты
        // /view_problems_with_reports - посмотреть проблемы с отчетами

        //Отдельная обработка команды /view_questions - просмотр всех активных (актуальных) вопросов
        if (inputText.equals("/view_questions")){
            return questionsUtil.getAllActiveQuestions();
        }

        //Когда пользователь выбирает приют, то сохраняем в БД под его chatId этот выбор
        if (inputText.equals("/dog_shelter") || inputText.equals("/cat_shelter")){
            userUtil.addShelterToUser(update);
            //Если пользователь выбрал тип приюта, то и Меню бота надо обновить

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
