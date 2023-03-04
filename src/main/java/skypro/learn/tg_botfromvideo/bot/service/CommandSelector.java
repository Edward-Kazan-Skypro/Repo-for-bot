package skypro.learn.tg_botfromvideo.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class CommandSelector {

    private String pathToFiles = "src/main/resources/txt_files_for_menu";

    /**
     * Метод для обработки сообщения от пользователя телеграмм-бота.
     * В качестве аргумента принимает текст.
     *
     * @param inputText
     * @return
     */

    public String selectBotCommand(String inputText) {
        //Отдельная обработка кодового слова для входа для администратора
        if (inputText.equals("/кодовое_слово")){
            return "Добро пожаловать, Администратор!";
        }
        //Обычная обработка команд
        return readTextFromFile(inputText);
    }

       /* switch (inputText) {
            case "/dog_shelter":
                saveSelectedShelter(inputText, visitor);
                fileName = "/dog_shelter";
                return readTextFromFile(fileName);
            case "/cat_shelter":
                saveSelectedShelter(inputText, visitor);
                fileName = "/cat_shelter";
                return readTextFromFile(fileName);

            // !!!! Надо создать файлы с такими же названиями и заполнить их содержанием !!!!

            //Команды, которые отображаются после выбора /dog_shelter
            case "/dog_shelter_address":
            case "/adopt_dog":
                //saveLastCommand(inputText, visitor) - запоминаем выбранный пункт меню
                //м.б. потом пойму как правильно это использовать
                saveLastCommand(inputText, visitor);
                fileName = "/adopt_dog";
                return readTextFromFile(fileName);
            case "/safety_measures_in_dog_shelter":
            case "/register_user_for_adopt_dog":
                fileName = "/register_user_for_adopt_dog";
                return readTextFromFile(fileName);
            case "/register_user":
                fileName = "/register_user";
                return readTextFromFile(fileName);
            case "/registration_user_complete":
                fileName = "/registration_user_complete";
                return readTextFromFile(fileName);
            case "/registration_user_error":
                fileName = "/registration_user_error";
                return readTextFromFile(fileName);

            case "/dogs_volunteer":
                fileName = "/dogs_volunteer";
                return readTextFromFile(fileName);
            case "/yes":



                //Команды, которые отображаются после выбора /adopt_dog
            case "/meeting_with_dog":
            case "/documents_for_adopting_dog":
            case "/recommendations_for_transporting":
            case "/improvement_home_for_puppy":
            case "/improvement_home_for_adult_dog":
            case "/improvement_home_for_disabled_dog":
            case "/initial_appeal_with_dog":
            case "/contacts_of_cynologists":
            case "/reasons_for_rejection":
                //case"/dogs_volunteer":

                //Команды, которые отображаются после выбора /cat_shelter
            case "/сat_shelter_address":
            case "/adopt_cat":
                saveLastCommand(inputText, visitor);
                fileName = "/adopt_cat";
                return readTextFromFile(fileName);
            case "/safety_measures_in_cat_shelter":
            case "/register_user_for_adopt_cat":
            case "/cats_volunteer":

                //Команды, которые отображаются после выбора /adopt_cat
            case "/meeting_with_cat":
            case "/documents_for_adopting_cat":
            case "/recommendations_for_transporting_cat":
            case "/improvement_home_for_kitty":
            case "/improvement_home_for_adult_cat":
            case "/improvement_home_for_disabled_cat":
            case "/reasons_for_rejection_cat":
                //case "/cats_volunteer":


            case "/send_report":

            case "/help":
                saveLastCommand(inputText, visitor);
                fileName = "/help";
                return readTextFromFile(fileName);
            case "/start":
                String welcomeText = "Здравствуйте " + visitor.getNameInChat() + "!\n\n";
                fileName = "/start";
                return welcomeText + readTextFromFile(fileName);
            default:
                return "Обработка запроса еще не реализована!";
        }
    }*/

    /**
     * Метод для чтения текстовых данных из файла.
     *
     * @param fileName
     * @return
     */
    private String readTextFromFile(String fileName) {
        StringBuilder content = new StringBuilder();
        String line;
        String pathWithName = pathToFiles + fileName + ".txt";
        try (BufferedReader br = new BufferedReader(new FileReader(pathWithName))) {
            while ((line = br.readLine()) != null) content.append(line).append("\n");
        } catch (IOException e) {
            log.error("Ошибка чтения файла! " + e.getMessage());
        }
        return new String(content);
    }
}
