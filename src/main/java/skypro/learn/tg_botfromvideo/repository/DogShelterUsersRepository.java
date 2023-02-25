package skypro.learn.tg_botfromvideo.repository;

import org.springframework.data.repository.CrudRepository;
import skypro.learn.tg_botfromvideo.model.RegisteredUserForDogShelter;

public interface DogShelterUsersRepository extends CrudRepository<RegisteredUserForDogShelter, Long> {
}
