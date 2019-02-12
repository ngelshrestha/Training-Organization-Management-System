package Properties;

import java.util.HashMap;

/**
 * Created by angel on 3/13/17.
 */
public class CourseTypeProperty implements Property {

    private String name = null;
    private int id = -1;

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
    }
}
