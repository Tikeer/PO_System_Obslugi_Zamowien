public class MenuItem {

    private int ID;
    private String name;
    private double price;
    private String category;

    public MenuItem(int ID, String name, double price, String category){
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getID(){
        return this.ID;
    }

    public String getName(){
        return this.name;
    }

    public double getPrice(){
        return this.price;
    }

    public String getCategory(){
        return this.category;
    }

    @Override
    public String toString(){
        return this.name + " (" + this.category + ") - " + this.price + "zl";
    }
}
