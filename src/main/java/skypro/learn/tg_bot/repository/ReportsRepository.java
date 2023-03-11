package skypro.learn.tg_bot.repository;

import org.springframework.data.repository.CrudRepository;
import skypro.learn.tg_bot.model.Report;

public interface ReportsRepository extends CrudRepository<Report, Long> {
}
