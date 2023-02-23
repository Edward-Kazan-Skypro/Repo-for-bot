package skypro.learn.tg_botfromvideo.bot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import skypro.learn.tg_botfromvideo.bot.commands.AboutShelterCommand;
import skypro.learn.tg_botfromvideo.bot.commands.AdoptAnimalCommand;
import skypro.learn.tg_botfromvideo.bot.commands.HelpCommand;

public class CommandSelector {

    /**
     * Метод для обработки сообщения от пользователя телеграмм-бота
     * @param update
     * @return
     */
    public String selectBotCommand(Update update) {
        String answer = "Обработка запроса еще не реализована!";
        if (update.hasMessage() && update.getMessage().hasText()) {
            //Здесь получаем текст, который пользователь ввел в чате
            String inputMessageText = update.getMessage().getText();
            //Здесь выполняются разные методы, в зависимости от выбранной команды (из меню или чата)
            switch (inputMessageText) {
                case "/about_shelter":
                    return new AboutShelterCommand().getABOUT_SHELTER_MENU_TXT();
                case "/shelters_address":
                case "/safety_measures":
                case "/register_user":
                case "/volunteer":

                case "/adopt_animal":
                    return new AdoptAnimalCommand().getADOPT_ANIMAL_MENU_TXT();
                case "/meeting_with_pet":
                case "/documents_for_adopting":
                case "/recommendations_for_transporting":
                case "/improvement_home_for_puppy":
                case "/improvement_home_for_adult dog":
                case "/improvement_home_for_disabled dog":
                case "/initial_appeal_with_dog":
                case "/contacts_of_cynologists":
                case "/reasons_for_rejection":

                case "/send_report":

                case "/help":
                    return new HelpCommand().getHELP_MENU_TXT();
                default:
                    return "Обработка запроса еще не реализована!";
            }
        }
        return answer;
    }
}

/* case "/start":
                    startCommandRecieved(chatId, update.getMessage().getChat().getFirstName());
                    registerUser(update.getMessage());
                    break;*/
/* private void registerUser(Message message) {
        if (userRepository.findById(message.getChatId()).isEmpty()) {
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
    private void startCommandRecieved(long chatId, String name) {
        String outputMessageText = "Привет, " + name + "! Добро пожаловать!";
        sendMessage(chatId, outputMessageText);
        log.info("Направлен ответ пользователю: " + name);
    }*/
