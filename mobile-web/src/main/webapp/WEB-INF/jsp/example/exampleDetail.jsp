<!-- 우측 컨텐츠 -->
<div id="contents">
    <c:choose>
        <c:when test="${example.idx == null}"><h2>예제 게시판 권한 관리 &gt; 예제 게시판 조회</h2></c:when>
        <c:otherwise><h2>예제 게시판 권한 관리 &gt; 예제 게시판 수정</h2></c:otherwise>
    </c:choose>
    <form method="post" id="submitForm" name="submitForm" enctype="multipart/form-data">
        <input type="hidden" name="idx" value="${example.idx}"/>
        <input type="hidden" name="searchInfo['search']" value="${example.searchInfo['search']}"/>
        <input type="hidden" name="searchValue" value="${example.searchValue}"/>
        <input type="hidden" name="pageNo" value="${example.pageNo}"/>
        <table>
            <caption>예제 게시판 수정 폼</caption>
            <colgroup>
                <col style="width:130px;">
                <col style="width:auto;">
            </colgroup>
            <tbody>
                <tr>
                    <th scope="row"><label for="title">제목</label></th>
                    <td><input type="text" id="title" name="title" style="width:100%;" placeholder="제목을 입력해주세요" title="제목 입력" value="${example.title}"></td>
                </tr>
                <tr>
                    <th scope="row"><label for="exampleContent">내용</label></th>
                    <td class="align_l">
                        <tags:smarteditor id="exampleContent" name="content" content="${example.content}" />
                    </td>
                </tr>
                <tr>
                    <th scope="row">사용상태</th>
                    <td>
                        <div class="rad`io_cont">
                            <input type="radio" id="useYn1" name="useYn" value="Y" ${example.useYn eq 'Y' ? 'checked' : ''} <c:if test="${example.useYn == null}">checked</c:if> > <label for="useYn1">사용함</label>
                            <input type="radio" id="useYn2" name="useYn" value="N" ${example.useYn eq 'N' ? 'checked' : ''} > <label for="useYn2">사용안함</label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th scope="row">첨부파일</th>
                    <td>
                        <div class="file-add">
                            <!-- 첨부파일 기본 템플릿 -->
                            <jsp:include page="/WEB-INF/jsp/common/attachTemplateDefault.jsp">
                                <jsp:param name="refType" value="example" />
                                <jsp:param name="mapCode" value="attach" />
                                <jsp:param name="index" value="1" />
                                <jsp:param name="isSingle" value="N" />
                            </jsp:include>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th scope="row">썸네일 이미지</th>
                    <td>
                    <!-- 썸네일 이미지 등록 -->
                        <!-- 첨부파일 기본 템플릿 -->
                        <div class="file-add">
                        <jsp:include page="/WEB-INF/jsp/common/attachTemplateDefault.jsp">
                            <jsp:param name="refType" value="example" />
                            <jsp:param name="mapCode" value="thumb" />
                            <jsp:param name="index" value="2" />
                            <jsp:param name="isSingle" value="Y" />
                        </jsp:include>
                        </div>
                    <!--// 썸네일 이미지 등록 -->
                </td>
                </tr>
                <tr>
                    <th>이미지 하나</th>
                    <td>
                        <div class="file-add">
                        <jsp:include page="/WEB-INF/jsp/common/attachTemplateDefault.jsp">
                            <jsp:param name="refType" value="example" />
                            <jsp:param name="mapCode" value="image" />
                            <jsp:param name="index" value="3" />
                            <jsp:param name="isSingle" value="Y" />
                        </jsp:include>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    </form>

    <div class="btnC pdt30">
        <a href="${cp}/example/list" class="btn w140 h40 dgray">목 록</a>
        <c:choose>
            <c:when test="${example.idx == null}"><a href="javascript:;" onclick="$.exampleSave();" class="btn w140 h40 orange">등 록</a></c:when>
            <c:otherwise>
                <a href="javascript:;" onclick="$.exampleSave();" class="btn w140 h40 orange">수 정</a>
                <a href="javascript:;" class="btn w140 h40 dark del">삭 제</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<style>
    table {
        border-collapse: collapse;
        width: 100%;
    }

    table, td, th {
        border: 1px solid black;
    }
</style>
<!--// 우측 컨텐츠 -->
<script type="text/javascript">
    var isCreateYn = '${empty example.idx ? 'Y' : 'N'}';
    var mode = {
        code: 'insert',
        name: '등록'
    };
    if(isCreateYn == 'N') {
        mode = {
            code: 'update',
            name: '수정'
        };
    }

    var attachOption = {
        refType: 'example',
        refKey: '${example.idx}',
        mapCodes: [
            {
                mapCode: 'attach',
                size: 1,
                exts: ['hwp','pdf','docs','jpg','png','xls','ppt']
            },
            {
                mapCode: 'thumb',
                size: 10,
                exts: ['jpg','png']
            },
            {
                mapCode: 'image',
                size: 10,
                exts: ['jpg','png']
            }
        ]
    };

    $(document).ready(function() {
        window.$attach.init(attachOption);

        // 등록
        $.exampleSave = function(){
            if($.trim($('input[name=title]').val()) == ''){
                $('input[name=title]').focus();
                alert('제목을 입력해 주세요.');
                return false;
            }

            window.oEditors.getById["exampleContent"].exec("UPDATE_CONTENTS_FIELD", []);
            var ir1 = document.getElementById("exampleContent").value;
            if(ir1 == ""  || ir1 == null || ir1 == '&nbsp;' || ir1 == '<p>&nbsp;</p>' || ir1 == '<p><br></p>') {
                window.oEditors.getById["exampleContent"].exec("FOCUS"); //포커싱
                alert('내용을 입력해 주세요.');
                return;
            }

            if(confirm('예제 게시판을 ' + mode.name + ' 하시겠습니까?') == true) {
                exampleSubmit('/example/'+mode.code);
            } else { }
        }

        $('.del').click(function() {
            if(confirm('예제 게시판을 삭제 하시겠습니까?') == true) {
                exampleSubmit('/example/delete');
            } else { }
        });
    });

    // 등록/수정
    function exampleSubmit(url) {
        $.ajaxJson(url, {
            form: $('#submitForm')[0],		// 전송할 form object
            attachRefType: 'example',	// 전송할 첨부파일 정보의 refType
            success: function (data) {
                alert( mode.name + '이 완료되었습니다.');
                location.href = '/example/list';
            }
        });
    }
</script>