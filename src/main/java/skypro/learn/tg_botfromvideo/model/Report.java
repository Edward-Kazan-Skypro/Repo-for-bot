package skypro.learn.tg_botfromvideo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;


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

    private LocalDate dateReport;

    @Override
    public String toString() {
        return "Отчет пользователя -  " + userName + ", от " + dateReport + ":\n" +
               "Описание состояния питомца - " + descriptionReport + "\n"+
               "Посмотреть весь отчет - " + fileUrl + "\n";
    }

    //Выключил, так и не разобрался как это работает...
    //@ManyToOne(optional = false)
    //@JoinColumn (name="author_report_id")
    //private User users;

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

