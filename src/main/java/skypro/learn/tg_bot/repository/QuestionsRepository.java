package skypro.learn.tg_bot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import skypro.learn.tg_bot.model.QuestionFromUser;

@Repository
public interface QuestionsRepository extends CrudRepository<QuestionFromUser, Long> {
}
