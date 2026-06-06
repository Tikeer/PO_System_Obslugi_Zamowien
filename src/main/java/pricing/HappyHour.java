package pricing;
import model.Order;
import model.OrderItem;
import java.time.LocalTime;
import java.util.Random;

public class HappyHour extends PricingRule {
    private int activeHour = new Random().nextInt(11) + 10; 

    public boolean isHappyHourNow() {
        return LocalTime.now().getHour() == activeHour;
    }

    public int getActiveHour() { return activeHour; }

    @Override
    public double calculatePrice(Order order) {
        return order.getItems().stream().mapToDouble(OrderItem::getUnitTotal).sum() * 0.8;
    }

    @Override
    public String getRuleName() { return "Happy Hour"; }

    @Override
    public String getDescription() {
        return "Promocja 20% obowiązuje tylko między " + activeHour + ":00 a " + activeHour + ":59.";
    }
}