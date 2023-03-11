package skypro.learn.tg_bot.bot.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Data
@NoArgsConstructor
public class AddCommandsToBotMenu {

    private List<BotCommand> listOfCommands;

    /**
     * Метод добавляет пункты в Меню телеграмм-бота.
     * В текущей версии приложения не реализовано.
     * @return список (List) команд
     */

    //Категорий пользователей 5 (4 + 1):
    //- первичный пользователь - по которому еще нет выбранного типа приюта.
    //- просто пользователь - он уже выбрал тип приюта и его chatId сохранен в БД.
    //- зарегистрированный пользователь - RegisteredUser - он успешно ввел имя, фамилию, телефон и email.
    //- "усыновитель" - тот, кто взял питомца из приюта, должен отправлять Отчет по питомцу
    //- администратор (волонтер)

    public List<BotCommand> addCommandsForNewUser(){
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "начать общение"));
        listOfCommands.add(new BotCommand("/help", "просмотр краткой справки по боту"));
        return listOfCommands;
    }

    public List<BotCommand> addCommandsForDogShelterUsers(){
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/dog_shelter_address", "адрес и местонахождение приюта"));
        listOfCommands.add(new BotCommand("/adopt_dog", "что нужно знать, чтобы взять собаку из приюта"));
        listOfCommands.add(new BotCommand("/safety_measures_in_shelter", "как вести себя на территории приюта"));
        listOfCommands.add(new BotCommand("/register_user_for_adopt_dog", "регистрация пользователя"));
        listOfCommands.add(new BotCommand("/dogs_volunteer", "написать волонтеру о проблеме/вопросе"));
        return listOfCommands;
    }

    public List<BotCommand> addCommandsForCatShelterUsers(){
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/сat_shelter_address", "адрес и местонахождение приюта"));
        listOfCommands.add(new BotCommand("/adopt_cat", "что нужно знать, чтобы взять кошку из приюта"));
        listOfCommands.add(new BotCommand("/safety_measures_in_shelter", "как вести себя на территории приюта"));
        listOfCommands.add(new BotCommand("/register_user_for_adopt_cat", "регистрация пользователя"));
        listOfCommands.add(new BotCommand("/cats_volunteer", "написать волонтеру о проблеме/вопросе"));
        return listOfCommands;
    }

    public List<BotCommand> addCommandsForRegisteredUsersAfterAdoptDog(){
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/improvement_home_for_puppy", "список рекомендаций по обустройству дома для щенка"));
        listOfCommands.add(new BotCommand("/impr_home_for_adult_dog", "как обустроить дом для взрослой собаки"));
        listOfCommands.add(new BotCommand("/impr_home_for_disabled_dog", "как обустроить дом для собаки с ограниченными возможностями"));
        listOfCommands.add(new BotCommand("/initial_appeal_with_dog", "советы кинолога по первичному обращению с собакой"));
        listOfCommands.add(new BotCommand("/contacts_of_cynologists", "контакты опытных кинологов"));
        listOfCommands.add(new BotCommand("/dogs_volunteer", "написать волонтеру о проблеме/вопросе"));
        listOfCommands.add(new BotCommand("/send_report", "отправить отчет по питомцу"));
        return listOfCommands;
    }

    public List<BotCommand> addCommandsForRegisteredUsersAfterAdoptCat(){
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/improvement_home_for_kitty", "список рекомендаций по обустройству дома для котенка"));
        listOfCommands.add(new BotCommand("/impr_home_for_adult_cat", "как обустроить дом для взрослой кошки"));
        listOfCommands.add(new BotCommand("/impr_home_for_disabled_cat", "как обустроить дом для кошки с ограниченными возможностями"));
        listOfCommands.add(new BotCommand("/initial_appeal_with_dog", "советы кинолога по первичному обращению с собакой"));
        listOfCommands.add(new BotCommand("/cats_volunteer", "написать волонтеру о проблеме/вопросе"));
        listOfCommands.add(new BotCommand("/send_report", "отправить отчет по питомцу"));
        return listOfCommands;
    }

    public List<BotCommand> addCommandsForAdmin(){
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/view_questions", "посмотреть вопросы пользователей"));
        listOfCommands.add(new BotCommand("/add_pet_to_user", "добавить питомца к портфолио пользователя"));
        //listOfCommands.add(new BotCommand("/view_actual_reports", "посмотреть свежие/актуальные отчеты"));
        //listOfCommands.add(new BotCommand("/view_problems_with_reports", "посмотреть проблемы с отчетами"));
        return listOfCommands;
    }
}