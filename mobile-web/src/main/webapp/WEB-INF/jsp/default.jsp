<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%
  WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(((HttpServletRequest) request).getSession()
      .getServletContext());
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no,viewport-fit=cover">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
  <meta name="format-detection" content="telephone=no">
  <meta property="og:title" content="테스트">
  <meta property="og:type" content="website" />
  <meta property="og:site_name" content="테스트">
  <meta property="og:url" content="" />
  <meta property="og:description" content="테스트 페이지 입니다.">
  <meta name="description" content="테스트 페이지 입니다.">
  <meta name="keywords" content="테스트">
  <title>테스트</title>

  <%@ include file="/WEB-INF/jsp/layout/styles.jsp" %>


  <%@ include file="/WEB-INF/jsp/layout/scripts.jsp" %>

  <sitemesh:write property='head'/>

</head>
<body class="<sitemesh:write property='body.class' />">
  <div id="wrap">
    <%@ include file="/WEB-INF/jsp/layout/header.jsp" %>

    <%@ include file="/WEB-INF/jsp/layout/nav.jsp" %>

    <sitemesh:write property='body'/>

    <%@ include file="/WEB-INF/jsp/layout/footer.jsp" %>
  </div>
  <%= (request.getAttribute("scripts-tags") != null) ?  request.getAttribute("scripts-tags") : "" %>
  <%= (request.getAttribute("scripts") != null) ?  request.getAttribute("scripts") : "" %>
</body>
</html>
