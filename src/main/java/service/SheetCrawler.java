package service;

import lombok.Getter;
import model.Meal;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.ExcelRules.*;
import static util.ExcelRules.getMealList;
import static util.ExcelRules.getTotalDeRefeicoes;
import static util.StringUtils.capitalizeName;

public class SheetCrawler {

    private static SheetCrawler instance;

    private FileInputStream fis;
    private Workbook workbook;
    private Sheet sheet;

    private int week = 1;
    private String clientName;
    private String weeklyKit;
    private String weeks;
    private String mealsAmount;
    private String delivery;
    private String mealType;
    private List<Meal> meals;

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
        weeks = getSemanas(sheet);
        mealsAmount = getTotalDeRefeicoes(sheet);
        meals = getMealList(sheet, week);
    }

    public void initializeCrawler(File file) {
        try {
            this.fis = new FileInputStream(file);
            this.workbook = new XSSFWorkbook(fis);
            this.sheet = workbook.getSheetAt(0);
            this.week = 1;
            this.clientName = "";
            this.weeklyKit = "";
            this.weeks = "";
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

        resultText.append("Orçamento personalizado \n");

        resultText.append("Cliente: ");
        resultText.append(capitalizeName(clientName));
        resultText.append("\n");

        resultText.append("Kit semanal: ");
        resultText.append(weeklyKit);
        resultText.append("\n");

        resultText.append("Semanas: ");
        resultText.append(weeks);
        resultText.append("\n");

        resultText.append("Entrega: ");
        resultText.append(delivery);
        resultText.append("\n");

        resultText.append(mealType);
        resultText.append(" - ");
        resultText.append(mealsAmount);
        resultText.append(" Refeições");
        resultText.append("\n");

        for(Meal meal : meals) {
            if(!meal.getName().isBlank()) {
                resultText.append(meal.toString());
                resultText.append("\n");
            }
        }
    }
}
