/*import manager.DataManager;
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
*/

import manager.DataManager;
import manager.MenuManager;
import manager.OrderManager;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.OrderStatus;
import pricing.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        DataManager dataManager = new DataManager();
        MenuManager menuManager = new MenuManager();
        OrderManager orderManager = new OrderManager();

        List<MenuItem> loadedMenu = dataManager.loadMenu();
        if (loadedMenu.isEmpty()) {
            System.out.println("Generowanie domyslnego menu");

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
            System.out.println("Zaladowano menu z istniejacego systemu");
            for (MenuItem item : loadedMenu) {
                menuManager.addItem(item);
            }
        }

        System.out.println("Symulacja pierwszego klienta");

        List<Order> loadedOrders = dataManager.loadOrders();

        List<MenuItem> currentMenu = menuManager.getAllItems();
        MenuItem orderedPizza = currentMenu.get(0);
        MenuItem orderedCola = currentMenu.get(2);

        Order testOrder = orderManager.createOrder(new StandardPricing());

        testOrder.addItem(new OrderItem(orderedPizza,2,"Bardzo wypieczona"));
        testOrder.addItem(new OrderItem(orderedCola,2,"Bez lodu"));

        testOrder.changeStatus(OrderStatus.IN_PROGRESS);

        System.out.println("Do zaplaty: " + testOrder.getTotalPrice() + " zl");

        dataManager.saveOrder(orderManager.getAllOrders());
    }
}
/*
        MenuItem pizza = new MenuItem(1, "Pizza Margherita", 32.0, "Główne");
        MenuItem burger = new MenuItem(2, "Burger Wołowy", 28.0, "Główne");
        MenuItem cola = new MenuItem(3, "Cola 0.5l", 8.0, "Napoje");
        MenuItem pasta = new MenuItem(4, "Pasta Carbonara", 30.0, "Główne");

        menuManager.addItem(pizza);
        menuManager.addItem(burger);
        menuManager.addItem(cola);
        menuManager.addItem(pasta);
        menuManager.loadMenu();

        System.out.println("\n--- START TESTÓW PAKIETU PRICING ---\n");

        //Standard
        Order o1 = orderManager.createOrder(new StandardPricing());
        o1.addItem(new OrderItem(pizza, 1, "Bez cebuli"));
        o1.addItem(new OrderItem(cola, 2, "Z lodem"));
        printResult(o1);

        // StudentPromo
        Order o2 = orderManager.createOrder(new StudentPromo());
        o2.addItem(new OrderItem(burger, 1, "Dobrze wysmażony"));
        o2.addItem(new OrderItem(cola, 1, ""));
        printResult(o2);

        // Combo
        DishOfTheDay dod = new DishOfTheDay();

        List<MenuItem> wylosowaneDnia = menuManager.getRandomItems(2);
        dod.refreshDailyItems(wylosowaneDnia);

        Order o3 = orderManager.createOrder(dod);

        o3.addItem(new OrderItem(wylosowaneDnia.get(0), 1, "Promocyjne!"));
        o3.addItem(new OrderItem(cola, 1, "Normalna cena"));
        printResult(o3);

        //Happy hour
        Order o4 = orderManager.createOrder(new HappyHour());
        o4.addItem(new OrderItem(pasta, 2, ""));
        printResult(o4);

        // zestaw
        ComboSet combo = new ComboSet();
        combo.setFixedPrice(45.0); // Stała cena za parę produktów
        combo.randomizeCombo(menuManager.searchMenu("")); // Losujemy parę z całego menu

        Order o5 = orderManager.createOrder(combo);
        System.out.println("Aktywne Combo: " + combo.getRuleName());
        o5.addItem(new OrderItem(cola, 1, "Poza combo"));
        printResult(o5);
    }

    private static void printResult(Order order) {
        System.out.println("ID Zamówienia: " + order.getOrderID());
        System.out.println("Zastosowana reguła: " + order.getTotalPrice() + " zł (" + order.getOrderStatus() + ")");
        System.out.println("--------------------------------------");
    }
}
*/