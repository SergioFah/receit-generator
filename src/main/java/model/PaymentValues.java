package model;

import lombok.Getter;
import lombok.Setter;


@Setter
public class PaymentValues {
    private double totalMonthly;
    private double deliveryTax;
    private double discountedPixValue;
    private double totalWithDelivery;

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

}
