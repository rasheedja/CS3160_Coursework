<%-- 
    Document   : index
    Created on : 15-Mar-2010, 14:47:22
    Author     : bastinl
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/coursework/libraries/bootstrap/css/bootstrap.min.css" />
        <script type="text/javascript" src="/coursework/libraries/jquery/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="/coursework/libraries/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script type="text/javascript">
            function checkUsername(newUsername) {
                // Usernames must be at least 8 characters long
                if (newUsername.length > 7) {
                    $.ajax({
                        method: "GET",
                        url: "/coursework/do/checkName",
                        contentType: 'application/json',
                        data: {username: newUsername},
                        success: function(data) {
                            if (data.nameExists === 'true') {
                                // Display error message if username is taken
                                $('#errorMessage').text("The username " + newUsername + " already exists.");
                                $('#errorMessage').removeClass('d-none');
                                $('#newUserSubmit').prop('disabled', true);
                            } else {
                                // Enable the submit button and hide the error message
                                // (if it is shown) if username is available
                                $('#errorMessage').addClass('d-none');
                                $('#errorMessage').text("");
                                $('#newUserSubmit').prop('disabled', false);
                            }
                        },
                        error: function() {
                            // Display an error message if there is an error in the server
                            $('#errorMessage').text("There was an error when creating your account, please contact IT.");
                            $('#errorMessage').removeClass('d-none');
                        }
                    });
                } else {
                    // Display an error message and ensure the submit button is disabled
                    // if the username is too short
                    $('#errorMessage').text("The username " + newUsername + " is too short. Usernames must be at least 7 characters long");
                    $('#errorMessage').removeClass('d-none');
                    $('#newUserSubmit').prop('disabled', true);
                }
            }
        </script>
        <title>Login / signup page</title>
    </head>
    <body>
        <header class="jumbotron jumbotron-fluid bg-dark text-light">
            <div class="container">
                <h1 class="text-center">Aston Snowdome</h1>
            </div>
        </header>
        <main>
            <div class="container">
                <div class="row">
                    <!-- d-none is a bootstrap class that sets the CSS display attribute to none -->
                    <!-- This class is toggled on and off by JavaScript when necessary -->
                    <div id="errorMessage" class="col-sm-12 alert alert-danger d-none"></div>
                    <form class="col-sm-6" method="POST" action="/coursework/do/login">
                        <div class="card">
                            <h2 class="card-header">Please log in!</h2>
                            <div class="card-body">
                                <div class="form-group col-sm-12">
                                    <label for="username">Username</label>
                                    <input type="text" name="username" class="form-control" placeholder="Username" required="true" />
                                </div>
                                <div class="form-group col-sm-12">
                                    <label for="password">Password</label>
                                    <input type="password" name="password" class="form-control" placeholder="Password" required="true" />      
                                </div>
                                <div class="form-group col-sm-12">
                                    <input type="submit" class="btn btn-primary" value="Click to log in" />
                                </div>
                            </div>
                        </div>
                    </form>
                    <form class="col-sm-6" method="POST" action="/coursework/do/newUser">
                        <div class="card">
                        <h2 class="col-sm-12 card-header"> Don't yet have an account? </h2>
                        <div class="card-body">
                            <div class="form-group col-sm-12">
                                <label for="newUsername">Username</label>
                                <input type="text" name="newUsername" class="form-control" placeholder="Username" onChange="checkUsername(this.value)" required="true" />
                            </div>
                            <div class="form-group col-sm-12">
                                <label for="newPassowrd">Password</label>
                                <input type="password" name="newPassword" class="form-control" placeholder="Password" required="true" />      
                            </div>
                            <div class="form-group col-sm-12">
                                <input type="submit" id="newUserSubmit" class="btn btn-primary" value="Sign up as a new user" disabled="true" />
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </body>
</html>
