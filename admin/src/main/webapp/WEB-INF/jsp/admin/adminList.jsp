<!-- 우측 컨텐츠 -->
<div id="contents">
  <h2>관리자 게시판 관리 &gt; 관리자 게시판 조회</h2>

  <form method="post" id="searchForm" name="searchForm" onsubmit="return false;">
    <input type="hidden" id="pageNo" name="pageNo" value="${admin.pageNo}" />
    <table>
      <caption>관리자 게시판 검색 폼</caption>
      <colgroup>
        <col style="width:130px;">
        <col style="width:auto;">
        <col style="width:130px;">
        <col style="width:auto;">
      </colgroup>
      <tbody>
      <tr>
        <th colspan="3">관리자 게시판 검색</th>
      </tr>
      <tr>
        <td>
          <div>
            <label for="search"></label>
            <select id="search" name="searchInfo['search']" title="회원종류목록" class="w150">
              <option ${admin.searchInfo['search'] eq 'all' ? 'selected' : ''} value="all">전체</option>
              <option ${admin.searchInfo['search'] eq 'name' ? 'selected' : ''} value="name">이름</option>
              <option ${admin.searchInfo['search'] eq 'id' ? 'selected' : ''} value="id">아이디</option>
            </select>
          </div>
        </td>
        <td>
          <input type="text" id="searchValue" name="searchValue" onkeydown="$.inputKeydownEvent(adminGoList);" placeholder="검색어를 입력해주세요." class="w390 mgl8" value="${admin.searchValue}">
        </td>
        <td>
          <a href="javascript:adminGoList();" class="btn w90 h30 dark mgl8">조회</a>
        </td>
      </tr>
      </tbody>
    </table>

    <div>
      Total <strong>${totalCount}</strong>

      <select id="dataPerPage" name="dataPerPage" title="리스트수" onchange="adminGoList();">
        <c:forEach var="item" items="${dataPerPageArray}">
          <option ${admin.dataPerPage eq item ? 'selected' : ''} value="${item}">${item}개</option>
        </c:forEach>
      </select>
    </div>
    <table>
      <caption>관리자 게시판 조회 목록</caption>
      <colgroup>
        <col style="width:50px;">
        <col style="width:auto;">
        <col style="width:120px;">
        <col style="width:120px;">
        <col style="width:120px;">
        <col style="width:120px;">
      </colgroup>

      <thead>
      <tr>
        <th></th>
        <th>아이디</th>
        <th>이름</th>
        <th>휴대폰 번호</th>
        <th>이메일</th>
        <th>가입일자</th>
      </tr>
      </thead>
      <tbody>
      <c:if test="${empty adminList}">
        <tr>
          <td colspan="6">데이터가 없습니다.</td>
        </tr>
      </c:if>
      <c:forEach var="admin" items="${adminList}" varStatus="status">
        <tr>
          <td>${totalCount - ((admin.pageNo -1) * 10 + status.index)}</td>
          <td><a href="javascript:;" onclick="adminDetail('${admin.idx}');">${admin.id}</a></td>
          <td>${admin.name}</td>
          <td>${tags:decrypt(admin.phone)}</td>
          <td>${tags:decrypt(admin.email)}</td>
          <td><fmt:formatDate value="${admin.createdDt}" pattern="yyyy.MM.dd"/></td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </form>

  <div style="text-align: right">
    <a href="/admin/new">등 록</a>
  </div>
  <!-- 페이징 -->
  <div style="text-align: center">
    <tags:pagination paginationInfo="${pagination}" jsFunction="adminList" />
  </div>
  <!--// 페이징 -->
</div>
<!--// 우측 컨텐츠 -->
<style>
  table {
    border-collapse: collapse;
    width: 100%;
  }

  table, td, th {
    border: 1px solid black;
  }
</style>
<script type="text/javascript">
  function adminGoList() {
    if($.trim($('#searchValue').val()) == '') {
      $('#searchValue').focus();
      //alert('검색어를 입력해 주세요.');
      //return false;s
    }

    $('#pageNo').val(1);
    searchForm.action = '/admin/list';
    searchForm.submit();
  }

  function adminList(pageNo) {
    $('#pageNo').val(pageNo);

    searchForm.action = '/admin/list';
    searchForm.submit();
  }

  function adminDetail(idx) {
    searchForm.action = '/admin/modify/'+idx;
    searchForm.submit();
  }
</script>