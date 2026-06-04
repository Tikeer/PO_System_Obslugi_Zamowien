package pricing;

import model.Order;
import model.OrderItem;

public class StandardPricing extends PricingRule {

    @Override
    public double calculatePrice(Order order){
        double total = 0.0;

        for (OrderItem item : order.getItems()){
            total += item.getUnitTotal();
        }
        return total;
    }

    @Override
    public String getRuleName(){
        return "Cennik Standardowy";
    }

    @Override
    public String getDescription(){
        return "Podstawowa metoda naliczania opłat na podstawie cen produktów.";
    }
}
