package util;

public class StringUtils {
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    public static String capitalizeName(String str) {
        if (str == null || str.isEmpty()) return str;

        String[] palavras = str.toLowerCase().split(" ");
        StringBuilder resultado = new StringBuilder();

        for (String palavra : palavras) {
            if (!palavra.isBlank()) {
                resultado.append(palavra.substring(0, 1).toUpperCase());
                resultado.append(palavra.substring(1));
                resultado.append(" ");
            }
        }

        return resultado.toString().trim();
    }

    public static String mealItemFormatter(String input) {
        if (input == null) return "";

        return input
                .replaceFirst("^FT\\s*", "")
                .replaceAll("\\s*\\d+\\s*", " ")
                .replaceAll("\\s+", " ")
                .replace("CONG VAPOR", "no vapor")
                .trim();
    }

    public static String mealDescFormatter(String input) {
        if (input == null || input.isBlank()) return "";

        input = input.trim();

        if (input.endsWith(",")) {
            input = input.substring(0, input.length() - 1).trim();
        }

        int ultimaVirgula = input.lastIndexOf(',');
        if (ultimaVirgula != -1) {
            input = input.substring(0, ultimaVirgula) + " e" + input.substring(ultimaVirgula + 1);
        }

        input = input.toLowerCase();
        input = input.substring(0, 1).toUpperCase() + input.substring(1);

        return input.trim();
    }



}
