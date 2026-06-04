package manager;

import model.Order;
import model.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private List<Order> orders;
    private int nextOrderId = 1;

    public OrderManager(){
        this.orders = new ArrayList<>();
    }

    public Order createOrder(PricingRule rule){
        Order newOrder = new Order(nextOrderId);
        nextOrderId += 1;
        newOrder.setPricingRule(rule);
        this.orders.add(newOrder);
        return newOrder;
    }

    public Order getOrder(int id){
        for(Order order : this.orders){
            if (order.getOrderID() == id){
                return order;
            }
        }
        return null;
    }

    public List<Order> getAllOrders() {
        return this.orders;
    }

    public void cancelOrder(int id){
        Order orderToCancel = getOrder(id);
        if(orderToCancel != null){
            orderToCancel.changeStatus(OrderStatus.CANCELLED);
        }
    }
}
