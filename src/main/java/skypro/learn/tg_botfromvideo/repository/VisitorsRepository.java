package skypro.learn.tg_botfromvideo.repository;

import org.springframework.data.repository.CrudRepository;
import skypro.learn.tg_botfromvideo.model.Visitor;

public interface VisitorsRepository extends CrudRepository<Visitor, Long> {
}
