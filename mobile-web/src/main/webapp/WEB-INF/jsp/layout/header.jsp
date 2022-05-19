<header>
  <c:choose>
    <c:when test="${isAuthenticated}">
      [${currentMember.name}]님 환영합니다. <a href="${cp}/logout">로그아웃</a>
    </c:when>
    <c:otherwise>
      <a href="${cp}/login">로그인</a>
    </c:otherwise>
  </c:choose>
</header>
