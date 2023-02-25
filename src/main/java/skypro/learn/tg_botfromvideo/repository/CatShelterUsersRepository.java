package skypro.learn.tg_botfromvideo.repository;

import org.springframework.data.repository.CrudRepository;
import skypro.learn.tg_botfromvideo.model.RegisteredUserForCatShelter;

public interface CatShelterUsersRepository extends CrudRepository<RegisteredUserForCatShelter, Long> {
}
