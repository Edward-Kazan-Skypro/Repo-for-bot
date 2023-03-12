package skypro.learn.tg_bot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    //@ManyToOne(fetch=FetchType.LAZY)
    //private User users;
}