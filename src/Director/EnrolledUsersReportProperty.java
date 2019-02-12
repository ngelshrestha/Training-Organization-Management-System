package Director;

import Properties.Property;
import Properties.PropertyFactory;

import java.util.Date;

/**
 * Created by angel on 3/15/17.
 */
public class EnrolledUsersReportProperty extends ReportProperty{

    @Override
    public void manipulateData() {
        this.setReportData();

        Property property = (Property) this.getPackageHierarchy().getProperty().getData().get("userPurchaseStatus");

        this.getReportData().put("userName", this.getUserPropertyList().getUserName());
        this.getReportData().put("packageName", this.getPackageHierarchy().getProperty().getData().get("name"));
        this.getReportData().put("purchaseStatus", property.getData().get("name"));
        this.getReportData().put("date", this.getDate());

    }
}
