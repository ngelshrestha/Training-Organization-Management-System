package Properties;

/**
 * Created by angel on 3/9/17.
 */
public class PropertyFactory {

    public static Property getProperty(String type)
    {
        Property property = null;

        switch (type)
        {
            case "Administrative Level":
                property = new AdministrativeLevelProperty();
                break;

            case "Training Type":
                property = new TrainingTypeProperty();
                break;

            case "Training Mode":
                property = new TrainingModeProperty();
                break;

            case "Course Type":
                property = new CourseTypeProperty();
                break;

            case "Package":
                property = new PackageProperty();
                break;

            case "Purchase Status":
                property = new PurchaseStatusProperty();
                break;

            default:
                property = null;
        }

        return property;
    }

}
