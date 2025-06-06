import model.Meal;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static util.ExcelRules.*;
import static util.StringUtils.capitalizeName;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("src/main/resources/test.xlsx");
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        int week = 1;
        String clientName = "";
        String kitSemanal = "";
        String semanas = "";
        String totalRefeicoes = "";
        String entrega = "11/06 - quarta pela manhã";
        String tipoDeRefeicao = "Almoço";
        List<Meal> meals = new ArrayList<>();
        StringBuffer resultTxt = new StringBuffer();

        clientName = getClient(sheet);
        kitSemanal = getKitSemanal(sheet);
        semanas = getSemanas(sheet);
        totalRefeicoes = getTotalDeRefeicoes(sheet);
        meals = getMealList(sheet, week);

        resultTxt.append("Orçamento personalizado \n");

        resultTxt.append("Cliente: ");
        resultTxt.append(capitalizeName(clientName));
        resultTxt.append("\n");

        resultTxt.append("Kit semanal: ");
        resultTxt.append(kitSemanal);
        resultTxt.append("\n");

        resultTxt.append("Semanas: ");
        resultTxt.append(semanas);
        resultTxt.append("\n");

        resultTxt.append("Entrega: ");
        resultTxt.append(entrega);
        resultTxt.append("\n");

        resultTxt.append(tipoDeRefeicao);
        resultTxt.append(" - ");
        resultTxt.append(totalRefeicoes);
        resultTxt.append(" Refeições");
        resultTxt.append("\n");

        for(Meal meal : meals) {
            if(!meal.getName().isBlank()) {
                resultTxt.append(meal.toString());
                resultTxt.append("\n");
            }
        }

        System.out.println(resultTxt);
    }
}
