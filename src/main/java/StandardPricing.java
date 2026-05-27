public class StandardPricing extends PricingRule{

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
        return
    }

    @Override
    public String getDescription(){
        return
    }
}
