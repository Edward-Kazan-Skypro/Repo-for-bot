package skypro.learn.tg_botfromvideo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import skypro.learn.tg_botfromvideo.model.QuestionFromUser;

@Repository
public interface QuestionsRepository extends CrudRepository<QuestionFromUser, Long> {
}
