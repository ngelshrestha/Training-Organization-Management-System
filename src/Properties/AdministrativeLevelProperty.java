package Properties;

import java.util.HashMap;

/**
 * Created by angel on 3/8/17.
 */
public class AdministrativeLevelProperty implements Property {

    private String name = null;
    private int id = -1;
    private boolean isChanged = false;

    private HashMap<String, Object> hashMap;

    @Override
    public HashMap getData() {
        hashMap = new HashMap<>();

        if (this.name != null)
        {
            hashMap.put("name", this.name);
        }

        if (this.id != -1)
        {
            hashMap.put("id", this.id);
        }

        hashMap.put("isChanged", this.isChanged);

        return hashMap;
    }

    @Override
    public void setData(HashMap<String, Object> hashMap) {
        this.hashMap = hashMap;

        if (hashMap.containsKey("name"))
        {
            this.name = (String) hashMap.get("name");
        }

        if (hashMap.containsKey("id"))
        {
            this.id = (int) hashMap.get("id");
        }

        if (hashMap.containsKey("isChanged"))
        {
            this.isChanged = (boolean) hashMap.get("isChanged");
        }
    }
}
