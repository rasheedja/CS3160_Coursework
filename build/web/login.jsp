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
                    <form class="col-sm-6" method="POST" action="/coursework/do/login">
                        <div class="card">
                            <h2 class="card-header">Please log in!</h2>
                            <div class="card-body">
                                <div class="form-group col-sm-12">
                                    <label for="username">Username</label>
                                    <input type="text" name="username" class="form-control" placeholder="Username" />
                                </div>
                                <div class="form-group col-sm-12">
                                    <label for="password">Password</label>
                                    <input type="password" name="password" class="form-control" placeholder="Password" />      
                                </div>
                                <div class="form-group col-sm-12">
                                    <input type="submit" class="btn btn-primary" value="Click to log in" />
                                </div>
                            </div>
                        </div>
                    </form>
                    <form class="col-sm-6" method="POST" action="/coursework/do/addUser">
                        <div class="card">
                        <h2 class="col-sm-12 card-header"> Don't yet have an account? </h2>
                        <div class="card-body">
                            <div class="form-group col-sm-12">
                                <label for="newUsername">Username</label>
                                <input type="text" name="newUsername" class="form-control" placeholder="Username"/>
                            </div>
                            <div class="form-group col-sm-12">
                                <label for="newPassowrd">Password</label>
                                <input type="password" name="newPassword" class="form-control" placeholder="Password" />      
                            </div>
                            <div class="form-group col-sm-12">
                                <input type="submit" class="btn btn-primary" value="Sign up as a new user"/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </body>
</html>
