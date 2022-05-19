<!-- 우측 컨텐츠 -->
<div id="contents">
    <h2>예제 게시판 관리 &gt; 예제 게시판 조회</h2>

    <form method="post" id="searchForm" name="searchForm" onsubmit="return false;">
        <input type="hidden" id="pageNo" name="pageNo" value="${example.pageNo}" />
        <table>
            <caption>예제 게시판 검색 폼</caption>
            <colgroup>
                <col style="width:130px;">
                <col style="width:auto;">
                <col style="width:130px;">
                <col style="width:auto;">
            </colgroup>
            <tbody>
            <tr>
                <th colspan="3">예제 게시판 검색</th>
            </tr>
            <tr>
                <td>
                    <div>
                        <label for="search"></label>
                        <select id="search" name="searchInfo['search']" title="회원종류목록" class="w150">
                            <option ${example.searchInfo['search'] eq 'title' ? 'selected' : ''} value="title">제목</option>
                            <option ${example.searchInfo['search'] eq 'content' ? 'selected' : ''} value="content">내용</option>
                        </select>
                    </div>
                </td>
                <td>
                    <input type="text" id="searchValue" name="searchValue" onkeydown="$.inputKeydownEvent(exampleGoList);" placeholder="검색어를 입력해주세요." class="w390 mgl8" value="${example.searchValue}">
                </td>
                <td>
                    <a href="javascript:exampleGoList();" class="btn w90 h30 dark mgl8">조회</a>
                </td>
            </tr>
            </tbody>
        </table>

        <div>
            Total <strong>${totalCount}</strong>

            <select id="dataPerPage" name="dataPerPage" title="리스트수" onchange="exampleGoList();">
                <c:forEach var="item" items="${dataPerPageArray}">
                    <option ${example.dataPerPage eq item ? 'selected' : ''} value="${item}">${item}개</option>
                </c:forEach>
            </select>
        </div>
        <table>
            <caption>예제 게시판 조회 목록</caption>
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
                    <th>번호</th>
                    <th>제목</th>
                    <th>첨부파일</th>
                    <th>사용여부</th>
                    <th>생성자</th>
                    <th>생성일</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty exampleList}">
                    <tr>
                        <td colspan="6">데이터가 없습니다.</td>
                    </tr>
                </c:if>
                <c:forEach var="example" items="${exampleList}" varStatus="status">
                    <tr>
                        <td>${status.index+1}</td>
                        <td><a href="javascript:;" onclick="exampleDetail('${example.idx}')">${example.title}</a></td>
                        <td>
                            <c:if test="${example.attachIdx ne null}">Y</c:if>
                        </td>
                        <td>${example.useYn}</td>
                        <td>${example.createdBy}</td>
                        <td><fmt:formatDate value="${example.createdDt}" pattern="yyyy-MM-dd"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </form>

    <div style="text-align: right">
        <a href="/example/new">등 록</a>
    </div>
    <!-- 페이징 -->
    <div style="text-align: center">
        <tags:pagination paginationInfo="${pagination}" jsFunction="exampleList" />
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
    function exampleGoList() {
        if($.trim($('#searchValue').val()) == '') {
            $('#searchValue').focus();
            //alert('검색어를 입력해 주세요.');
            //return false;s
        }

        $('#pageNo').val(1);
        searchForm.action = '/example/list';
        searchForm.submit();
    }

    function exampleList(pageNo) {
        $('#pageNo').val(pageNo);

        searchForm.action = '/example/list';
        searchForm.submit();
    }

    function exampleDetail(idx) {
        searchForm.action = '/example/modify/'+idx;
        searchForm.submit();
    }
 </script>