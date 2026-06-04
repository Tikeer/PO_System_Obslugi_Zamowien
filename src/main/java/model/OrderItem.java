package model;

public class OrderItem {

    private MenuItem menuItem;
    private int quantity;
    private String notes;

    public OrderItem(MenuItem menuItem, int quanity, String notes){
        this.menuItem = menuItem;
        this.quantity = quanity;
        this.notes = notes;
    }

    public double getUnitTotal(){
        return this.menuItem.getPrice() * this.quantity;
    }

    public MenuItem getMenuItem(){
        return this.menuItem;
    }

    public int getQuanity(){
        return this.quantity;
    }

    public String getNotes(){
        return this.notes;
    }

    //pozniej w javaSwing to sie zmieni
    @Override
    public String toString(){
        String baseText = this.quantity + "x " + this.menuItem.getName();

        if(this.notes != null && !this.notes.isEmpty()) {
            baseText += " (" + this.notes + ")";
        }

        baseText += " - " + getUnitTotal() + "zl";

        return baseText;
    }
}
