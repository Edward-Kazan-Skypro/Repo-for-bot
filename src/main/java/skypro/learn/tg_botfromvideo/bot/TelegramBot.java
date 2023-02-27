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
import skypro.learn.tg_botfromvideo.model.RegisteredUserForCatShelter;
import skypro.learn.tg_botfromvideo.model.RegisteredUserForDogShelter;
import skypro.learn.tg_botfromvideo.model.Visitor;
import skypro.learn.tg_botfromvideo.repository.CatShelterUsersRepository;
import skypro.learn.tg_botfromvideo.repository.DogShelterUsersRepository;
import skypro.learn.tg_botfromvideo.repository.VisitorsRepository;


@SuppressWarnings("deprecation")
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;
    final VisitorsRepository visitorsRepository;
    final DogShelterUsersRepository dogShelterUsersRepository;
    final CatShelterUsersRepository catShelterUsersRepository;
    final BotMenuCreator botMenuCreator;

    public TelegramBot(BotConfig botConfig,
                       VisitorsRepository visitorsRepository,
                       DogShelterUsersRepository dogShelterUsersRepository,
                       CatShelterUsersRepository catShelterUsersRepository,
                       BotMenuCreator botMenuCreator) {
        this.botConfig = botConfig;
        this.visitorsRepository = visitorsRepository;
        this.dogShelterUsersRepository = dogShelterUsersRepository;
        this.catShelterUsersRepository = catShelterUsersRepository;
        this.botMenuCreator = botMenuCreator;
        try {
            this.execute(new SetMyCommands(
                    BotMenuCreator.addCommonCommandsToBotMenu(),
                    new BotCommandScopeDefault(),
                    null));
        } catch (TelegramApiException e) {
            log.error("Ошибка формирования МЕНЮ бота: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!visitorsRepository.existsById(update.getMessage().getChatId())) {
            registerVisitor(update);
        }
        var chatId = update.getMessage().getChatId();
        Visitor visitor = visitorsRepository.findById(chatId).get();
        String inputText = update.getMessage().getText();
        //Вот мы получили сообщение, которе пользователь ввел в чате.
        //Это может быть как готовая команда, так и, например, регистрационные данные.
        //Значит полученный текст надо проверить:
        // - если текст начинается с символа /, то это команда для меню;
        // - если с рег:, то это данные с регистрационными данными пользователя
        // - прочие данные мы не обрабатываем
        if (inputText.startsWith("/")) {
            String outputText = new CommandSelector(visitorsRepository).selectBotCommand(inputText, visitor);
            sendMessage(chatId, outputText);
        }
        if (inputText.startsWith("рег:")) {
            if (registerUser(inputText, visitor)) {
                sendMessage(chatId, new CommandSelector(visitorsRepository).selectBotCommand("/registration_user_complete", visitor));
            } else {
                sendMessage(chatId, new CommandSelector(visitorsRepository).selectBotCommand("/registration_user_error", visitor));
            }
        }
    }

    /**
     * Метод для авто-регистрации посетителей чата.
     * Сохраняется chatId и имя посетителя.
     *
     * @param update
     */
    private Visitor registerVisitor(Update update) {
        var chatId = update.getMessage().getChatId();
        var name = update.getMessage().getChat().getFirstName();
        Visitor visitor = new Visitor();
        visitor.setNameInChat(name);
        visitor.setChatId(chatId);
        visitor.setVisitedShelter("");
        visitorsRepository.save(visitor);
        log.info("Посетитель " + name + " добавлен в БД посетителей");
        return visitor;
    }

    private boolean registerUser(String inputText, Visitor visitor) {
        //Зарегистрированный пользователь раньше был просто посетителем,
        //Значит часть информации можно получить из экземпляра visitor
        //Соберем значения полей, чтобы потом их присвоить
        Long newChatId = visitor.getChatId();
        String newNameInChat = visitor.getNameInChat();
        String visitedShelter = visitor.getVisitedShelter();
        //Получим значения полей из распарсенной строки
        String text = inputText.replaceFirst("рег:", "");
        System.out.println(inputText + " --> " + text);
        //Удалим пробелы внутри строки
        text = text.replaceAll(" ", "");
        //Теперь делим полученную строку на отдельные слова
        String[] words = text.split(";");
        //Надо проверить - не пустые ли эти 4 элемента массива
        //Если одно из них отсутствует, то регистрация будет неудачной и в БД ничего сохранять нельзя
        for (String word: words) {
            if (word.isEmpty() && word.isBlank()){
                return false;
            }
        }
        //Теперь из массива получим имя, фамилию, номер телефона и почту
        String newFirstName = words[0];
        String newLastName = words[1];
        String newPhoneNumber = words[2];
        String newEmail = words[3];
        String selectedShelter;
        //Значения всех будущих полей получены, но надо решить в качестве кого регистрируется пользователь?
        //И тут мы учитываем из какого раздела меню он перешел в регистрацию.
        //Если из раздела "приют для собак", то в туда его и регистрируем, и наоборот.
        if (!visitorsRepository.findById(visitor.getChatId()).get().getVisitedShelter().isEmpty()) {
            selectedShelter = visitorsRepository.findById(visitor.getChatId()).get().getVisitedShelter();
            if (selectedShelter.equals("/dog_shelter")) {
                RegisteredUserForDogShelter newUserForDogShelter = new RegisteredUserForDogShelter();
                newUserForDogShelter.setChatId(newChatId);
                newUserForDogShelter.setNameInChat(newNameInChat);
                newUserForDogShelter.setFirstName(newFirstName);
                newUserForDogShelter.setLastName(newLastName);
                newUserForDogShelter.setPhoneNumber(newPhoneNumber);
                newUserForDogShelter.setE_mail(newEmail);
                newUserForDogShelter.setVisitedShelter(selectedShelter);
                dogShelterUsersRepository.save(newUserForDogShelter);
            }
            if (selectedShelter.equals("/cat_shelter")) {
                RegisteredUserForCatShelter newUserFoCatShelter = new RegisteredUserForCatShelter();
                newUserFoCatShelter.setChatId(newChatId);
                newUserFoCatShelter.setNameInChat(newNameInChat);
                newUserFoCatShelter.setFirstName(newFirstName);
                newUserFoCatShelter.setLastName(newLastName);
                newUserFoCatShelter.setPhoneNumber(newPhoneNumber);
                newUserFoCatShelter.setE_mail(newEmail);
                newUserFoCatShelter.setVisitedShelter(selectedShelter);
                catShelterUsersRepository.save(newUserFoCatShelter);
            }
            return true;
        }
        return false;
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