import java.util.ArrayList;
import java.util.List;

public class MenuManager {
    private List<MenuItem> menuItems;

    public MenuManager(){
        this.menuItems = new ArrayList<>();
    }

    public void addItem(MenuItem item){
        if(item != null){
            this.menuItems.add(item);
            System.out.println("Dodano do menu: " + item.getName());
        }
    }

    public void removeItem(int id){
        this.menuItems.remove(id);
    }

    public List<MenuItem> searchMenu(String query){

    }

    public List<MenuItem> getRandomItems(int n){

    }

    public List<MenuItem> getAll(){
        return this.menuItems;
    }

    public void loadMenu(){

    }
}
