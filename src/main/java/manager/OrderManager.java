package manager;

import model.Order;
import pricing.PricingRule;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {

    private List<Order> orders = new ArrayList<>();

    public Order createOrder(PricingRule rule) {
        Order newOrder = new Order(rule);
        orders.add(newOrder);
        return newOrder;
    }

    public void addOrder(Order order) {
        if (order != null) {
            orders.add(order);
        }
    }

    public List<Order> getAllOrders() {
        return orders;
    }
}