public class OrderItem {

    private MenuItem menuItem;
    private int quanity;
    private String notes;

    public OrderItem(MenuItem menuItem, int quanity,String notes){
        this.menuItem = menuItem;
        this.quanity = quanity;
        this.notes = notes;
    }

    public double getUnitTotal(){
        return this.menuItem.getPrice() * this.quanity;
    }

    public MenuItem getMenuItem(){
        return this.menuItem;
    }

    public int getQuanity(){
        return this.quanity;
    }

    public String getNotes(){
        return this.notes;
    }
}
