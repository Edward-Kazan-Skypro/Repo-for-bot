package skypro.learn.tg_botfromvideo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import skypro.learn.tg_botfromvideo.model.User;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
    //TestUserForMenu getTestUserForMenuByByChatId(Long chatId);
}
