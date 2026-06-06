package model;

import pricing.PricingRule;
import pricing.StandardPricing;
import java.util.ArrayList;
import java.util.List;


public class Order {
    private static int idCounter = 1;

    private int orderID;
    private long createdAt;
    private OrderStatus orderStatus;
    private List<OrderItem> items;
    private PricingRule pricingRule;

    public Order(PricingRule pricingRule) {
        this.orderID = idCounter++;
        this.createdAt = System.currentTimeMillis();
        this.orderStatus = OrderStatus.PENDING;
        this.items = new ArrayList<>();
        this.pricingRule = pricingRule;
    }

    public void addItem(OrderItem item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    public void changeStatus(OrderStatus status) {
        this.orderStatus = status;
    }

    public double getTotalPrice() {
        if (this.pricingRule == null) {

            StandardPricing defaultPricing = new StandardPricing();
            return defaultPricing.calculatePrice(this);
        }

        return this.pricingRule.calculatePrice(this);
    }

    public int getOrderID() {
        return orderID;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderItem> getItems() {
        return items;
    }


    public void setPricingRule(PricingRule pricingRule) {
        this.pricingRule = pricingRule;
    }

    public PricingRule getPricingRule() {
        return this.pricingRule;
    }

    @Override
    public String toString() {
        return "Zamówienie #" + orderID + " [" + orderStatus + "] - " + String.format("%.2f", getTotalPrice()) + " zł";
    }
}