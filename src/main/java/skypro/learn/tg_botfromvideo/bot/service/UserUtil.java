package skypro.learn.tg_botfromvideo.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import skypro.learn.tg_botfromvideo.model.InnerStatusUser;
import skypro.learn.tg_botfromvideo.model.User;
import skypro.learn.tg_botfromvideo.repository.UsersRepository;

import java.sql.Timestamp;
import java.time.LocalDate;

@Slf4j
@Component
public class UserUtil {
    final private UsersRepository usersRepository;

    public UserUtil(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    /**
     * Метод для регистрации пользователей, которые планируют взять питомца из приюта.
     * По итогам работы метода может быть создан один из двух зарегистрированных пользователей
     * (для каждого приюта) или не создан, если введены некорректные регистрационные данные.
     *
     * @author Мухаметзянов Эдуард
     */
    public boolean registerUser(Update update) {
        //Соберем значения полей, чтобы потом их присвоить
        Long newChatId = update.getMessage().getChatId();
        String newNameInChat = update.getMessage().getChat().getFirstName();
        String selectedShelter = "";
        if (usersRepository.findById(newChatId).isPresent()) {
            selectedShelter = usersRepository.findById(newChatId).get().getVisitedShelter();
        }
        //Получаем строку из сообщения
        String text = update.getMessage().getText();
        //Уберем из строки уже ненужные символы
        text = text.replaceAll("рег:", "");
        //Удалим пробелы внутри строки
        text = text.replaceAll(" ", "");
        //Теперь делим полученную строку на отдельные слова
        String[] words = text.split(";");
        //Если после деления на слова получилось меньше 4 элементов, то во введенной строке чего-то не хватает,
        //а значит и данные для регистрации введены неправильно
        if (words.length != 4) {
            return false;
        }
        //Теперь из массива получим имя, фамилию, номер телефона и почту
        String newFirstName = words[0];
        String newLastName = words[1];
        String newPhoneNumber = words[2];
        String newEmail = words[3];

        //Значения всех будущих полей получены, создаем экземпляр класса и заполняем его поля.
        User newUser = new User();
        newUser.setChatId(newChatId);
        newUser.setNameInChat(newNameInChat);
        newUser.setFirstName(newFirstName);
        newUser.setLastName(newLastName);
        newUser.setPhoneNumber(newPhoneNumber);
        newUser.setE_mail(newEmail);
        newUser.setVisitedShelter(selectedShelter);
        newUser.setRegistered(true);
        newUser.setInnerStatusUser(InnerStatusUser.REGISTERED_USER);
        //Сохраняем экземпляр в БД
        usersRepository.save(newUser);
        //Возвращаем логическое значение
        //Если в БД есть пользователь с таким chatId, то вернется true
        //Если нет - вернется false
        boolean userIsCreated = usersRepository.existsById(newChatId);
        if (userIsCreated) {
            log.info("Регистрация завершена успешно!");
        } else {
            log.error("Регистрация не удалась...");
        }
        return userIsCreated;
    }

    /**
     * Метод для первичной регистрации пользователя.
     * Вызывается при отправке какого-либо сообщения Боту.
     * @param chatId
     * @param nameInChat
     */
    public void silentRegistrationUser(Long chatId, String nameInChat) {
        User user = new User();
        user.setChatId(chatId);
        user.setNameInChat(nameInChat);
        user.setRegistered(false);
        user.setInnerStatusUser(InnerStatusUser.NOT_REGISTERED_USER);
        usersRepository.save(user);
        log.info("Новый пользователь " + nameInChat + " записан в БД");
    }

    /**
     * Метод добавления в профиль пользователя информации о взятом из приюта питомце.
     * Добавляется вид и кличка питомца.
     * @param update
     * @return
     */

    public boolean addPetToUser(Update update) {
        //Получаем строку из сообщения
        String text = update.getMessage().getText();
        //Уберем из строки уже ненужные символы
        text = text.replaceAll("питомец:", "");
        //Теперь делим полученную строку на отдельные слова
        String[] words = text.split(";");
        //Если после деления на слова получилось меньше 3 элементов, то во введенной строке чего-то не хватает,
        //а значит придется ввести строку снова.
        if (words.length != 3) {
            return false;
        }
        //Теперь из массива получим chatId усыновителя, вид питомца и кличку питомца
        String chatIdUser = words[0];
        String typePet = words[1];
        String namePet = words[2];
        boolean petIsAdded = false;
        //Найдем "усыновителя" под таким номером
        if (usersRepository.findById(Long.valueOf(chatIdUser)).isPresent()) {
            User user = usersRepository.findById(Long.valueOf(chatIdUser)).get();
            user.setHavePet(true);
            user.setPetName(typePet + " " + namePet);
            user.setDateAdoptPet(LocalDate.now());
            user.setInnerStatusUser(InnerStatusUser.USER_WITH_PET);
            usersRepository.save(user);
            petIsAdded = true;
        }
        if (petIsAdded) {
            log.info("Привязка питомца к пользователю успешна!");
        } else {
            log.error("Привязка питомца к пользователю не удалась...");
        }
        return petIsAdded;
    }

    /**
     * Метод добавления в профиль пользователя информации о выбранном приюте.
     * @param update
     */

    public void addShelterToUser(Update update) {
        //Когда пользователь выбирает приют, то сохраняем в БД под его chatId этот выбор
        String inputText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        if (usersRepository.findById(chatId).isPresent()) {
            User user = usersRepository.findById(chatId).get();
            user.setVisitedShelter(inputText);
            usersRepository.save(user);
            log.info("Пользователь выбрал " + inputText);
        }
    }
}
