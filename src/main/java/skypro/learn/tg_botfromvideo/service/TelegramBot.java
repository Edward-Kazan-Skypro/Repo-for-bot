package skypro.learn.tg_botfromvideo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import skypro.learn.tg_botfromvideo.config.BotConfig;
import skypro.learn.tg_botfromvideo.model.User;
import skypro.learn.tg_botfromvideo.repository.UserRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("deprecation")
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final String HELP_TXT = "Приветствую!\n" +
            "Этот бот создан в учебных целях - \n" +
            "как результат совместной работы.\n\n" +
            "Выберите /view_pets чтобы просмотреть сведения о питомцах\n\n" +
            "Выберите /volunteer чтобы пообщаться с волонтером \n\n";
    final BotConfig botConfig;

    final UserRepository userRepository;

    public TelegramBot(BotConfig botConfig, UserRepository userRepository) {
        this.botConfig = botConfig;
        this.userRepository = userRepository;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "приветствие пользователя"));
        listOfCommands.add(new BotCommand("/view_pets", "просмотр сведений о питомцах"));
        listOfCommands.add(new BotCommand("/help", "просмотр краткой справки по боту"));
        listOfCommands.add(new BotCommand("/volunteer", "позвать в чат волонтера"));
        listOfCommands.add(new BotCommand("/settings", "установки"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Произошла ошибка: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            //Здесь получаем текст, который пользователь ввел в чате
            String inputMessageText = update.getMessage().getText();
            //Узнаем id пользователя(собеседника)
            long chatId = update.getMessage().getChatId();

            //Здесь выполняются разные методы, в зависимости от выбранной команды (из меню или чата)
            switch (inputMessageText){
                case "/start":
                    startCommandRecieved(chatId, update.getMessage().getChat().getFirstName());
                    registerUser(update.getMessage());
                    break;

                case "/help":
                    sendMessage(chatId, HELP_TXT);
                    break;
                default:
                    sendMessage(chatId, "Остальное в разработке...");
            }
        }
    }

    private void registerUser(Message message){
        if (userRepository.findById(message.getChatId()).isEmpty()){
            var chat = message.getChat();
            var chatId = message.getChatId();
            User user = new User();
            user.setChatId(chatId);
            user.setName(chat.getFirstName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            log.info("Пользователь " + user + "добавлен в БД");


        }

    }


    //Метод для отправки сообщения, если выбрана команда /start
    private void startCommandRecieved(long chatId, String name){
        String outputMessageText = "Привет, " + name + "! Добро пожаловать!";
        sendMessage(chatId, outputMessageText);
        log.info("Направлен ответ пользователю: " + name);
    }

    //Общий приватный метод, который получает id пользователя и текст (который мы отправляем пользователю)
    //Этот метод используется при отправке всех сообщений в боте
    private void sendMessage(long chatId, String textToSend){
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
    public String getBotToken(){
        return botConfig.getToken();
    }
}
