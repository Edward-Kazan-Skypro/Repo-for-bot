package skypro.learn.tg_bot.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import skypro.learn.tg_bot.model.Admin;
import skypro.learn.tg_bot.repository.AdminRepository;
import skypro.learn.tg_bot.repository.UsersRepository;

import java.sql.Timestamp;

@Slf4j
@Component
public class AdminsUtil {

    final private AdminRepository adminRepository;
    final private UsersRepository usersRepository;

    public AdminsUtil(AdminRepository adminRepository,
                      UsersRepository usersRepository) {
        this.adminRepository = adminRepository;
        this.usersRepository = usersRepository;
    }

    //Метод добавления админа
    //Приложение, если поступает "/кодовое_слово", должно создать объект админ
    //найти есть ли пользователь с таким же chatId и удалить его из БД как пользователя
    //чтобы не было смешения ролей
    //и, наконец, сохранить админа в БД
    public boolean addAdmin (Update update){
        boolean adminIsCreated;
        Long chatId = update.getMessage().getChatId();
        //Если есть пользователь с таким chatId, то удаляем его из списка сохраненных пользователей
        if (usersRepository.existsById(chatId)){
            usersRepository.deleteById(chatId);
        }
        //Теперь создадим админа и добавим его в БД
        Admin admin = new Admin();
        admin.setLoginDate(new Timestamp(System.currentTimeMillis()));
        admin.setChatId(chatId);
        admin.setNameInChat(update.getMessage().getChat().getFirstName());
        adminRepository.save(admin);
        adminIsCreated = adminRepository.existsById(chatId);
        if (adminIsCreated){
            log.info("Администратор добавлен в БД!");
        } else{
            log.info("Администратор НЕ добавлен в БД!");
        }
        return adminIsCreated;
    }
}