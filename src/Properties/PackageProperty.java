package Properties;

import Admin.Hierarchy;
import Admin.PackageHierarchy;
import Course.CourseProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PackageProperty implements Property {

    private String name = null;
    private int id = -1;
    private Hierarchy level = null;
    private List<CourseProperty> courseProperty = null;
    private PackageHierarchy hierarchy = null;
    private Property userPurchaseStatus = null;
    private int courseRequired = -1;

    private HashMap<String, Object> hashMap;

    @Override
    public HashMap<String ,Object> getData() {
        hashMap = new HashMap<>();

        if (this.name != null)
        {
            hashMap.put("name", this.name);
        }

        if (this.id != -1)
        {
            hashMap.put("id", this.id);
        }

        if (this.courseProperty != null)
        {
            hashMap.put("courseIds", this.courseProperty);
        }

        if (this.level != null)
        {
            hashMap.put("level", this.level);
        }

        if (this.courseRequired != -1)
        {
            hashMap.put("courseRequired", this.courseRequired);
        }

        if (this.hierarchy != null)
        {
            hashMap.put("packageHierarchy",this.hierarchy);
        }

        if (this.userPurchaseStatus != null)
        {
            hashMap.put("userPurchaseStatus", this.userPurchaseStatus);
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

        if (hashMap.containsKey("courseIds"))
        {
            this.courseProperty = (List<CourseProperty>) hashMap.get("courseIds");
        }

        if (hashMap.containsKey("level"))
        {
            this.level = (Hierarchy) hashMap.get("level");
        }

        if (hashMap.containsKey("courseRequired"))
        {
            this.courseRequired = (int) hashMap.get("courseRequired");
        }

        if (hashMap.containsKey("packageHierarchy"))
        {
            this.hierarchy = (PackageHierarchy) hashMap.get("packageHierarchy");
        }

        if (hashMap.containsKey("userPurchaseStatus"))
        {
            this.userPurchaseStatus = (Property) hashMap.get("userPurchaseStatus");
        }
    }
}
