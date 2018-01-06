/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.PreparedStatement;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author bastinl
 */
public class Users {
  
    private ResultSet rs = null;
    private PreparedStatement pstmt = null;
    DataSource ds = null;
   
    public Users() {
        
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
        
    }

    public int isValid(String name, String pwd) {
       
        try {
            
            Connection connection = ds.getConnection();
            System.out.println(connection);
            if (connection != null) {
               /**
                * Check the database if the username and password combination is
                * correct. If it is, return the clientID value for the user. If not,
                * return -1. A PreparedStatement Object is used to protect 
                * against SQL injection.
                */
               
               pstmt = connection.prepareStatement("SELECT * FROM clients WHERE username = ? AND password = ?");
               pstmt.setString(1, name);
               pstmt.setString(2, pwd);
               rs = pstmt.executeQuery();
               
               if (rs.next()) {
                   return rs.getInt(1); // Return the first field, i.e. the clentID
               } else {
                   return -1;
               }
            } else {
                return -1;
            }
        } catch(SQLException e) {
                    
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            return -1;
        }
        
        
    }
    
    public boolean addUser(String name, String pwd) {
        try {
            Connection connection = ds.getConnection();
            if (connection != null) {
                
                pstmt = connection.prepareStatement("INSERT INTO clients (username, password) VALUES (?,?)");
                pstmt.setString(1, name);
                pstmt.setString(2, pwd);

                int success = pstmt.executeUpdate();

                // Success is 1 if a row was updated, and 0 if not
                if (success == 1) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch(SQLException e) {
                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
               return false;
        }
        return false;
    }
    
    /**
     * Check if the given name exists in the database
     * 
     * @param name The name to check
     * @return True if the name exists, False otherwise
     */
    public boolean doesNameExist(String name) {
        try {
            Connection connection = ds.getConnection();
            if (connection != null) {
                pstmt = connection.prepareStatement("SELECT * FROM clients WHERE username = ?");
                pstmt.setString(1, name);
                rs = pstmt.executeQuery();
                
                // If the result returns any data, the username exists in the database
                return rs.next();

            }
        } catch (SQLException e) {
            System.out.println("Exception is ;" + e + ": message is " + e.getMessage());
            return false;
        }
        return false;
    }
}
