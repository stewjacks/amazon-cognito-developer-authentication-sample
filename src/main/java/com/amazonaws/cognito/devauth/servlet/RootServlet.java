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
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.amazonaws.cognito.devauth.Constants;
import main.java.com.amazonaws.cognito.devauth.AWSCognitoDeveloperAuthenticationLogger;
import main.java.com.amazonaws.cognito.devauth.exception.MissingParameterException;
import main.java.com.amazonaws.cognito.devauth.identity.AWSCognitoDeveloperAuthentication;

/**
 * An abstract class for AWSCognitoDeveloperAuthentication servlets.
 */
public abstract class RootServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected static final Logger log = AWSCognitoDeveloperAuthenticationLogger.getLogger();

    /**
     * A AWSCognitoDeveloperAuthentication instance.
     */
    protected AWSCognitoDeveloperAuthentication auth;

    @Override
    public void init() throws ServletException {
        super.init();
        auth = new AWSCognitoDeveloperAuthentication();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

    protected String getServletParameter(HttpServlet servlet, String parameterName) {
        String parameterValue = servlet.getInitParameter(parameterName);
        if (parameterValue == null) {
            parameterValue = servlet.getServletContext().getInitParameter(parameterName);
        }

        return parameterValue;
    }

    protected String getRequiredParameter(HttpServletRequest request, String parameterName)
            throws MissingParameterException {
        String value = request.getParameter(parameterName);
        if (value == null) {
            throw new MissingParameterException(parameterName);
        }

        value = value.trim();
        if (value.length() == 0) {
            throw new MissingParameterException(parameterName);
        }
        else {
            return value;
        }
    }
    
    protected String getParameter(HttpServletRequest request, String parameterName) {
        return request.getParameter(parameterName);
    }

    public void forward(HttpServletRequest request, HttpServletResponse response, String url)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }

    public void sendErrorResponse(int httpResponseCode, HttpServletResponse response) throws IOException {
        response.setStatus(httpResponseCode);
        response.setContentType("text/plain; charset=UTF-8");
        response.setDateHeader("Expires", System.currentTimeMillis());

        ServletOutputStream out = response.getOutputStream();
        out.println(Constants.getMsg(httpResponseCode));
    }

    public void sendOKResponse(HttpServletResponse response, String data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain; charset=UTF-8");
        response.setDateHeader("Expires", System.currentTimeMillis());

        if (null != data) {
            ServletOutputStream out = response.getOutputStream();
            out.println(data);
        }
    }

}
