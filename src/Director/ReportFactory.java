package Director;

import JDBCConnection.JDBCConnectionPool;

/**
 * Created by angel on 3/15/17.
 */
public class ReportFactory {

    public static Report getReport(String type, JDBCConnectionPool pool)
    {
        Report report;

        switch (type)
        {
            case "Feedback":
                report = new GetFeedbackReport(type,pool);
                break;

            case "User":
                report = new GetEnrolledUserReport(type,pool);
                break;

            default:
                report = null;
                break;
        }

        return report;
    }

}
