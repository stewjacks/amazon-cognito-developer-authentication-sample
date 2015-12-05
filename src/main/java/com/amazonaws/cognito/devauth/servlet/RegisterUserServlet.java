/*
 * Copyright 2010-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package main.java.com.amazonaws.cognito.devauth.servlet;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.amazonaws.cognito.devauth.Utilities;
import main.java.com.amazonaws.cognito.devauth.exception.DataAccessException;
import main.java.com.amazonaws.cognito.devauth.exception.MissingParameterException;

/**
 * Servlet implementation class UserRegisterServlet
 */
public class RegisterUserServlet extends RootServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.info("entering register user request");

        // Validate parameters
        log.info("Validate parameters");
        String username, password;
        try {
            username = getRequiredParameter(request, "username");
            password = getRequiredParameter(request, "password");
        } catch (MissingParameterException e) {
            log.warning("Missing parameter: " + e.getMessage() + ". Setting Http status code "
                    + HttpServletResponse.SC_BAD_REQUEST);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            forward(request, response, "/jsp/mpe.jsp");
            return;
        }

        String endpoint = Utilities.getEndPoint(request);
        log.info(String.format("Register user: username [%s], endpoint [%s]", username, endpoint));

        log.info("Validate username and password");
        if (!Utilities.isValidUsername(username) || !Utilities.isValidPassword(password)) {
            log.warning(String.format("Invalid parameters: username [%s] or password", username));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            forward(request, response, "/jsp/mpe.jsp");
            return;
        }

        try {
            log.info("Register user: " + username);
            boolean result = auth.registerUser(username, password, endpoint);
            if (!result) {
                log.warning(String.format("Duplicate registration [%s]", username));
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                forward(request, response, getServletParameter(this, "error"));
                return;
            }
        } catch (DataAccessException e) {
            log.log(Level.SEVERE, String.format("Failed to register user [%s]", username), e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            forward(request, response, getServletParameter(this, "error"));
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        log.info(String.format("User [%s] registered successfully", username));
        forward(request, response, getServletParameter(this, "success"));
        return;
    }
}
