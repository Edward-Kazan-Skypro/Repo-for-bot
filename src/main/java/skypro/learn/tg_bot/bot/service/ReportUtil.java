package skypro.learn.tg_bot.bot.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import skypro.learn.tg_bot.model.Report;
import skypro.learn.tg_bot.repository.ReportsRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Класс для обработки отчетов пользователей
 */

@Slf4j
@Component
public class ReportUtil {

    final private ReportsRepository reportsRepository;

    public ReportUtil(ReportsRepository reportsRepository) {
        this.reportsRepository = reportsRepository;
    }

    public String viewAllReports() {
        List<Report> listOfReports;
        StringBuilder resultListForView = new StringBuilder();
        listOfReports = (List<Report>) reportsRepository.findAll();
        for (Report report : listOfReports) {
            resultListForView.append(report);
        }
        if (resultListForView.isEmpty()){
            resultListForView.append("Список отчетов пуст");
        }
        return resultListForView.toString();
    }

    public String viewCurrentReports(){
        List<Report> listOfReports;
        StringBuilder resultListForView = new StringBuilder();
        listOfReports = (List<Report>) reportsRepository.findAll();
        LocalDate currentDate = LocalDate.now();
        for (Report report : listOfReports) {
            if (report.getDateReport().equals(currentDate)){
                resultListForView.append(report);
            }
        }
        if (resultListForView.isEmpty()){
            resultListForView.append("Список отчетов пуст");
        }
        return resultListForView.toString();
    }
}






