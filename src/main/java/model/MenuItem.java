package model;

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
    public String getCategory(){return this.category;}

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString(){
        return this.name + " (" + this.category + ") - " + this.price + "zl";
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItem = (MenuItem) o;
        return ID == menuItem.ID;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(ID);
    }



}
