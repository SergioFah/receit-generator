package model;

import lombok.Getter;
import lombok.Setter;
import util.MathUtil;


@Setter
public class PaymentValues {
    private double totalMonthly;
    private double deliveryTax;
    private double discountedPixValue;
    private double totalWithDelivery;
    private double pixDiscount;
    private double cardDiscount;

    public String getTotalMonthly() {
        return String.format("R$ %.2f", MathUtil.round(totalMonthly, 1));
    }

    public String getDeliveryTax() {
        return String.format("R$ %.2f", MathUtil.round(deliveryTax, 1));
    }

    public String getDiscountedPixValue() {
        return String.format("R$ %.2f", MathUtil.round(discountedPixValue, 1));
    }

    public String getTotalWithDelivery() {
        return String.format("R$ %.2f", MathUtil.round(totalWithDelivery, 1));
    }

    public String getPixDiscount() {
        if (pixDiscount > 0) {
            return String.format(" (-%.0f%%)", pixDiscount);
        }
        return "";
    }

    public String getCardDiscount() {
        if (cardDiscount > 0) {
            return String.format(" (-%.0f%%)", cardDiscount);
        }
        return "";
    }

}
