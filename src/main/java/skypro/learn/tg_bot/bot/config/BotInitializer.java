package skypro.learn.tg_bot.bot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import skypro.learn.tg_bot.bot.TelegramBot;

@Slf4j
@Component
public class BotInitializer {
   final private TelegramBot bot;
    public BotInitializer(TelegramBot bot) {
        this.bot = bot;
    }

    /**
     * Метод для инициализации телеграмм-бота
     * @throws TelegramApiException
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(bot);
        }
        catch (TelegramApiException e) {
            log.error("Произошла ошибка: " + e.getMessage());
        }
    }
}