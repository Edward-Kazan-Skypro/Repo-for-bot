package skypro.learn.tg_botfromvideo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;

@Entity(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    private Long chatId;

    private String nameInChat;

    private String firstName;

    private String lastName;

    private String visitedShelter = "not selected";

    private String phoneNumber;

    private String e_mail;

    private boolean isRegistered;

    //поле для хранения правда/ложь по наличию взятого питомца
    private boolean isHavePet;

    //нужно поле для хранения списка питомцев у пользователя
    //или одного питомца???
    //одного как бы проще, но все равно нужен класс Pet!
    //Пусть для простоты будет текстовое поле с кличкой и видом питомца,
    //Например - кошка Мурка.
    //Вряд ли в приюте будет несколько животных с одной и той же кличкой
    private String petName;

    private LocalDate dateAdoptPet;

    private InnerStatusUser innerStatusUser;

    //Выключил, так и не разобрался как это работает...
    //@OneToMany (fetch = FetchType.LAZY, mappedBy = "users")
    //private Collection<Report> reports;
}
