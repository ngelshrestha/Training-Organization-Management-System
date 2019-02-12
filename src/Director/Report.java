package Director;

import Admin.Hierarchy;
import JDBCConnection.JDBCConnectionPool;
import User.UserProperty;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by angel on 3/15/17.
 */
public abstract class Report {

    private String type;
    protected List<HashMap<String,Object>> list;
    protected JDBCConnectionPool pool;

    public Report(String type, JDBCConnectionPool pool)
    {
        this.type = type;
        this.pool = pool;
    }

    public List<ReportProperty> steps() throws SQLException {
        getReportData();
        return generateReport();
    }

    public List<ReportProperty> generateReport()
    {
        List<ReportProperty> reports = new ArrayList<>();

        for (HashMap<String,Object> singleReport:
                list) {

            ReportProperty reportProperty = ReportPropertyFactory.getReportProperty(type);

            reportProperty.setUserPropertyList((UserProperty) singleReport.get("user"));
            reportProperty.setPackageHierarchy((Hierarchy) singleReport.get("package"));
            reportProperty.setDate((Date) singleReport.get("date"));

            reportProperty.manipulateData();

            reports.add(reportProperty);
        }

        return reports;
    }

    public abstract void getReportData() throws SQLException;

}
