package skypro.learn.tg_bot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import skypro.learn.tg_bot.model.User;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
    //TestUserForMenu getTestUserForMenuByByChatId(Long chatId);
}
