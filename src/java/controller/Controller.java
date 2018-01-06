/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Lesson;
import model.LessonSelection;

import model.LessonTimetable;
import model.Users;

/**
 *
 * @author bastinl
 */
public class Controller extends HttpServlet {

   private Users users;
   private LessonTimetable availableLessons;

    public void init() {
         users = new Users();
         availableLessons = new LessonTimetable();
         // Attach the lesson timetable to the application scope
         this.getServletContext().setAttribute("availableLessons", availableLessons);
        
    }
    
    public void destroy() {
        
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the context path which is used to determine the action to perform
        String action = request.getPathInfo();
        RequestDispatcher dispatcher = null;
        boolean forwardRequest = true;
        
        if (action.equals("/login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            int clientID = users.isValid(username, password);
            
            /**
             * Valid users are those with a clientID other than -1. For valid users,
             * we create a session for them. In the session, we store the username,
             * the users lesson selections, and redirect the user to the timetable
             * view. Invalid users are redirected back to the login page.
             */
            if (clientID != -1) {
                HttpSession session = request.getSession();
                LessonSelection lessonSelection = new LessonSelection(clientID);
                
                session.setAttribute("name", username);
                session.setAttribute("lessonSelection", lessonSelection);
                
                dispatcher = this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");
            } else {
                dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
            }
        } else if (action.equals("/newUser")) {
            // Create a user with the given username and password
            String username = request.getParameter("newUsername");
            String password = request.getParameter("newPassword");
            users.addUser(username, password);
            dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
        } else if (action.equals("/checkName")) {
            String username = request.getParameter("username");
            boolean exists = users.doesNameExist(username);
            
            String responseData = "{\"nameExists\": \"" + String.valueOf(exists) + "\"}";
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(responseData);
            
            // Ensure that the request is not forwarded
            forwardRequest = false;
        } else {
            HttpSession session = request.getSession(false);
            /**
             * If a user who is not logged in tries to access another page,
             * redirect them to the login page. We know which users are logged
             * in because all logged in users should have a valid session
             * containing their lesson selection and their name.
             */
            if (session == null || (session.getAttribute("lessonSelection") == null) || session.getAttribute("name") == null) {
                dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
            } else {
                if (action.equals("/viewTimetable")) {
                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");
               
                } else if (action.equals("/chooseLesson")) {
                    /**
                     * When a user chooses a lesson, the lesson is stored in their
                     * session and they are redirected to the LessonSelectionView.
                     * If the user has already selected the lesson, no new entry is
                     * added
                     */
                    String lessonID = request.getParameter("lessonID");
                    LessonSelection lessonSelection = (LessonSelection) session.getAttribute("lessonSelection");

                    // Only add the lesson if it has not already been selected
                    if (lessonSelection.getLesson(lessonID) == null) {
                        lessonSelection.addLesson(availableLessons.getLesson(lessonID));
                    }

                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");
           
                } else if (action.equals("/viewSelection")) {
                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");
                
                } else if (action.equals("/finaliseBooking")) {
                    // Update the database with the users selection and redirect them to the LessonTimetableView
                    LessonSelection lessonSelection = (LessonSelection) session.getAttribute("lessonSelection");
                    lessonSelection.updateBooking();
                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");
                
                } else if (action.equals("/cancelLesson")) {
                    // Cancel the lesson selected by the user
                    String lessonID = request.getParameter("lessonID");
                    LessonSelection lessonSelection = (LessonSelection) session.getAttribute("lessonSelection");
                    lessonSelection.removeLesson(lessonID);

                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");
                    
                } else if (action.equals("/lessonCalendar")) {
                    dispatcher = this.getServletContext().getRequestDispatcher("/LessonCalendar.jspx");

                } else if (action.equals("/logOut")) {
                    // Invalidate the users session and redirect them to the login page
                    session.invalidate();
                    dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
                
                } else {
                    // Redirect unrecognised actions to the login page
                    dispatcher = this.getServletContext().getRequestDispatcher("/login.jsp");
                }
            }
        }
        
        if (forwardRequest) {
            // Redirect the user to the chosen page
            dispatcher.forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
