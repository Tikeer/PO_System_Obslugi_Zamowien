package pricing;

import model.Order;

public class HappyHour extends PricingRule {
    private double discount = 0.2;


    @Override
    public double calculatePrice(Order order) {
        return 0;
    }

    @Override
    public String getRuleName(){
        return
    }

    @Override
    public String getDescription(){
        return
    }
}
