package util;

import model.Meal;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

import static util.CellUtils.getValueAt;
import static util.StringUtils.*;

public class ExcelRules {
    public static String getClient(Sheet sheet) {
        Row row = sheet.getRow(1);
        Cell cell = row.getCell(2);

        try {
            return cell.getStringCellValue();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o nome do cliente");
        }
    }

    public static String getKitSemanal(Sheet sheet) {
        Row row = sheet.getRow(2);
        Cell cell = row.getCell(2);

        try {
            String result = String.valueOf(cell.getNumericCellValue());
            return result.substring(0, result.length() - 2);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o valor do kit semanal");
        }
    }

    public static String getSemanas(Sheet sheet) {
        Row row = sheet.getRow(3);
        Cell cell = row.getCell(2);

        try {
            String result = String.valueOf(cell.getNumericCellValue());
            return result.substring(0, result.length() - 2);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o semanas");
        }
    }

    public static String getTotalDeRefeicoes(Sheet sheet) {
        Row row = sheet.getRow(4);
        Cell cell = row.getCell(2);

        try {
            String result = String.valueOf(cell.getNumericCellValue());
            return result.substring(0, result.length() - 2);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o semanas");
        }
    }

    public static List<Meal> getMealList(Sheet sheet, int week) {
        List<Meal> meals = new ArrayList<>();
        StringBuffer mealNameAux = new StringBuffer();
        week++;
        Meal auxMeal = new Meal();
        Cell auxCell;
        int count = 0;
        for (int i = 187; i < 246; i++) {
            auxCell = sheet.getRow(i).getCell(week);

            if (count == 0) {
                auxMeal.setQnt(auxCell.getNumericCellValue());
                count++;
                continue;
            }

            if(!auxCell.getCellType().equals(CellType.BLANK)) {
                if (!auxCell.getStringCellValue().isBlank()) {
                    mealNameAux.append(mealItemFormatter(auxCell.getStringCellValue()));
                    if (count == 1) {
                        mealNameAux.append(" com ");
                    } else {
                        mealNameAux.append(", ");
                    }
                    count++;
                    continue;
                }
            }

            if (count == 9) {
                auxMeal.setName(mealDescFormatter(mealNameAux.toString()));
                meals.add(auxMeal);
                auxMeal = new Meal();
                mealNameAux = new StringBuffer();
                count = 0;
                continue;
            }
            count++;
        }




        return meals;
    }

    private static String cleanEnding(String input) {
        input = input.trim();
        if (input.endsWith(",") || input.endsWith(" e") || input.endsWith(" com")) {
            return input.substring(0, input.lastIndexOf(" "));
        }
        return input;
    }

}
