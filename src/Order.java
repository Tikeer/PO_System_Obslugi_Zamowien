import java.util.ArrayList;
import java.util.List;

enum orderStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

public class Order {

    private int orderID;
    private orderStatus status;
    private long createdAt;
    private PricingRule currentRule;
    private List<OrderItem> items = new ArrayList<>();

    public Order(int orderID){
        this.orderID = orderID;
        this.status = orderStatus.PENDING;
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

    public OrderItem addItem{
        items.add()
    }

    public OrderItem removeItem{
        items.remove()
    }

    public orderStatus changeStatus{

    }

    public List<OrderItem> getItems(){
        return this.items;
    }

    public int getOrderID(){
        return this.orderID;
    }
}

