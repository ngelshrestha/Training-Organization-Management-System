package Properties;

import Admin.AdministrativeLevelHierarchy;
import Admin.CourseTypeHierarchy;
import Admin.Hierarchy;

import java.util.HashMap;

/**
 * Created by angel on 3/8/17.
 */
public class TrainingTypeProperty implements Property {

    private String name = null;
    private int id = -1;
    private int courseSubTypeId = -1;
    private Hierarchy hierarchy = null;

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

        if (this.courseSubTypeId != -1)
        {
            hashMap.put("courseSubTypeId", this.courseSubTypeId);
        }

        if (this.hierarchy != null)
        {
            hashMap.put("courseTypeHierarchy", this.hierarchy);
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

        if (hashMap.containsKey("courseSubTypeId"))
        {
            this.courseSubTypeId = (int) hashMap.get("courseSubTypeId");
        }

        if (hashMap.containsKey("courseTypeHierarchy"))
        {
            this.hierarchy = (Hierarchy) hashMap.get("courseTypeHierarchy");
        }
    }
}
