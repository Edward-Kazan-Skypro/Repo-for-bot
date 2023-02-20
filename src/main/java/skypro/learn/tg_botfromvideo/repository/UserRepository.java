package skypro.learn.tg_botfromvideo.repository;

import org.springframework.data.repository.CrudRepository;
import skypro.learn.tg_botfromvideo.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
