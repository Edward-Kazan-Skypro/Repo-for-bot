package skypro.learn.tg_botfromvideo.bot.commands.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HelpCommand {

    private final String HELP_MENU_TXT = """
            Приветствую!
            Этот бот создан в учебных целях -
            как результат совместной работы
            студентов в рамках программы
            обучения по направлению Java-программист.""";
}
