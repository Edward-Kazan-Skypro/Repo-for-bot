package skypro.learn.tg_botfromvideo.bot.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AboutShelterCommand {

    private final String ABOUT_SHELTER_TXT = "Здесь Вы можете подробнее узнать о нашем приюте.\n" +
            "Наша цель - найти доброго и заботливого хозяина каждому питомцу! \n\n" +
            "Выберите один из вариантов запроса:\n\n" +
            "/shelters_address - адрес приюта/как нас найти\n\n" +
            "/view_pets - посмотреть наших питомцев\n\n" +
            "/volunteer - пообщаться с волонтером\n\n" +
            "/help - просмотр краткой справки по боту";
}
