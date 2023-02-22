package skypro.learn.tg_botfromvideo.bot.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdoptAnimalCommand {
    private final String ADOPT_ANIMAL_TXT = "Приветствую!\n" +
            "Здесь Вы можете узнать как взять питомца из приюта.\n\n" +
            "Выберите один из вариантов запроса:\n\n" +
            "/recommendations_for_keeping - рекомендации по содержанию\n\n" +
            "/adopting_steps - как взять собаку из приюта (по шагам)\n\n" +
            "/view_pets - посмотреть наших питомцев\n\n" +
            "/register_user - зарегистрироваться\n\n" +
            "/volunteer - пообщаться с волонтером\n\n" +
            "/help - просмотр краткой справки по боту";
}
