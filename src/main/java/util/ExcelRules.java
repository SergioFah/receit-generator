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
    private static final int CLIENT_Y = 1;
    private static final int CLIENT_X = 2;

    private static final int WEEKLY_KIT_Y = 2;
    private static final int WEEKLY_KIT_X = 2;

    private static final int WEEKLY_AMOUNT_Y = 3;
    private static final int WEEKLY_AMOUNT_X = 2;

    private static final int MEALS_AMOUNT_Y = 4;
    private static final int MEALS_AMOUNT_X = 2;

    private static final int MAX_INGREDIENT_ITEMS = 14;

    private static final int[] MEALS_ROW_INDEX = {9, 30, 51, 72, 93, 114};
    private static final int[] WEEK_COLUMN_INDEX = {1, 16, 29, 42};

    public static String getClient(Sheet sheet) {
        Row row = sheet.getRow(CLIENT_Y);
        Cell cell = row.getCell(CLIENT_X);

        try {
            return cell.getStringCellValue();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o nome do cliente");
        }
    }

    public static String getWeeklyKit(Sheet sheet) {
        Row row = sheet.getRow(WEEKLY_KIT_Y);
        Cell cell = row.getCell(WEEKLY_KIT_X);

        try {
            String result = String.valueOf(cell.getNumericCellValue());
            return result.substring(0, result.length() - 2);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o valor do kit semanal");
        }
    }

    public static int getWeekAmount(Sheet sheet) {
        Row row = sheet.getRow(WEEKLY_AMOUNT_Y);
        Cell cell = row.getCell(WEEKLY_AMOUNT_X);

        try {
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter as semanas");
        }
    }

    public static String getMealsAmount(Sheet sheet) {
        Row row = sheet.getRow(MEALS_AMOUNT_Y);
        Cell cell = row.getCell(MEALS_AMOUNT_X);

        try {
            String result = String.valueOf(cell.getNumericCellValue());
            return result.substring(0, result.length() - 2);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível obter o total de refeições");
        }
    }

    public static Meal getMealAtRow(Sheet sheet, int row, int week) {
        int rowOffset = row + 5;
        StringBuffer mealNameAux = new StringBuffer();
        Meal meal = new Meal();
        Cell auxCell;

        meal.setQnt(sheet.getRow(rowOffset).getCell(WEEK_COLUMN_INDEX[week - 1] + 10).getNumericCellValue());
        meal.setPrice(sheet.getRow(rowOffset - 4).getCell(WEEK_COLUMN_INDEX[week - 1] + 10).getNumericCellValue());
        meal.setTipo(sheet.getRow(rowOffset - 6).getCell(WEEK_COLUMN_INDEX[week - 1] + 1).getStringCellValue());

        for (int i = 0; i < MAX_INGREDIENT_ITEMS; i++) {
            auxCell = sheet.getRow(i + rowOffset).getCell(WEEK_COLUMN_INDEX[week -1]);
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

            for (int row : MEALS_ROW_INDEX) {
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
            PaymentValues paymentValues = null;

            paymentValues = new PaymentValues();

            paymentValues.setTotalMonthly(sheet.getRow(151).getCell(2).getNumericCellValue());
            paymentValues.setDeliveryTax(sheet.getRow(153).getCell(2).getNumericCellValue());

            paymentValues.setTotalWithDelivery(sheet.getRow(152).getCell(3).getNumericCellValue());
            paymentValues.setDiscountedPixValue(sheet.getRow(172).getCell(3).getNumericCellValue());
            paymentValues.setDiscountedCardValue(sheet.getRow(172).getCell(2).getNumericCellValue());

            if (paymentValues.getDiscountedCardValue().equals("R$ 0,00")  || paymentValues.getDiscountedPixValue().equals("R$ 0,00")) {
                paymentValues.setDiscountedCardValue(sheet.getRow(171).getCell(2).getNumericCellValue());
                paymentValues.setDiscountedPixValue(sheet.getRow(171).getCell(3).getNumericCellValue());
            }

            paymentValues.setCardDiscount(sheet.getRow(168).getCell(2).getNumericCellValue());
            paymentValues.setPixDiscount(sheet.getRow(168).getCell(3).getNumericCellValue());

            return paymentValues;
        }
    }
