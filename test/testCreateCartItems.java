import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class testCreateCartItems {
    public static void main(String [] args){
        JSONArray loArr = new JSONArray();
        JSONObject loJSON;
        
        loJSON = new JSONObject();
        loJSON.put("sBarrCode", "000-000-000-01");
        loJSON.put("sDescript", "Item 1");
        loJSON.put("sOtherInf", "Item 1 other info");
        loJSON.put("nQtyOnHnd", 50);
        loJSON.put("nUnitPrce", 100.00);
        loJSON.put("nQtyOrder", 1);
        loArr.add(loJSON);
        
        loJSON = new JSONObject();
        loJSON.put("sBarrCode", "000-000-000-02");
        loJSON.put("sDescript", "Item 2");
        loJSON.put("sOtherInf", "Item 2 other info");
        loJSON.put("nQtyOnHnd", 100);
        loJSON.put("nUnitPrce", 200.00);
        loJSON.put("nQtyOrder", 2);
        loArr.add(loJSON);
        
        loJSON = new JSONObject();
        loJSON.put("sBarrCode", "000-000-000-03");
        loJSON.put("sDescript", "Item 3");
        loJSON.put("sOtherInf", "Item 3 other info");
        loJSON.put("nQtyOnHnd", 150);
        loJSON.put("nUnitPrce", 300.00);
        loJSON.put("nQtyOrder", 3);
        loArr.add(loJSON);
        
        System.out.println(loArr.toJSONString());
    }
}
