package pricing;

import model.MenuItem;
import model.Order;
import model.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DishOfTheDay extends PricingRule {

    private double discount = 0.25;

    private List<MenuItem> dailyItems = new ArrayList<>();

    @Override
    public double calculatePrice(Order order) {
        double total = 0.0;

        for (OrderItem item : order.getItems()) {
            double itemValue = item.getUnitTotal();

            if (dailyItems.contains(item.getMenuItem())) {

                total += itemValue * (1 - discount);
            } else {

                total += itemValue;
            }
        }
        return total;
    }

    public void refreshDailyItems(List<MenuItem> newSelection) {
        this.dailyItems = newSelection;
    }

    @Override
    public String getRuleName() {
        return "Danie Dnia";
    }

    @Override
    public String getDescription() {
        if (dailyItems.isEmpty()) {
            return "Dzisiaj nie wybrano jeszcze dań dnia.";
        }

        String itemNames = dailyItems.stream()
                .map(MenuItem::getName)
                .collect(Collectors.joining(", "));

        return "Promocja na wybrane dania";
    }
}