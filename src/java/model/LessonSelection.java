/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author bastinl
 */
public class LessonSelection  {
    
    private HashMap<String, Lesson> chosenLessons;
    private int ownerID;
    
    private DataSource ds = null;
    
    private ResultSet rs = null;
    private Statement st = null;

    public LessonSelection(int owner) {
        
        chosenLessons = new HashMap<String, Lesson>();
        this.ownerID = owner;

        // You don't need to make any changes to the try/catch code below
        try {
            // Obtain our environment naming context
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            ds = (DataSource)envCtx.lookup("jdbc/LessonDatabase");
        }
            catch(Exception e) {
            System.out.println("Exception message is " + e.getMessage());
        }
        
        // Connect to the database - this is a pooled connection, so you don't need to close it afterwards
        try {
            Connection connection = ds.getConnection();
            try {
                if (connection != null) {
                    /**
                     * Select all the lessons selected by the user using an
                     * inner join on the lessons_booked lessonID and the lessons
                     * lessonID where the lessons_booked clientID is equal to
                     * the user's ID. The results are then added to the
                     * chosenLessons HashMap.
                     */
                    PreparedStatement pstmt = connection.prepareStatement
                        ("SELECT l.lessonid, l.description, l.level, l.startDateTime, l.endDateTime "
                            + "FROM lessons l "
                            + "INNER JOIN lessons_booked lb "
                            + "ON lb.lessonid = l.lessonid "
                            + "WHERE lb.clientid = ?");
                    pstmt.setInt(1, this.ownerID);
                    rs = pstmt.executeQuery();
                    
                    Lesson lesson;
                    while (rs.next()) {
                        String lessonID = rs.getString(1);
                        String description = rs.getString(2);
                        int level = rs.getInt(3);
                        Timestamp startTime = rs.getTimestamp(4);
                        Timestamp endTime = rs.getTimestamp(5);

                        lesson = new Lesson(description, startTime, endTime, level, lessonID);
                        chosenLessons.put(lessonID, lesson);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            }
        } catch (Exception e){

            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
        }
    }

    /**
     * @return the items
     */
    public Set <Entry <String, Lesson>> getItems() {
        return chosenLessons.entrySet();
    }

    public void addLesson(Lesson l) {
       
        Lesson i = new Lesson(l);
        this.chosenLessons.put(l.getId(), i);
       
    }
    
    public void removeLesson(String lessonID) {
        this.chosenLessons.remove(lessonID);
    }

    public Lesson getLesson(String id){
        return this.chosenLessons.get(id);
    }
    
    public int getNumChosen(){
        return this.chosenLessons.size();
    }

    public int getOwner() {
        return this.ownerID;
    }
    
    /**
     * This method removes all current bookings in the database made by the user
     * and then updates the database with their current selection.
     */
    public void updateBooking() {
        try {
            Connection connection = ds.getConnection();
            try {
                if (connection != null) {
                    // Delete current selection from database
                    PreparedStatement pstmt = connection.prepareStatement("DELETE FROM lessons_booked WHERE clientid = ?");
                    pstmt.setInt(1, this.ownerID);
                    pstmt.executeUpdate();

                    // Prepare statement to add a lesson into the database
                    pstmt = connection.prepareStatement("INSERT INTO lessons_booked(clientid, lessonid) VALUES (?, ?)");
                    pstmt.setInt(1, this.ownerID);

                    // Add each selected lesson into the database
                    // The HashMap keys here correspond to the lessonID
                    Object[] lessonKeys = chosenLessons.keySet().toArray();
                    for (Object lessonKey : lessonKeys) {
                        pstmt.setString(2, (String) lessonKey);
                        pstmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                System.out.println("Exception is ;" + e + ": message is " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Exception is ;" + e + ": message is " + e.getMessage());
        }
        
    }

}
