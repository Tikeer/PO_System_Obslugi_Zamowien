import manager.DataManager;
import manager.MenuManager;
import manager.OrderManager;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import pricing.*;
import gui.RestaurantGUI;

import javax.swing.SwingUtilities;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        DataManager dataManager = new DataManager();
        MenuManager menuManager = new MenuManager();
        OrderManager orderManager = new OrderManager();


        List<MenuItem> loadedMenu = dataManager.loadMenu();
        if (loadedMenu.isEmpty()) {
            System.out.println("Generowanie domyślnego menu");

            MenuItem pizza = new MenuItem(1, "Pizza Margherita", 32.0, "Główne");
            MenuItem burger = new MenuItem(2, "Burger Wołowy", 28.0, "Główne");
            MenuItem cola = new MenuItem(3, "Cola 0.5l", 8.0, "Napoje");
            MenuItem pasta = new MenuItem(4, "Pasta Carbonara", 30.0, "Główne");

            menuManager.addItem(pizza);
            menuManager.addItem(burger);
            menuManager.addItem(cola);
            menuManager.addItem(pasta);

            dataManager.saveMenu(menuManager.getAllItems());
        } else {
            System.out.println("Załadowano menu z istniejącego systemu");
            for (MenuItem item : loadedMenu) {
                menuManager.addItem(item);
            }
        }


        System.out.println("Uruchamianie interfejsu graficznego (GUI)...");

        SwingUtilities.invokeLater(() -> {

            RestaurantGUI gui = new RestaurantGUI(menuManager, orderManager,dataManager);
            gui.setVisible(true);
        });
    }
}