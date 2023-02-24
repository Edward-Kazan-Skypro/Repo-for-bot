package skypro.learn.tg_botfromvideo.bot.service;

import lombok.extern.slf4j.Slf4j;
import skypro.learn.tg_botfromvideo.bot.commands.common.StartCommand;
import skypro.learn.tg_botfromvideo.bot.commands.dog_shelter.AboutDogShelterCommand;
import skypro.learn.tg_botfromvideo.bot.commands.dog_shelter.AdoptDogCommand;
import skypro.learn.tg_botfromvideo.model.Visitor;
import skypro.learn.tg_botfromvideo.repository.VisitorsRepository;
import java.io.*;

@Slf4j
public class CommandSelector {

    private String pathToFiles = "src/main/resources/txt_files_for_menu";

    final VisitorsRepository visitorsRepository;

    public CommandSelector(VisitorsRepository visitorsRepository) {
        this.visitorsRepository = visitorsRepository;
    }

    /**
     * Метод для обработки сообщения от пользователя телеграмм-бота.
     * В качестве аргумента принимает текст.
     * @param inputText
     * @return
     */

    public String selectBotCommand(String inputText, Visitor visitor) {

            switch (inputText) {
                case "/dog_shelter":
                    saveSelectedShelter(inputText, visitor);
                    return "dog_shelter selected";
                case "/cat_shelter":
                    saveSelectedShelter(inputText, visitor);
                    return "cat_shelter selected";

                //Команды для отображения информации по приюту для собак

                case "/about_dog_shelter":
                    saveLastCommand(inputText, visitor);
                    return new AboutDogShelterCommand().getABOUT_DOG_SHELTER_MENU_TXT();

                case "/dog_shelter_address":
                case "/safety_measures":
                case "/register_user":
                case "/volunteer":

                case "/adopt_dog":
                    return new AdoptDogCommand().getADOPT_ANIMAL_MENU_TXT();

                case "/meeting_with_dog":
                case "/documents_for_adopting_dog":
                case "/recommendations_for_transporting":
                case "/improvement_home_for_puppy":
                case "/improvement_home_for_adult_dog":
                case "/improvement_home_for_disabled_dog":
                case "/initial_appeal_with_dog":
                case "/contacts_of_cynologists":
                case "/reasons_for_rejection":

                //Команды для отображения информации по приюту для кошек


                case "/send_report":

                case "/help":
                    saveLastCommand(inputText, visitor);
                    String fileName = "/help";
                    return readTextFromFile(fileName);
                case "/start":
                    String answer = "Здравствуйте " + visitor.getName() + "!\n\n " +
                            "Перед Вами информационный бот, который поможет получить сведения:\n" +
                            "- о выбранном приюте (для кошек или для собак)\n" +
                            "- как взять питомца из приюта\n" +
                            "- как отправить отчет о взятом из приюта питомце\n" +
                            "- как позвать на помощь волонтера - если нужна дополнительная информация\n\n" +
                            "Итак, начнем наше общение!\n\n";
                    answer += new StartCommand().getSTART_COMMAND_TXT();

                    return answer;
                default:
                    return "Обработка запроса еще не реализована!";
            }
    }

    private String readTextFromFile(String fileName){
        StringBuilder content = new StringBuilder();
        String line;
        String pathWithName = pathToFiles+fileName + ".txt";
        try(BufferedReader br = new BufferedReader(new FileReader(pathWithName)))
        {
            while((line = br.readLine()) != null){
                content.append(line);
            }
        }
        catch(IOException e){
            log.error("Ошибка чтения файла! " + e.getMessage());
        }
        return new String(content);
    }

    private void saveLastCommand(String inputText, Visitor visitor){
        visitor.setLastCommand(inputText);
        visitorsRepository.save(visitor);
    }

    private void saveSelectedShelter(String inputText, Visitor visitor){
        visitor.setVisitedShelter(inputText);
        visitorsRepository.save(visitor);
    }
}
