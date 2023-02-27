package skypro.learn.tg_botfromvideo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "visitorsTable")
@Data
@NoArgsConstructor
public class Visitor {

    @Id
    private Long chatId;

    private String nameInChat;

    private String visitedShelter;

    private String lastCommand;
}
