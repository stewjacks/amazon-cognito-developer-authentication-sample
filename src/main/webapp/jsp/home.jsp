<!--
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
-->

<%@page import="main.java.com.amazonaws.cognito.devauth.Constants"%>
<%@page import="main.java.com.amazonaws.cognito.devauth.Utilities"%>
<%@page
	import="main.java.com.amazonaws.cognito.devauth.Configuration"%>
<%@ page session="true"%>

<html>
<head>
<title>Amazon Cognito Developer Authentication - Welcome</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, minimum-scale=1.0, maximum-scale=1.0">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/jsp/css/styles.css"
	type="text/css" media="screen" charset="utf-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/jsp/css/styles-mobile.css"
	type="text/css" media="screen" charset="utf-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/jsp/css/styles-tablet.css"
	type="text/css" media="screen" title="no title" charset="utf-8">
</head>

<body class="success">
	<div id="header">
		<h1>Amazon Cognito Developer Authentication</h1>
	</div>

	<div id="body">
		<fieldset>
			<legend>Congratulations!</legend>
			<p class="message">You have successfully configured the Cognito
				Developer Authentication.</p>
		</fieldset>
		<fieldset>
			Next steps
			<ul>
				<li><a href="jsp/register.jsp">Register a user</a> to use in
					the app.</li>
			</ul>
		</fieldset>
	</div>

	<%
	    if (Utilities.isEmpty(Configuration.DEVELOPER_PROVIDER_NAME)
	            || Utilities.isEmpty(Configuration.IDENTITY_POOL_ID)) {
	%>
	<p class="warning">Warning: Your Cognito Developer Authentication
		 is not properly configured.</p>
	<%
	    }
	%>

	<%
	    int port = request.getServerPort();
	    if (443 != port) {
	        if (response instanceof HttpServletResponse) {
	%>
	<p class="warning">Warning: You are not running SSL.</p>
	<%
	    }
	    }
	%>

	<div id="footer">
		<p class="footnote"><%=Configuration.APP_NAME%>
			- AWSCognitoDeveloperAuthentication
		</p>
	</div>
</body>

</html>
