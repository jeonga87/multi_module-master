<header>
  <c:if test="${isAuthenticated}">
    [${currentAdmin.name}]님 환영합니다. <a href="${cp}/logout">로그아웃</a>
  </c:if>
</header>
