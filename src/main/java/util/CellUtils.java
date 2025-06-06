package util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import static org.apache.poi.ss.util.SheetUtil.getCell;

public class CellUtils {
    public static boolean stringCompare(Cell cell, String s) {
        if ((cell.getCellType() == CellType.STRING)
                && (s.equalsIgnoreCase(cell.getStringCellValue().trim()))){
            return true;
        }

        return false;
    }

    public static String getValueAt(Row row, Cell cell, int offset) {
        try {
            return row.getCell(cell.getColumnIndex() + offset).getStringCellValue();
        } catch (Exception e) {
            String resultado = String.valueOf(row.getCell(cell.getColumnIndex() + offset).getNumericCellValue());

            if (resultado.endsWith(".0")) {
                resultado = resultado.substring(0, resultado.length() - 2);
            }

            return resultado;
        }
    }

    public static String whenEqualsReturnAt(Row row, Cell cell, String s, int offset) {
        if(CellUtils.stringCompare(cell, s)) {
             return getValueAt(row, cell, offset);
        }
        return null;
    }


}
