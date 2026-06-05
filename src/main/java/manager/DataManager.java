package manager;

import model.Order;
import model.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pricing.PricingRule;
import pricing.PricingRuleAdapter;

public class DataManager {
    private final String filePathOrders = "orders.json";
    private final String filePathMenu = "menu.json";
    private Gson gson;

    public DataManager(){
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(PricingRule.class, new PricingRuleAdapter())
                .create();
    }

    public void saveOrder(List<Order> orders){
        try(FileWriter writer = new FileWriter(filePathOrders)){
            gson.toJson(orders,writer);
            System.out.println("Pomyslnie zapisano zamowienia do pliku");
        }
        catch (IOException e){
            System.out.println("Wystapil blad podczas zapisu: " + e.getMessage());
        }
    }

    public List<Order> loadOrders(){
        try(FileReader reader = new FileReader(filePathOrders)){
            Type listType = new TypeToken<ArrayList<Order>>(){}.getType();
            List<Order> loadOrders = gson.fromJson(reader, listType);

            if(loadOrders == null){
                return new ArrayList<>();
            }

            System.out.println("Pomyslnie wczytano baze zamowien");
            return loadOrders;
        }
        catch (IOException e ){
            System.out.println("Brak pliku z danymi. Tworze pusta baze zamowien.");
            return new ArrayList<>();
        }
    }

    public void saveMenu(List<MenuItem> menu){
        try(FileWriter writer = new FileWriter(filePathMenu)){
            gson.toJson(menu,writer);
            System.out.println("Pomyslnie zapisano menu do pliku");
        }
        catch (IOException e){
            System.out.println("Wystapil blad podczas zapisu: " + e.getMessage());
        }
    }

    public List<MenuItem> loadMenu(){

        DataManager dataManager = new DataManager();
        MenuManager menuManager = new MenuManager();

        try(FileReader reader = new FileReader(filePathMenu)){
            Type listType = new TypeToken<ArrayList<MenuItem>>(){}.getType();
            List<MenuItem> loadedMenu = gson.fromJson(reader,listType);

            if(loadedMenu == null || loadedMenu.isEmpty()){
                return new ArrayList<>();
            }

            System.out.println("Pomyslnie wczytano menu z pliku.");
            return loadedMenu;
        }
        catch (IOException e){
            System.out.println("Brak pliku menu. System wygeneruje domyslne menu. ");
            return new ArrayList<>();
        }
    }


}
