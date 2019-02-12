package Properties;

import java.util.HashMap;

/**
 * Created by angel on 3/15/17.
 */
public class PurchaseStatusProperty implements Property {

    private int id = -1;
    private String name = null;

    private HashMap<String,Object> hashMap = null;

    @Override
    public HashMap getData() {
        hashMap = new HashMap<>();

        if (this.id != -1)
        {
            hashMap.put("id", this.id);
        }

        if (this.name != null)
        {
            hashMap.put("name", this.name);
        }

        return hashMap;
    }

    @Override
    public void setData(HashMap<String, Object> data) {
        this.hashMap = data;

        if (this.hashMap.containsKey("id"))
        {
            this.id = (int) this.hashMap.get("id");
        }

        if (this.hashMap.containsKey("name"))
        {
            this.name = (String) this.hashMap.get("name");
        }
    }
}
