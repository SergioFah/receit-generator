package model;

import lombok.Getter;
import lombok.Setter;


@Setter
public class PaymentValues {
    private double totalMonthly;
    private double deliveryTax;
    private double discountedPixValue;
    private double totalWithDelivery;
    private double pixDiscount;
    private double cardDiscount;

    public String getTotalMonthly() {
        return String.format("R$ %.2f", totalMonthly);
    }

    public String getDeliveryTax() {
        return String.format("R$ %.2f", deliveryTax);
    }

    public String getDiscountedPixValue() {
        return String.format("R$ %.2f", discountedPixValue);
    }

    public String getTotalWithDelivery() {
        return String.format("R$ %.2f", totalWithDelivery);
    }

    public String getPixDiscount() {
        if (pixDiscount > 0) {
            return String.format("(-%.0f%%)", pixDiscount);
        }
        return "";
    }

    public String getCardDiscount() {
        if (cardDiscount > 0) {
            return String.format("(-%.0f%%)", cardDiscount);
        }
        return "";
    }

}
