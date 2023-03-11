package skypro.learn.tg_bot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import skypro.learn.tg_bot.model.Admin;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Long> {
}
