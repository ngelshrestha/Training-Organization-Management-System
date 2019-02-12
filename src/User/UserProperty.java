package User;

import Admin.Hierarchy;

import java.util.Date;

/**
 * Created by angel on 3/15/17.
 */
public class UserProperty {

    private int userId;
    private String userName;
    private String email;
    private String password;
    private PasswordProperty passwordProperty;
    private int phoneNo;
    private Hierarchy level;
    private Hierarchy coursePackage;
    private String message;
    private Date date;

    public void setPasswordProperty(PasswordProperty passwordProperty) {
        this.passwordProperty = passwordProperty;
    }

    public void setPhoneNo(int phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(Hierarchy level) {
        this.level = level;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCoursePackage(Hierarchy coursePackage) {
        this.coursePackage = coursePackage;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public PasswordProperty getPasswordProperty() {
        return passwordProperty;
    }

    public int getPhoneNo() {
        return phoneNo;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Hierarchy getLevel() {
        return level;
    }

    public Hierarchy getCoursePackage() {
        return coursePackage;
    }

    public Date getDate() {
        return date;
    }
}
