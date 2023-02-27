package skypro.learn.tg_botfromvideo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.sql.Timestamp;

@Entity(name = "reportsTable")
@Data
@NoArgsConstructor
public class Report {

    @Id
    private Timestamp dateReport;

    //Надо добавить поля:
    //фото питомца
    //кличка
    //прочие поля, которые прописаны в ТЗ

}
