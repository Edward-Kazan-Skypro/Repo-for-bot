package skypro.learn.tg_bot.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import skypro.learn.tg_bot.model.Admin;
import skypro.learn.tg_bot.model.InnerStatusUser;
import skypro.learn.tg_bot.model.User;
import skypro.learn.tg_bot.repository.AdminRepository;
import skypro.learn.tg_bot.repository.UsersRepository;


/**
 * Класс для определения - какие команды Меню будут отображаться пользователю.
 * В текущей версии приложения не реализовано ввиду нестабильной работы.
 */
@Component
@Slf4j
public class BotMenuSelector {

    final private UsersRepository usersRepository;
    final private AdminRepository adminRepository;
    final private AddCommandsToBotMenu addCommandsToBotMenu;

    public BotMenuSelector(UsersRepository usersRepository,
                           AdminRepository adminRepository,
                           AddCommandsToBotMenu addCommandsToBotMenu) {
        this.usersRepository = usersRepository;
        this.adminRepository = adminRepository;
        this.addCommandsToBotMenu = addCommandsToBotMenu;
    }

    public SetMyCommands selectMenuForUser(Long chatId) {
        User user = new User();
        //Для удобства получим ранее зарегистрированного в БД пользователя и админа
        if (usersRepository.findById(chatId).isPresent()) {
            user = usersRepository.findById(chatId).get();
            System.out.println(user);
        }
        //SetMyCommands setMyCommands = null;
        //Узнаем его внутренний статус и заполняем Меню под него
        //Сгруппируем по приютам
        if (user.getVisitedShelter().equals("/dog_shelter")) {
            if (user.getInnerStatusUser() == InnerStatusUser.USER_WITH_PET) {
                return new SetMyCommands(
                        addCommandsToBotMenu.addCommandsForRegisteredUsersAfterAdoptDog(),
                        new BotCommandScopeDefault(),
                        null);
            } else {
                return new SetMyCommands(
                        addCommandsToBotMenu.addCommandsForDogShelterUsers(),
                        new BotCommandScopeDefault(),
                        null);
            }
        }
        if (user.getVisitedShelter().equals("/cat_shelter")) {
            if (user.getInnerStatusUser() == InnerStatusUser.USER_WITH_PET) {
                return new SetMyCommands(
                        addCommandsToBotMenu.addCommandsForRegisteredUsersAfterAdoptCat(),
                        new BotCommandScopeDefault(),
                        null);
            } else {
                return new SetMyCommands(
                        addCommandsToBotMenu.addCommandsForCatShelterUsers(),
                        new BotCommandScopeDefault(),
                        null);

            }
        } 
        return new SetMyCommands(
                    addCommandsToBotMenu.addCommandsForNewUser(),
                    new BotCommandScopeDefault(),
                    null);
    }

    public SetMyCommands selectMenuForAdmin(Long chatId) {
        SetMyCommands setMyCommands = null;
        Admin admin = null;
        if (adminRepository.findById(chatId).isPresent()) {
            admin = adminRepository.findById(chatId).get();
        }
        if (admin != null) {
            setMyCommands = new SetMyCommands(
                    addCommandsToBotMenu.addCommandsForAdmin(),
                    new BotCommandScopeDefault(),
                    null);
        }
        assert setMyCommands != null;
        System.out.println(setMyCommands);
        return setMyCommands;
    }
}
