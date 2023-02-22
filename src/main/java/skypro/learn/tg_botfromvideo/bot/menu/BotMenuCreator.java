package skypro.learn.tg_botfromvideo.bot.menu;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@Data
@NoArgsConstructor
public class BotMenuCreator  {

    private static List<BotCommand> listOfCommands = new ArrayList<>();
    public static List<BotCommand> addCommandsToBotMenu (){
        listOfCommands.add(new BotCommand("/about_shelter", "приветствие пользователя"));
        listOfCommands.add(new BotCommand("/adopt_animal", "как взять собаку из приюта"));
        //listOfCommands.add(new BotCommand("/send_report", "прислать отчет о содержании питомца"));
        listOfCommands.add(new BotCommand("/volunteer", "пообщаться с волонтером"));
        listOfCommands.add(new BotCommand("/help", "просмотр краткой справки по боту"));
        return listOfCommands;
    }
}
