package skypro.learn.tg_bot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity(name = "admins")
@Data
@NoArgsConstructor
public class Admin {
    @Id
    private Long chatId;

    private String nameInChat;

    private Timestamp loginDate;

}
