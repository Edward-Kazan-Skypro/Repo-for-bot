package skypro.learn.tg_botfromvideo.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import skypro.learn.tg_botfromvideo.bot.service.CommandSelector;
import skypro.learn.tg_botfromvideo.bot.config.BotConfig;
import skypro.learn.tg_botfromvideo.bot.service.BotMenuCreator;
import skypro.learn.tg_botfromvideo.repository.UserRepository;


@SuppressWarnings("deprecation")
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;
    final UserRepository userRepository;
    final BotMenuCreator botMenuCreator;

    public TelegramBot(BotConfig botConfig, UserRepository userRepository, BotMenuCreator botMenuCreator) {
        this.botConfig = botConfig;
        this.userRepository = userRepository;
        this.botMenuCreator = botMenuCreator;
        try {
            this.execute(new SetMyCommands(
                    BotMenuCreator.addCommandsToBotMenu(),
                    new BotCommandScopeDefault(),
                    null));
        } catch (TelegramApiException e) {
            log.error("Ошибка формирования МЕНЮ бота: " + e.getMessage());
        }
    }
    @Override
    public void onUpdateReceived(Update update) {
        sendMessage(update.getMessage().getChatId(), new CommandSelector().selectBotCommand(update));
    }

    //Общий приватный метод, который получает id пользователя и текст (который мы отправляем пользователю)
    //Этот метод используется при отправке всех сообщений в боте
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