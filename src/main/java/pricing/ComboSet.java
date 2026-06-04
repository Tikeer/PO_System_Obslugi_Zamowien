package pricing;

import model.MenuItem;
import model.Order;
import model.OrderItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ComboSet extends PricingRule {

    private double fixedPrice = 40.0;
    private List<MenuItem> comboItems = new ArrayList<>();

    public void randomizeCombo(List<MenuItem> allAvailableItems) {
        if (allAvailableItems.size() >= 2) {
            List<MenuItem> copy = new ArrayList<>(allAvailableItems);
            Collections.shuffle(copy);
            this.comboItems = copy.subList(0, 2);
            System.out.println("Nowy zestaw Combo: " + getRuleName());
        }
    }

    @Override
    public double calculatePrice(Order order) {
        if (comboItems.size() < 2) return calculateStandardPrice(order);

        int countA = 0;
        int countB = 0;
        double totalPrice = 0.0;

        MenuItem itemA = comboItems.get(0);
        MenuItem itemB = comboItems.get(1);

        for (OrderItem orderItem : order.getItems()) {
            if (orderItem.getMenuItem().equals(itemA)) {
                countA += orderItem.getQuanity();
            } else if (orderItem.getMenuItem().equals(itemB)) {
                countB += orderItem.getQuanity();
            } else {
                totalPrice += orderItem.getUnitTotal();
            }
        }

        int numberOfCombos = Math.min(countA, countB);

        totalPrice += numberOfCombos * fixedPrice;
        totalPrice += (countA - numberOfCombos) * itemA.getPrice();
        totalPrice += (countB - numberOfCombos) * itemB.getPrice();

        return totalPrice;
    }

    private double calculateStandardPrice(Order order) {
        return order.getItems().stream().mapToDouble(OrderItem::getUnitTotal).sum();
    }

    @Override
    public String getRuleName() {
        if (comboItems.size() < 2) return "Zestaw Combo zły zestaw";
        return "Zestaw: " + comboItems.get(0).getName() + " + " + comboItems.get(1).getName();
    }

    @Override
    public String getDescription() {
        if (comboItems.size() < 2) return "Brak wybranych produktów do zestawu.";
        return "Kup oba produkty w promocyjnej cenie: " + fixedPrice + " zł";
    }

    public void setFixedPrice(double fixedPrice) {
        this.fixedPrice = fixedPrice;
    }
}