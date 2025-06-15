package service;

import lombok.Getter;
import lombok.Setter;
import model.Meal;
import model.PaymentValues;
import model.Week;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
    @Setter
    private String delivery = "";
    private PaymentValues paymentValues;

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
        weeklyKit = getWeeklyKit(sheet);
        weekAmount = getWeekAmount(sheet);
        mealsAmount = getMealsAmount(sheet);
        meals = getMealList(sheet, weekAmount);
        paymentValues = getPaymentValues(sheet);
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
        resultText.append("\n");

        resultText.append("Obs.: Todos os acompanhamentos inclusos, exceto salada crua. Variamos de acordo com seu plano. \n");
        resultText.append("\n");
        resultText.append("-----\n");

        int semana = 1;
        for (Week week : meals) {
            resultText.append("*Semana ").append(semana++).append("*\n\n");

            if (!week.getLunchList().isEmpty()) {
                resultText.append("*Almoço* - ");
                resultText.append(week.getLuncQnt()).append(" refeições\n");

                for (Meal meal : week.getLunchList()) {
                    resultText.append(meal.toString()).append("\n");
                }
                resultText.append("\n");
            }

            if (!week.getDinnerList().isEmpty()) {
                resultText.append("*Jantar* - ");
                resultText.append(week.getDinnerQnt()).append(" refeições\n");

                for (Meal meal : week.getDinnerList()) {
                    resultText.append(meal.toString()).append("\n");
                }
                resultText.append("\n");
            }

            resultText.append("-----\n");
        }

        resultText.append(paymentValues.getTotalMonthly());
        resultText.append("\n+\n");
        resultText.append(paymentValues.getDeliveryTax());
        resultText.append(" (frete)\n");
        resultText.append("=\n");
        resultText.append(paymentValues.getTotalWithDelivery());
        resultText.append("\n");
        resultText.append("\n");
        resultText.append("Forma de pagamento: \n");
        resultText.append("- *Crédito à vista via link de pagamento:* ");
        resultText.append(paymentValues.getTotalWithDelivery());
        resultText.append(paymentValues.getCardDiscount());
        resultText.append("\n");
        resultText.append("\n");
        resultText.append("- *PIX:* ");
        resultText.append(paymentValues.getDiscountedPixValue());
        resultText.append(paymentValues.getPixDiscount());
        resultText.append("\n");
        resultText.append("\n");

        resultText.append("*PIX CNPJ: 45372421000181*\n");
        resultText.append("Obs.: Pedido será confirmado mediante pagamento antecipado");

    }
}
