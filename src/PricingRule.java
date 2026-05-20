public class PricingRule {

    public abstract double calculatePrice(Order order);

    public abstract String getRuleName();

    public abstract String getDescription();
}
