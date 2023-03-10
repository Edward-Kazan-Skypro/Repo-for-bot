package skypro.learn.tg_botfromvideo.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import skypro.learn.tg_botfromvideo.bot.config.BotInitializer;
import skypro.learn.tg_botfromvideo.bot.service.*;
import skypro.learn.tg_botfromvideo.bot.config.BotConfig;
import skypro.learn.tg_botfromvideo.model.Report;
import skypro.learn.tg_botfromvideo.repository.AdminRepository;
import skypro.learn.tg_botfromvideo.repository.ReportsRepository;
import skypro.learn.tg_botfromvideo.repository.UsersRepository;

import java.util.Comparator;
import java.util.List;


@SuppressWarnings("deprecation")
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final private BotConfig botConfig;
    final private BotMenuCreator botMenuCreator;
    final private BotMenuSelector botMenuSelector;
    final private CommandUtil commandUtil;
    final private UserUtil userUtil;
    final private ReportUtil reportUtil;
    final private QuestionsUtil questionsUtil;
    final private UsersRepository usersRepository;
    final private AdminRepository adminRepository;

    final private ReportsRepository reportsRepository;

    public TelegramBot(BotConfig botConfig,
                       BotMenuCreator botMenuCreator,
                       BotMenuSelector botMenuSelector,
                       CommandUtil commandUtil,
                       UserUtil userUtil,
                       ReportUtil reportUtil,
                       QuestionsUtil questionsUtil,
                       UsersRepository usersRepository,
                       AdminRepository adminRepository,
                       ReportsRepository reportsRepository) {
        this.botConfig = botConfig;
        this.botMenuCreator = botMenuCreator;
        this.botMenuSelector = botMenuSelector;
        this.commandUtil = commandUtil;
        this.userUtil = userUtil;
        this.reportUtil = reportUtil;
        this.questionsUtil = questionsUtil;
        this.usersRepository = usersRepository;
        this.adminRepository = adminRepository;
        this.reportsRepository = reportsRepository;
        //Здесь выбираем список команд для нового пользователя
        try {
            execute(new SetMyCommands(
                    botMenuCreator.addCommandsForNewUser(),
                    new BotCommandScopeDefault(),
                    null));
        } catch (TelegramApiException e) {
            log.error("Ошибка формирования МЕНЮ бота: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        //Получаем chatId - уникальный идентификатор пользователя
        //т.е. узнаем кто нам написал
        Long chatId = update.getMessage().getChatId();
        boolean inputMessage = update.getMessage().hasDocument();
        String checkInputText = update.getMessage().getText();
        System.out.println("inputMessage " + inputMessage);

        //В первую очередь обновим отображаемое Меню бота для пользователя
        //Если пользователя с таким chatId нет, то сохраним его в БД
        if (!usersRepository.existsById(chatId)) {
            String nameInChat = update.getMessage().getChat().getFirstName();
            userUtil.silentRegistrationUser(chatId, nameInChat);
            log.info("Новый пользователь " + nameInChat + " зарегистрирован как посетитель");
        } else {
            //Обновляем Меню для пользователя, соответствующее его статусу
            try {
                execute(botMenuSelector.selectMenuForUser(chatId));
            } catch (TelegramApiException e) {
                log.error("Ошибка формирования Пользовательского Меню бота: " + e.getMessage());
            }
        }
        //Если сообщение от админа, то выбираем его Меню
        if (adminRepository.existsById(chatId)) {
            try {
                execute(botMenuSelector.selectMenuForAdmin(chatId));
            } catch (TelegramApiException e) {
                log.error("Ошибка формирования Административного меню бота: " + e.getMessage());
            }
        }

        //В этой части - выбираем действие в зависимости от полученного update
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getText() == null) {
                sendMessage(chatId, "Пожалуйста, введите сообщение или команду\n" +
                        " /start");
            } else {
                String inputText = update.getMessage().getText();
                //Если введенный текст начинается с "/", то сразу отправляем на обработку
                if (inputText.startsWith("/")) {
                    String outputText = commandUtil.selectBotCommand(update);
                    sendMessage(chatId, outputText);
                }
                //Если текст начинается с "рег:", то значит поступили регистрационные данные
                if (inputText.startsWith("рег:")) {
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
                    //Обработка команды /add_pet_to_user осуществляется штатно через CommandSelector,
                    //т.е. выводится на экран инструкция для правильного добавления питомца к профилю пользователя.
                    //Значит обработаем введенную строку
                    if (inputText.startsWith("питомец:")) {
                        if (userUtil.addPetToUser(update)) {
                            sendMessage(chatId, "Привязка питомца к пользователю успешна!");
                        } else {
                            sendMessage(chatId, "Привязка питомца к пользователю не удалась...\n" +
                                    "Попробуйте еще раз ввести данные, подсказка - см. команду /add_pet_to_user");
                        }
                    }
                    if (inputText.startsWith("вопрос:")) {
                        if (questionsUtil.addQuestion(update)) {
                            sendMessage(chatId, "Уважаемый Пользователь! Ваш вопрос сохранен и будет направлен волонтеру.");
                        } else {
                            sendMessage(chatId, "Уважаемый Пользователь! Ваш вопрос не сохранен, пожалуйста внимательнее перечитайте как надо записать вопрос.");
                        }
                    }
                }
            }
            if (update.hasMessage() && update.getMessage().hasDocument()) {
                if (addReport(update)) {
                    sendMessage(chatId, "Отчет (в виде картинки с текстом) сохранен!");
                } else {
                    sendMessage(chatId, "Отчет не сохранен. Пожалуйста, добавьте описание состояния питомца!");
                }
            }
        }
    }

    private boolean addReport(Update update) {
        /*List<PhotoSize> photos = update.getMessage().getPhoto();
        long chat_id = update.getMessage().getChatId();
        String f_id = photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null).getFileId();
        int f_width = photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null).getWidth();
        int f_height = photos.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null).getHeight();
        String caption = "file_id: " + f_id + "\nwidth: " + Integer.toString(f_width) + "\nheight: " + Integer.toString(f_height);
        SendPhoto msg = new SendPhoto()
                .setChatId(chat_id)
                .setPhoto(f_id)
                .setCaption(caption);
        try {
            sendPhoto(msg); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }*/

        GetFile getFile = new GetFile(update.getMessage().getDocument().getFileId());
        boolean reportIsSaved = false;
        try {
            Report report = new Report();
            File file = execute(getFile);
            //String fileId = file.getFileId();
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


       /* if (update.getMessage().hasDocument()){
            String doc_id = update.getMessage().getDocument().getFileId();
            String doc_name = update.getMessage().getDocument().getFileName();
            String doc_mime = update.getMessage().getDocument().getMimeType();
            Long doc_size = update.getMessage().getDocument().getFileSize();
            String getID = String.valueOf(update.getMessage().getFrom().getId());

            Document document = new Document();
            document.setMimeType(doc_mime);
            document.setFileName(doc_name);
            document.setFileSize(doc_size);
            document.setFileId(doc_id);

            GetFile getFile = new GetFile();
            getFile.setFileId(document.getFileId());
            try {
                File file = execute(getFile);
                downloadFile(file, new java.io.File(".pictures/"+getID+"_"+doc_name));

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }*/

        /*if (update.hasMessage() && update.getMessage().hasPhoto()) {

            // Message contains photo
            // Set variables
            long chat_id = update.getMessage().getChatId();

            // Array with photo objects with different sizes
            // We will get the biggest photo from that array
            List<PhotoSize> photos = update.getMessage().getPhoto();
            // Know file_id
            String f_id = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getFileId();
            // Know photo width
            int f_width = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getWidth();
            // Know photo height
            int f_height = photos.stream()
                    .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                    .findFirst()
                    .orElse(null).getHeight();
            // Set photo caption
            String caption = "file_id: " + f_id + "\nwidth: " + f_width + "\nheight: " + f_height;
            System.out.println("------------------------------------------------------");
            System.out.println(caption);
            System.out.println("------------------------------------------------------");
            SendPhoto msg = new SendPhoto();
            msg.setChatId(chat_id);
            //msg.setPhoto(f_id);
            msg.setCaption(caption);
            try {
                execute(msg);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            sendMessage(chat_id, String.valueOf(msg));
                //sendPhoto(msg); // Call method to send the photo with caption

        }*/

//Надо прописать обработку полученной фотографии питомца, которая является частью отчета

//Если текст начинается с "отчет:", то значит пользователь добавляет отчетные данные
//Надо продумать как маркировать команды, которые связаны с отправкой отчета !!!!


    /*public void uploadFile(String file_name, String file_id) throws IOException {
        String token = "6105284745:AAH3PI1oHtKI9MoEJFgmbJpaTH1xYg-JjZI";
        URL url = new URL("https://api.telegram.org/bot"+token+"/getFile?file_id="+file_id);
        BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = new URL("https://api.telegram.org/file/bot" + token + "/" + file_path);
        FileOutputStream fos = new FileOutputStream(upPath + file_name);
        System.out.println("Start upload");
        ReadableByteChannel rbc = Channels.newChannel(downoload.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        uploadFlag = 0;
        System.out.println("Uploaded!");
    }*/

   /* private SendMessage addButtonsInMenu(Long chatId){
        SendMessage message = new SendMessage(); // Create a message object
        message.setChatId(chatId);
        message.setText("Ваш выбор сохранен");
        // Create ReplyKeyboardMarkup object
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow firstPositiveAnswers = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        firstPositiveAnswers.add("Питомец чувствует себя хорошо,\n" +
                "активно передвигается и исследует территорию");
        // Add the first row to the keyboard
        keyboard.add(firstPositiveAnswers);
        // Create another keyboard row
        KeyboardRow firstNegativeAnswers = new KeyboardRow();
        // Set each button for the second line
        firstNegativeAnswers.add("Питомец чувствует не очень хорошо");
        //negativeAnswers.add("Row 2 Button 2");
        //negativeAnswers.add("Row 2 Button 3");
        // Add the second row to the keyboard
        keyboard.add(firstNegativeAnswers);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }*/