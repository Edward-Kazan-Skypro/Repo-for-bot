package skypro.learn.tg_botfromvideo.bot.service;

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
public class BotMenuCreator  {

    private List<BotCommand> listOfCommands;

    /**
     * Метод добавляет пункты в Меню телеграмм-бота
     * @return список (List) команд
     */

    //Категорий пользователей 5 (4 + 1):
    //- первичный пользователь - по которому еще нет выбранного типа приюта.
    //- просто пользователь - он уже выбрал тип приюта и его chatId сохранен в БД.
    //- зарегистрированный пользователь - RegisteredUser - он успешно ввел имя, фамилию, телефон и email.
    //- "усыновитель" - тот, кто взял питомца из приюта, должен отправлять Отчет по питомцу
    //- администратор (волонтер)

    //Команды меню для первичного пользователя:
    // /start - здесь пользователь может выбрать тип приюта, информация по которому будет в дальнейшем доступна
    // /help - здесь рекомендация начать работу с команды /start
    //После того как пользователь нажал /start он без дополнительных уведомлений регистрируется в БД
    //При этом сохраняется его chatId, имя (как он указал в чате) и тип приюта.
    //В зависимости от типа приюта и формируется список команд в отображаемом Меню
    //т.к. для разных приютов - разная отображаемая информация.
    public List<BotCommand> addCommandsForNewUser(){
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "начать общение"));
        listOfCommands.add(new BotCommand("/help", "просмотр краткой справки по боту"));
        return listOfCommands;
    }

    //Команды меню для пользователя, выбравшего приют для собак:
    // /dog_shelter_address - адрес и местонахождение приюта, как доехать
    // /adopt_dog - что нужно знать, чтобы взять собаку из приюта
    // /safety_measures_in_dog_shelter - правила безопасности поведения на территории приюта
    // /register_user_for_adopt_dog - регистрация пользователя
    // /dogs_volunteer - написать волонтеру о проблеме/вопросе
    //Учитывая, что все эти команды в Меню, то переход из этих информационных сообщений не нужен
    //кроме выбора /adopt_dog - там снова отображается список команд.
    //Пользователь просто снова нажимает Меню и выбирает следующий пункт этого Меню.
    public List<BotCommand> addCommandsForDogShelterUsers(){
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/dog_shelter_address", "адрес и местонахождение приюта"));
        listOfCommands.add(new BotCommand("/adopt_dog", "что нужно знать, чтобы взять собаку из приюта"));
        listOfCommands.add(new BotCommand("/safety_measures_in_shelter", "как вести себя на территории приюта"));
        listOfCommands.add(new BotCommand("/register_user_for_adopt_dog", "регистрация пользователя"));
        listOfCommands.add(new BotCommand("/dogs_volunteer", "написать волонтеру о проблеме/вопросе"));
        return listOfCommands;
    }

    //Команды меню для пользователя, выбравшего приют для кошек:
    // /сat_shelter_address - адрес и местонахождение приюта, как доехать
    // /adopt_cat - что нужно знать, чтобы взять кошку из приюта
    // /safety_measures_in_cat_shelter - правила безопасности поведения на территории приюта
    // /register_user_for_adopt_cat - регистрация пользователя
    // /cats_volunteer - написать волонтеру о проблеме/вопросе
    //Учитывая, что все эти команды в Меню, то переход из этих информационных сообщений не нужен
    //кроме выбора /adopt_cat - там снова отображается список команд.
    //Пользователь просто снова нажимает Меню и выбирает следующий пункт этого Меню.
    public List<BotCommand> addCommandsForCatShelterUsers(){
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/сat_shelter_address", "адрес и местонахождение приюта"));
        listOfCommands.add(new BotCommand("/adopt_cat", "что нужно знать, чтобы взять кошку из приюта"));
        listOfCommands.add(new BotCommand("/safety_measures_in_shelter", "как вести себя на территории приюта"));
        listOfCommands.add(new BotCommand("/register_user_for_adopt_cat", "регистрация пользователя"));
        listOfCommands.add(new BotCommand("/cats_volunteer", "написать волонтеру о проблеме/вопросе"));
        return listOfCommands;
    }

    //Команды меню для зарегистрированного пользователя, взявшего собаку.
    //Вот здесь не ясно до конца что ему будет важно.
    //С одной стороны он уже взял питомца и вряд ли ему нужна информация как проехать и все такое.
    //Но наверно будет важна информация по уходу за собакой.
    // /improvement_home_for_puppy - список рекомендаций по обустройству дома для щенка
    // /improvement_home_for_adult_dog - список рекомендаций по обустройству дома для взрослой собаки
    // /improvement_home_for_disabled_dog - список рекомендаций по обустройству дома для собаки с ограниченными возможностями (зрение, передвижение)
    // /initial_appeal_with_dog - советы кинолога по первичному обращению с собакой
    // /contacts_of_cynologists - контакты опытных кинологов
    // /dogs_volunteer - написать волонтеру о проблеме/вопросе
    // /send_report - отправить отчет по питомцу
    //Учитывая, что все эти команды в Меню, то переход из этих информационных сообщений не нужен.
    //Пользователь просто снова нажимает Меню и выбирает следующий пункт этого Меню.
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

    //Команды меню для зарегистрированного пользователя, взявшего кошку.
    //С одной стороны он уже взял питомца и вряд ли ему нужна информация как проехать и все такое.
    //Но наверно будет важна информация по уходу за кошкой.
    // /improvement_home_for_kitty - список рекомендаций по обустройству дома для котенка
    // /improvement_home_for_adult_cat - список рекомендаций по обустройству дома для взрослой кошки
    // /improvement_home_for_disabled_cat - список рекомендаций по обустройству дома для кошки с ограниченными возможностями (зрение, передвижение)
    // /cats_volunteer - написать волонтеру о проблеме/вопросе
    // /send_report - отправить отчет по питомцу
    //Учитывая, что все эти команды в Меню, то переход из этих информационных сообщений не нужен.
    //Пользователь просто снова нажимает Меню и выбирает следующий пункт этого Меню.
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

    //Команды меню для админа.
    //Акцент на отчетность, просмотр сообщений с вопросами от пользователей и уведомление пользователя(-ей)
    //На данный момент список возможных пунктов меню следующий (! требует обсуждения и доработки! ):
    // /view_questions - посмотреть вопросы пользователей
    // /view_actual_reports - посмотреть свежие/актуальные отчеты
    // /view_problems_with_reports - посмотреть проблемы с отчетами
    // /add_pet_to_user - добавить питомца к портфолио пользователя
    public List<BotCommand> addCommandsForAdmin(){
        listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/view_questions", "посмотреть вопросы пользователей"));
        listOfCommands.add(new BotCommand("/add_pet_to_user", "добавить питомца к портфолио пользователя"));
        //listOfCommands.add(new BotCommand("/view_actual_reports", "посмотреть свежие/актуальные отчеты"));
        //listOfCommands.add(new BotCommand("/view_problems_with_reports", "посмотреть проблемы с отчетами"));
        return listOfCommands;
    }
}
