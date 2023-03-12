package skypro.learn.tg_bot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "questionFromUsers")
@Data
@NoArgsConstructor
public class QuestionFromUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;

    //поле для хранения имени пользователя, от которого поступил вопрос
    private String questioner;

    private LocalDate questionDate;

    private String question;

    //поле-индикатор, рассмотрен вопрос или нет
    //если пользователю дан ответ, то ставим false, по дефолту сразу true
    boolean isActive = true;

    private Long questionerChatId;

    @Override
    public String toString() {
        return "Дата вопроса: " + questionDate + "\n" +
                "Автор вопроса: " + questioner + "\n" +
                "chat id автора: " + questionerChatId + "\n" +
                "Вопрос: " + question +"\n\n";
    }
}