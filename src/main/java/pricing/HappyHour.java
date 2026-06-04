package pricing;

import model.Order;
import model.OrderItem;

public class HappyHour extends PricingRule {

    private double discount = 0.2;

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
        return "Happy Hour";
    }

    @Override
    public String getDescription() {
        return "Promocja ograniczona czasowo.";
    }
}