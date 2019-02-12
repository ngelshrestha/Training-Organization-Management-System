package Director;

import Admin.Hierarchy;
import User.UserProperty;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by angel on 3/15/17.
 */
public abstract class ReportProperty {

    private UserProperty userPropertyList;
    private Hierarchy packageHierarchy;
    private Date date;
    private HashMap<String,Object> data;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPackageHierarchy(Hierarchy packageHierarchy) {
        this.packageHierarchy = packageHierarchy;
    }

    public void setUserPropertyList(UserProperty userPropertyList) {
        this.userPropertyList = userPropertyList;
    }

    public void setReportData()
    {
        data = new HashMap<>();
    }

    public Date getDate() {
        return date;
    }

    public Hierarchy getPackageHierarchy() {
        return packageHierarchy;
    }

    public UserProperty getUserPropertyList() {
        return userPropertyList;
    }

    public HashMap<String, Object> getReportData() {

        return data;
    }

    public abstract void manipulateData();

}
