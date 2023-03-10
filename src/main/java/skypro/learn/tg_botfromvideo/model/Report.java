package skypro.learn.tg_botfromvideo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import skypro.learn.tg_botfromvideo.repository.UsersRepository;

import java.sql.Timestamp;

@Entity(name = "reportsTable")
@Data
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Имя пользователя

    private String userName;
    //id пользователя

    private String userId;
    //url файла

    private String fileUrl;
    //сам отчет

    private String descriptionReport;
}

   /* @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp dateReport;

    //фото питомца - надо потом добавить!

    //рацион
    private String animalDiet;

    //самочувствие
    private String animalHealth;

    //поведение
    private String animalBehavior;

    //поле для хранения chatId пользователя, от кого направлен отчет
    private Long chatId;

    //поле для хранения имени пользователя, от которого направлен отчет
    private String userName;

    @Override
    public String toString() {
        return "Отчет пользователя " + userName + "от " + dateReport + "\n" +
                "Рацион животного: " + animalDiet + "\n" +
                "Общее самочувствие и привыкание к новому месту " + animalHealth + "\n" +
                "Изменение в поведении: отказ от старых привычек, приобретение новых " + animalBehavior + "\n";
    }*/

