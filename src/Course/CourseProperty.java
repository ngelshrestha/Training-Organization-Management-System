package Course;

import Admin.Hierarchy;

import java.util.List;

/**
 * Created by angel on 3/9/17.
 */
public class CourseProperty {

    private int courseId;
    private String courseName;
    private int courseCode;
    private int courseHours;
    private int levelId;
    private int courseSubType;
    private int courseTypeId;
    private int courseModeId;
    private int trainingTypeId;
    private Hierarchy trainingMode;
    private List<Integer> modes;
    private List<Hierarchy> trainingModeHierarchy;


    public CourseProperty()
    {}

    public Hierarchy getTrainingMode() {
        return trainingMode;
    }

    public int getCourseModeId() {
        return courseModeId;
    }

    public List<Hierarchy> getTrainingModeHierarchy() {
        return trainingModeHierarchy;
    }

    public int getCourseTypeId() {
        return courseTypeId;
    }

    public int getTrainingTypeId() {
        return trainingTypeId;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getCourseSubType() {
        return courseSubType;
    }

    public int getLevelId() {
        return levelId;
    }

    public List<Integer> getModes() {
        return modes;
    }

    public int getCourseCode() {
        return courseCode;
    }

    public int getCourseHours() {
        return courseHours;
    }

    public String getCourseName() {
        return courseName;
    }


    public void setTrainingMode(Hierarchy trainingMode) {
        this.trainingMode = trainingMode;
    }

    public void setCourseModeId(int courseModeId) {
        this.courseModeId = courseModeId;
    }

    public void setTrainingModeHierarchy(List<Hierarchy> trainingModeHierarchy) {
        this.trainingModeHierarchy = trainingModeHierarchy;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCourseTypeId(int courseTypeId) {
        this.courseTypeId = courseTypeId;
    }

    public void setTrainingTypeId(int trainingTypeId) {
        this.trainingTypeId = trainingTypeId;
    }

    public void setCourseCode(int courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseHours(int courseHours) {
        this.courseHours = courseHours;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseSubType(int courseSubType) {
        this.courseSubType = courseSubType;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public void setModes(List<Integer> modes) {
        this.modes = modes;
    }
}
