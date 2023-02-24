package skypro.learn.tg_botfromvideo.bot.commands.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
public class StartCommand {

    private final String START_COMMAND_TXT = """
            Пожалуйста, выберите приют (собачий или кошачий),
            информацию о котором Вам интересна.
            /dog_shelter - выбор приюта для собак
            /cat_shelter - выбор приюта для кошек""";
}
