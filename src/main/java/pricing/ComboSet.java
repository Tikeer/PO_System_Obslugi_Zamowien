package pricing;
import model.*;
import java.util.*;

public class ComboSet extends PricingRule {
    private List<MenuItem> comboItems = new ArrayList<>();

    public void randomizeCombo(List<MenuItem> allAvailableItems) {
        if (allAvailableItems.size() >= 2) {
            List<MenuItem> copy = new ArrayList<>(allAvailableItems);
            Collections.shuffle(copy);
            this.comboItems = copy.subList(0, 2);
        }
    }

    @Override
    public double calculatePrice(Order order) {
        return order.getItems().stream().mapToDouble(OrderItem::getUnitTotal).sum() * 0.9;
    }

    @Override
    public String getRuleName() {
        if (comboItems.size() < 2) return "Brak wylosowanego Combo";
        return "Zestaw: " + comboItems.get(0).getName() + " + " + comboItems.get(1).getName();
    }

    @Override
    public String getDescription() { return "Zniżka 10% na zestaw produktów."; }
}