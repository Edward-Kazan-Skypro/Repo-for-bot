package skypro.learn.tg_bot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import skypro.learn.tg_bot.bot.service.*;
import skypro.learn.tg_bot.bot.config.BotConfig;
import skypro.learn.tg_bot.model.Report;
import skypro.learn.tg_bot.repository.ReportsRepository;
import skypro.learn.tg_bot.repository.UsersRepository;
import java.time.LocalDate;

@SuppressWarnings("deprecation")
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final private BotConfig botConfig;
    final private CommandUtil commandUtil;
    final private UserUtil userUtil;
    final private QuestionsUtil questionsUtil;
    final private UsersRepository usersRepository;
    final private ReportsRepository reportsRepository;

    public TelegramBot(BotConfig botConfig,
                       AddCommandsToBotMenu addCommandsToBotMenu,
                       CommandUtil commandUtil,
                       UserUtil userUtil,
                       ReportUtil reportUtil,
                       QuestionsUtil questionsUtil,
                       UsersRepository usersRepository,
                       ReportsRepository reportsRepository) {
        this.botConfig = botConfig;
        this.commandUtil = commandUtil;
        this.userUtil = userUtil;
        this.questionsUtil = questionsUtil;
        this.usersRepository = usersRepository;
        this.reportsRepository = reportsRepository;

        //Здесь выбираем список команд для нового пользователя
        try {
            execute(new SetMyCommands(
                    addCommandsToBotMenu.addCommandsForNewUser(),
                    new BotCommandScopeDefault(),
                    null));
        } catch (TelegramApiException e) {
            log.error("Ошибка формирования МЕНЮ бота: " + e.getMessage());
        }
    }

    /**
     * Метод для обработки сообщений.
     * @param update
     */

    @Override
    public void onUpdateReceived(Update update) {
        //Получаем chatId - уникальный идентификатор пользователя
        //т.е. узнаем кто нам написал
        Long chatId = update.getMessage().getChatId();
        String subText;
        //В первую очередь обновим отображаемое Меню бота для пользователя
        //Если пользователя с таким chatId нет, то сохраним его в БД
        if (!usersRepository.existsById(chatId)) {
            String nameInChat = update.getMessage().getChat().getFirstName();
            userUtil.silentRegistrationUser(chatId, nameInChat);
            log.info("Новый пользователь " + nameInChat + " зарегистрирован как посетитель");
        }

        //Ниже - закомментированный код заполнения кнопки Меню командами,
        //соответствующими статусу пользователя.
        //Код выключил, т.к. работает нестабильно.
        //else {
        //Обновляем Меню для пользователя, соответствующее его статусу
        //    try {
        //        execute(botMenuSelector.selectMenuForUser(chatId));
        //    } catch (TelegramApiException e) {
        //        log.error("Ошибка формирования Пользовательского Меню бота: " + e.getMessage());
        //    }
        //}
        //Если сообщение от админа, то выбираем его Меню
        //if (adminRepository.existsById(chatId)) {
        //    try {
        //        execute(botMenuSelector.selectMenuForAdmin(chatId));
        //    } catch (TelegramApiException e) {
        //        log.error("Ошибка формирования Административного меню бота: " + e.getMessage());
        //    }
        //}

        //В этой части - выбираем действие в зависимости от полученного update
        if (update.hasMessage() && update.getMessage().hasText()) {
            String inputText = update.getMessage().getText();
            //Если введенный текст начинается с "/", то сразу отправляем на обработку
            if (inputText.startsWith("/")) {
                String outputText = commandUtil.selectBotCommand(update);
                sendMessage(chatId, outputText);
            }
            //Если текст начинается с "рег:", то значит поступили регистрационные данные
            subText = inputText.substring(0, 4);
            if (subText.equals("рег:")) {
                if (userUtil.registerUser(update)) {
                    sendMessage(chatId, "Регистрация пользователя завершена успешно!");
                } else {
                    sendMessage(chatId, "Регистрация не удалась...\n" +
                            "\n" +
                            "Попробуйте снова...\n" +
                            "\n" +
                            "Возможные причины:\n" +
                            "- забыли написать имя, фамилию, номер телефона или электронную почту\n" +
                            "Сообщение, содержащее регистрационные данные, должно выглядеть так:\n" +
                            "рег: Иван; Иванов; +7(999) 000-00-00; почта@почта.com\n" +
                            "- не выбран приют для питомцев - начните общение с ботом с команды\n" +
                            "\n" +
                            "/start");
                }
            }
            //Обработка команды /add_pet_to_user осуществляется штатно через CommandSelector,
            //т.е. выводится на экран инструкция для правильного добавления питомца к профилю пользователя.
            //Значит обработаем введенную строку
            subText = inputText.substring(0, 8);
            if (subText.equals("питомец:")) {
                if (userUtil.addPetToUser(update)) {
                    sendMessage(chatId, "Привязка питомца к пользователю успешна!");
                } else {
                    sendMessage(chatId, "Привязка питомца к пользователю не удалась...\n" +
                            "Попробуйте еще раз ввести данные, подсказка - см. команду /add_pet_to_user");
                }
            }
            subText = inputText.substring(0, 7);
            if (subText.equals("вопрос:")) {
                if (questionsUtil.addQuestion(update)) {
                    sendMessage(chatId, "Уважаемый Пользователь! Ваш вопрос сохранен и будет направлен волонтеру.");
                } else {
                    sendMessage(chatId, "Уважаемый Пользователь! Ваш вопрос не сохранен, пожалуйста внимательнее перечитайте как надо записать вопрос.");
                }
            }
        }
        if (update.hasMessage() && update.getMessage().hasDocument()) {
            if (saveReport(update)) {
                sendMessage(chatId, "Отчет сохранен!");
            } else {
                sendMessage(chatId, "Отчет не сохранен. Пожалуйста, добавьте описание состояния питомца!");
            }
        }
        if (update.getMessage().getText() == null) {
            sendMessage(chatId, "Пожалуйста, введите сообщение или команду\n" +
                    " /start");
        }
    }

    /**
     * Метод сохранения документа - отчета пользователя о состоянии питомца.          *
     * @param update
     * @return
     */

    private boolean saveReport(Update update) {
        GetFile getFile = new GetFile(update.getMessage().getDocument().getFileId());
        boolean reportIsSaved = false;
        try {
            Report report = new Report();
            File file = execute(getFile);
            String url = file.getFileUrl(botConfig.getToken());
            String userName = update.getMessage().getFrom().getFirstName();
            String userId = update.getMessage().getFrom().getId().toString();
            String description = update.getMessage().getCaption();
            if (description.isEmpty()) {
                return reportIsSaved;
            }
            report.setUserName(userName);
            report.setFileUrl(url);
            report.setUserId(userId);
            report.setDescriptionReport(description);
            report.setDateReport(LocalDate.now());
            reportsRepository.save(report);
            reportIsSaved = true;
        } catch (TelegramApiException e) {
            log.error("Ошибка сохранения файла отчета: " + e.getMessage());
        }
        return reportIsSaved;
    }


    /**
     * Метод для отправки сообщений.
     * Метод принимает два аргумента - chatId (для идентификации собеседника) и textToSend (текст сообщения)
     *
     * @param chatId
     * @param textToSend
     */
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        //Чтобы отправить сообщение, надо указать кому будем отправлять - а именно его chatId
        message.setChatId(chatId);
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Произошла ошибка: " + e.getMessage());
        }
    }

    //Здесь два обязательных метода для получения имени и токена бота
    //Без корректной работы этих методов соединения с ботом НЕ БУДЕТ
    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @SuppressWarnings("deprecation")
    public String getBotToken() {
        return botConfig.getToken();
    }
}