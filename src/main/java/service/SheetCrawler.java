package service;

import lombok.Getter;
import model.Meal;
import model.Week;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.transform.Result;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.ExcelRules.*;
import static util.StringUtils.capitalizeName;

public class SheetCrawler {

    private static SheetCrawler instance;

    private FileInputStream fis;
    private Workbook workbook;
    private Sheet sheet;

    private int week = 1;
    private String clientName;
    private String weeklyKit;
    private int weekAmount;
    private String mealsAmount;
    private String delivery;
    private String mealType;
    private List<Week> meals;

    @Getter
    private StringBuffer resultText;

    private SheetCrawler() {}

    public static SheetCrawler getInstance() {
        if (instance == null) {
            instance = new SheetCrawler();
        }
        return instance;
    }

    private void crawl() {
        clientName = getClient(sheet);
        weeklyKit = getKitSemanal(sheet);
        weekAmount = getWeekAmount(sheet);
        mealsAmount = getTotalDeRefeicoes(sheet);
        meals = getMealList(sheet, weekAmount);
    }

    public void initializeCrawler(File file) {
        try {
            this.fis = new FileInputStream(file);
            this.workbook = new XSSFWorkbook(fis);
            this.sheet = workbook.getSheetAt(0);
            this.week = 1;
            this.clientName = "";
            this.weeklyKit = "";
            this.weekAmount = 1;
            this.mealsAmount = "";
            this.delivery = "11/06 - quarta pela manhã";
            this.mealType = "Almoço";
            this.meals = new ArrayList<>();
            this.resultText = new StringBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateTextResult() {
        if (sheet == null) {
            throw new RuntimeException("Insira uma planilha válida");
        }

        crawl();

        resultText.append("*Orçamento personalizado* \n");

        resultText.append("*Cliente:* ");
        resultText.append(capitalizeName(clientName));
        resultText.append("\n");

        resultText.append("*Kit semanal:* ");
        resultText.append(weeklyKit);
        resultText.append("\n");

        resultText.append("*Semanas:* ");
        resultText.append(weekAmount);
        resultText.append("\n");

        resultText.append("*Entrega:* ");
        resultText.append(delivery);
        resultText.append("\n");

        int semana = 1;
        for (Week week : meals) {
            resultText.append("*Semana ");
            resultText.append(semana++);
            resultText.append("* \n");

            if (!week.getLunchList().isEmpty()) {
                resultText.append("Almoço - ");
                resultText.append(week.getLuncQnt());
                resultText.append(" refeições\n");

                for (Meal meal : week.getLunchList()) {
                    resultText.append(meal.toString()).append("\n");
                }
            }

            if (!week.getDinnerList().isEmpty()) {
                resultText.append("Jantar - ");
                resultText.append(week.getDinnerQnt());
                resultText.append(" refeições\n");

                for (Meal meal : week.getDinnerList()) {
                    resultText.append(meal.toString()).append("\n");
                }
            }

            resultText.append("\n"); // separador entre semanas
        }

    }
}
