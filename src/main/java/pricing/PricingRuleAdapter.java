package pricing;

import com.google.gson.*;
import java.lang.reflect.Type;

 public class PricingRuleAdapter implements JsonSerializer<PricingRule>, JsonDeserializer<PricingRule> {

    @Override
    public JsonElement serialize(PricingRule src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", src.getClass().getSimpleName());

        jsonObject.add("data", context.serialize(src));
        return jsonObject;
    }

    @Override
    public PricingRule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();


        String type = jsonObject.get("type").getAsString();
        JsonElement data = jsonObject.get("data");


        switch (type) {
            case "StandardPricing":
                return context.deserialize(data, StandardPricing.class);
            case "StudentPromo":
                return context.deserialize(data, StudentPromo.class);
            case "HappyHour":
                return context.deserialize(data, HappyHour.class);
            case "DishOfTheDay":
                return context.deserialize(data, DishOfTheDay.class);
            case "ComboSet":
                return context.deserialize(data, ComboSet.class);
            default:
                return new StandardPricing(); // Bezpieczny fallback
        }
    }
}
