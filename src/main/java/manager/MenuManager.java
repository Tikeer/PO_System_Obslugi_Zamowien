package manager;


import model.MenuItem;


import java.util.ArrayList;

import java.util.List;


public class MenuManager {

    private List<MenuItem> menuItems;


    public MenuManager() {

        this.menuItems = new ArrayList<>();

    }


    public void addItem(MenuItem item) {

        if (item != null) {

            this.menuItems.add(item);

            System.out.println("Dodano do menu: " + item.getName());

        }

    }


    public void removeItem(int id) {

        this.menuItems.remove(id);

    }


    public List<MenuItem> searchMenu(String query) {
        List<MenuItem> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (MenuItem item : menuItems) {

            if (item.getName().toLowerCase().contains(lowerQuery) ||
                    item.getCategory().toLowerCase().contains(lowerQuery)) {
                results.add(item);
            }
        }
        return results;
    }

    public List<MenuItem> getRandomItems(int n) {
        List<MenuItem> copy = new ArrayList<>(menuItems);

        java.util.Collections.shuffle(copy);


        return copy.subList(0, Math.min(n, copy.size()));
    }

    public void loadMenu() {

        System.out.println("System: Menu gotowe do pracy. Załadowano " + menuItems.size() + " pozycji.");
    }
}