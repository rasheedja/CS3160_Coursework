/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

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
                    
                    // TODO instantiate and populate the 'lessons' HashMap by selecting the relevant infromation from the database
                  
                    lessons = new HashMap<String, Lesson>();
            
                    // TODO add code here to retrieve the infromation and create the new Lesson objects
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
    
}
