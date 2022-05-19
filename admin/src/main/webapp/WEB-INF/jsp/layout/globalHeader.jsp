<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tlds/taglibs.tld" prefix="tags" %>

<c:set var="cp" value="${pageContext.request.contextPath}"/>

<spring:eval expression="T(kr.co.demo.security.SecurityUtils).isAdmin()" var="isAuthenticated" />
<spring:eval expression="T(kr.co.demo.security.SecurityUtils).getCurrentAdmin()" var="currentAdmin" />

<%-- 리스트 개수 목록 --%>
<c:set var="dataPerPageArray" value="${fn:split('10,20,30,40,50,60,70,80,90,100,150,200',',')}" />