<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String imgDomain = request.getContextPath();
	request.setAttribute("imgDomain", imgDomain);
%>