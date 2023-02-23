package skypro.learn.tg_botfromvideo.bot.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AboutShelterCommand {

    private final String ABOUT_SHELTER_MENU_TXT = """
            Здесь Вы можете подробнее узнать о нашем приюте.
            Наша цель - найти доброго и заботливого хозяина каждому питомцу!

            Выберите один из вариантов запроса:

            /shelters_address - адрес приюта, расписание работы, схема проезда

            /safety_measures - общие рекомендации по технике безопасности на территории приюта
            
            /register_user - зарегистрировать пользователя

            /volunteer - пообщаться с волонтером""";
}
