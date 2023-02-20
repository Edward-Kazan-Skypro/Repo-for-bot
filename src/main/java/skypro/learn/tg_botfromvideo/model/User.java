package skypro.learn.tg_botfromvideo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.glassfish.grizzly.http.util.TimeStamp;

import java.sql.Timestamp;

@Entity(name = "usersDataTable")
@Data
@NoArgsConstructor
public class User {

    @Id
    private Long chatId;

    private String name;

    private Timestamp registeredAt;
}
