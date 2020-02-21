package edu.lawrence.androiddaycare;

import java.sql.Date;
import java.time.LocalDate;
/**
 *
 * @author Khanh Toan
 */
public class Child {

    private int id;
    private int parentID;
    private String name;
    private Date birthday;

    public Child() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

}
