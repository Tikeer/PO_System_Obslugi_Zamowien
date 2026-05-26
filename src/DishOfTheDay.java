import java.util.List;

public class DishOfTheDay extends PricingRule{
    private double discount;
    private List<MenuItem> dailyItems;

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
