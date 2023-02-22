package skypro.learn.tg_botfromvideo.bot.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HelpCommand {

    private final String HELP_TXT = "Приветствую!\n" +
            "Этот бот создан в учебных целях - \n" +
            "как результат совместной работы.\n\n" +
            "Выберите один из вариантов запроса:\n\n" +
            "/about_shelter - посмотреть информацию о приюте\n\n" +
            "/adopt_animal - как взять собаку из приюта\n\n" +
            "/send_report - прислать отчет о содержании питомца\n\n" +
            "/volunteer - пообщаться с волонтером\n\n" +
            "/help - просмотр краткой справки по боту";
}
