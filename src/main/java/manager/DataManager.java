package manager;

import model.Order;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private final String filePath = "orders.json";
    private Gson gson;

    public DataManager(){
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveOrder(List<Order> orders){
        try(FileWriter writer = new FileWriter(filePath)){
            gson.toJson(orders,writer);
            System.out.println("Pomyslnie zapisano zamowienia do pliku");
        }
        catch (IOException e){
            System.out.println("Wystapil blad podczas zapisu: " + e.getMessage());
        }
    }

    public List<Order> loadOrders(){
        try(FileReader reader = new FileReader(filePath)){
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

    public void exportToFile(){

    }

    public void importFromFile(){

    }
}
