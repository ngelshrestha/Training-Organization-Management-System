package Director;

/**
 * Created by angel on 3/15/17.
 */
public class ReportPropertyFactory {

    public static ReportProperty getReportProperty(String type)
    {
        ReportProperty property;

        switch (type)
        {
            case "Feedback":
                property = new FeedbackReportProperty();
                break;

            case "User":
                property = new EnrolledUsersReportProperty();
                break;

            default:
                property = null;
                break;
        }

        return property;
    }

}
