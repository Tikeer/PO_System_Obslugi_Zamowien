package model;

import pricing.PricingRule;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int orderID;
    private OrderStatus status;
    private long createdAt;
    private PricingRule currentRule;
    private List<OrderItem> items = new ArrayList<>();

    public Order(int orderID){
        this.orderID = orderID;
        this.status = OrderStatus.PENDING;
        this.createdAt = System.currentTimeMillis();
    }

    public void setPricingRule(PricingRule rule){
        this.currentRule = rule;
    }

    public double getTotalPrice(){
        if (currentRule == null){
            return 0.0;
        }
        return currentRule.calculatePrice(this);
    }

    public void addItem(OrderItem item){
        this.items.add(item);
    }

    public void removeItem(OrderItem item){
        this.items.remove(item);
    }

    public void changeStatus(OrderStatus newStatus){
        this.status = newStatus;
    }

    public List<OrderItem> getItems(){
        return this.items;
    }

    public int getOrderID(){
        return this.orderID;
    }

    public OrderStatus getOrderStatus(){
        return this.status;
    }

    public long getCreatedAt() {return this.createdAt;}

}

