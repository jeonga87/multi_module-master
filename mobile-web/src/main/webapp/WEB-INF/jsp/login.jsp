<head>
    <meta name="decorator" content="/WEB-INF/jsp/empty.jsp"/>
</head>

<div id="wrap" role="login">
    <h1 class="hide">사용자</h1>

    <section id="cont-wrap">
        <h1 class="hide">페이지 영역</h1>
        <section class="login">
            <h1>사용자 전용 페이지 입니다.</h1>
            <form id="loginForm" name="loginForm">
                <p><input type="text" name="id" id="id" placeholder="아이디" maxlength="20"></p>
                <p><input type="password" name="pwd" id="pwd" placeholder="비밀번호" maxlength="20"></p>
                <p><a href="javascript:;" onclick="loginGo();">로그인</a></p>
            </form>
        </section><!--//login E-->
    </section>
</div>

<script type="text/javascript">
    function loginGo() {
        if($.trim($('input[name=id]').val()) == ''){
            $('input[name=id]').focus();
            alert('아이디를 입력해 주세요.');
            return false;
        }

        if($.trim($('input[name=pwd]').val()) == ''){
            $('input[name=pwd]').focus();
            alert('비밀번호를 입력해 주세요.');
            return false;
        }

        $.ajax ( '${cp}/authentication', {
            type: 'POST',
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            data: $('#loginForm').serialize(),
            success: function (data) {
                location.href = '../..';
            },
            error: function (xhr) {
                let reason = xhr.getResponseHeader('X-Unauthorized-Reason');
                let msg = '아이디가 없거나 비밀번호가 일치하지 않습니다.';

                if ( reason == 'DISABLED ACCOUNT' ) {
                    msg = '탈퇴된 계정입니다.';
                } else if ( reason == 'NO ACCOUNT' ) {
                    msg = '존재하지 않는 계정입니다.';
                }
                alert(msg);
            }
        });
    }
</script>
