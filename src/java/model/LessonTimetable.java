/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


/**
 *
 * @author bastinl
 */
public class LessonTimetable {

  private Connection connection=null;
  
  private ResultSet rs = null;
  private Statement st = null;
  
  private Map lessons = null;
  
  private DataSource ds = null;
    
    public LessonTimetable() {

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
        
        try {
            // Connect to the database - you can use this connection to 
            // create and prepare statements, and you don't need to worry about closing it
            Connection connection = ds.getConnection();
        
             try {

                if (connection != null) {
                    lessons = new HashMap<String, Lesson>();
                    
                    /**
                     * Retrieve all the lessons from the database and then
                     * create lesson objects using the resulting data.
                     * Store these lesson objects into the lessons HashMap
                     */
                    PreparedStatement pstmt = connection.prepareStatement("SELECT lessonid, description, level, startDateTime, endDateTime FROM lessons");
                    rs = pstmt.executeQuery();
                    Lesson lesson;
                    
                    while (rs.next()) {
                        String lessonID = rs.getString(1);
                        String description = rs.getString(2);
                        int level = rs.getInt(3);
                        Timestamp startTime = rs.getTimestamp(4);
                        Timestamp endTime = rs.getTimestamp(5);
                        
                        lesson = new Lesson(description, startTime, endTime, level, lessonID);
                        lessons.put(lessonID, lesson);
                    }
                }

            }catch(SQLException e) {

                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
        
            }
        
        
          }catch(Exception e){

             System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
          }
      
    }
    
   
    /**
     * @return the items
     */
    public Lesson getLesson(String itemID) {
        
        return (Lesson)this.lessons.get(itemID);
    }

    public Map getLessons() {
        
        return this.lessons;
        
    }
    
    /**
     * Get the earliest date from the lessons in the database. If there are no
     * lessons in the database, return todays date
     * 
     * @return String A date in the format 'YYYY-mm-dd
     */
    public String getEarliestLessonDate() {
        // Learnt about DateFormat from http://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Connection connection = ds.getConnection();
            try {
                if (connection != null) {
                    PreparedStatement pstmt = connection.prepareStatement("SELECT startDateTime FROM lessons ORDER BY startDateTime LIMIT 1");
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        Timestamp earliestLessonDate = rs.getTimestamp(1);
                        return dateFormat.format(earliestLessonDate);
                    } else {
                        // If there are no lessons in the databse, return todays date
                        return dateFormat.format(new Date());
                    }
                }

            } catch (SQLException e) {
                System.out.println("Exception is ;" + e + ": message is " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Exception is ;" + e + ": message is " + e.getMessage());
        }
        // Return todays date If there are errors
        return dateFormat.format(new Date());
    }
    
}
