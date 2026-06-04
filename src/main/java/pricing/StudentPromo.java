package pricing;

import model.Order;
import model.OrderItem;

public class StudentPromo extends PricingRule {

    private double discount = 0.35;

    @Override
    public double calculatePrice(Order order) {
        double total = 0.0;


        for (OrderItem item : order.getItems()) {

            total += item.getUnitTotal();
        }


        return total * (1 - discount);
    }

    @Override
    public String getRuleName() {
        return "Promocja Studencka";
    }

    @Override
    public String getDescription() {
        return "Zniżka studencka naliczana od całości zamówienia.";
    }
}