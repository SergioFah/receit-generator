package util;

import model.Meal;
import model.PaymentValues;
import model.Week;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

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

    public static int getWeekAmount(Sheet sheet) {
        Row row = sheet.getRow(3);
        Cell cell = row.getCell(2);

        try {
            return (int) cell.getNumericCellValue();
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

    public static Meal getMealAtRow(Sheet sheet, int row, int week) {
        int columnOffset = week > 1 ? ((week * 12) + 1) : 1;
        int rowOffset = row + 5;
        StringBuffer mealNameAux = new StringBuffer();
        Meal meal = new Meal();
        Cell auxCell;

        meal.setQnt(sheet.getRow(rowOffset).getCell(columnOffset + 10).getNumericCellValue());
        meal.setPrice(sheet.getRow(rowOffset - 4).getCell(columnOffset + 10).getNumericCellValue());
        meal.setTipo(sheet.getRow(rowOffset - 6).getCell(columnOffset + 1).getStringCellValue());

        for (int i = 0; i < 14; i++) {
            auxCell = sheet.getRow(i + rowOffset).getCell(columnOffset);
            if(!auxCell.getCellType().equals(CellType.BLANK)) {
                if (!auxCell.getStringCellValue().isBlank()) {
                    mealNameAux.append(mealItemFormatter(auxCell.getStringCellValue()));
                    if (i == 0) {
                        mealNameAux.append(" com ");
                    } else {
                        mealNameAux.append(", ");
                    }
                }
            }
        }

        if (mealNameAux.toString().isBlank()) {
            return null;
        }

        meal.setName(mealDescFormatter(mealNameAux.toString()));

        return meal;
    }

    public static List<Week> getMealList(Sheet sheet, int weekAmount) {
        List<Week> weeks = new ArrayList<>();

        for (int i = 1; i <= weekAmount; i++) {
            Week week = new Week();
            List<Meal> lunchList = new ArrayList<>();
            List<Meal> dinnerList = new ArrayList<>();
            int lunchQnt = 0;
            int dinnerQnt = 0;

            int[] rowIndexes = {9, 30, 51, 72, 93, 114};

            for (int row : rowIndexes) {
                Meal meal = getMealAtRow(sheet, row, i);

                if (meal == null) {
                    continue;
                }

                if (meal.getTipo().equals("ALMOÇO")) {
                    lunchQnt+=meal.getQnt();
                    lunchList.add(meal);
                } else {
                    dinnerQnt+=meal.getQnt();
                    dinnerList.add(meal);
                }
            }
            week.setLuncQnt(lunchQnt);
            week.setDinnerQnt(dinnerQnt);
            week.setLunchList(lunchList);
            week.setDinnerList(dinnerList);
            weeks.add(week);
        }

        return weeks;
        }

        public static PaymentValues getPaymentValues(Sheet sheet) {
            PaymentValues paymentValues = new PaymentValues();

            paymentValues.setTotalMonthly(sheet.getRow(151).getCell(2).getNumericCellValue());
            paymentValues.setTotalWithDelivery(sheet.getRow(152).getCell(3).getNumericCellValue());
            paymentValues.setDeliveryTax(sheet.getRow(153).getCell(2).getNumericCellValue());
            paymentValues.setDiscountedPixValue(sheet.getRow(172).getCell(3).getNumericCellValue());

            return paymentValues;
        }
    }
