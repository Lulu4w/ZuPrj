<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>出错啦！</h1>
<c:out escapeXml="false" value="${errorMsg }"></c:out>