package skypro.learn.tg_bot.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String petName;

    private LocalDate dateAdoptPet;

    private InnerStatusUser innerStatusUser;
}