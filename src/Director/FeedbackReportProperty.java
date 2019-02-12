package Director;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by angel on 3/15/17.
 */
public class FeedbackReportProperty extends ReportProperty {

    @Override
    public void manipulateData() {
        this.setReportData();

        this.getReportData().put("userName", this.getUserPropertyList().getUserName());
        this.getReportData().put("packageName", this.getPackageHierarchy().getProperty().getData().get("name"));
        this.getReportData().put("message", this.getUserPropertyList().getMessage());
        this.getReportData().put("date", this.getDate());

    }
}
