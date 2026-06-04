import manager.DataManager;
import manager.OrderManager;

public class Main {
    public static void main(String[] args){
        DataManager dataManager = new DataManager();
        OrderManager orderManager = new OrderManager();

        // wczytanie starych plikow

        System.out.println("Witaj w systemie zamowien");

        dataManager.saveOrder(orderManager.getAllOrders());
    }
}
