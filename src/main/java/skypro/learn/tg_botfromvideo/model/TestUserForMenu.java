package skypro.learn.tg_botfromvideo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "userForTesting")
@Data
@NoArgsConstructor
public class TestUserForMenu {

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

    private InnerStatusUser innerStatusUser;

}
