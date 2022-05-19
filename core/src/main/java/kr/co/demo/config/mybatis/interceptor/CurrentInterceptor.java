package kr.co.demo.config.mybatis.interceptor;

import kr.co.demo.admin.domain.Admin;
import kr.co.demo.common.Base;
import kr.co.demo.security.SecurityUtils;
import kr.co.demo.util.CommonUtil;
import kr.co.demo.util.DateUtil;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * mybatis에서 query 실행시 특정 데이터를 파라미터에 넣어주는 Interceptor
 * query에서 current 변수를 호출해서 사용(예: #{current.dt})
 */
@Intercepts({
        @Signature(type = Executor.class, method="query", args={MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method="update", args={MappedStatement.class, Object.class})
})
public class CurrentInterceptor implements Interceptor {

    private static final int MAPPED_STATEMENT_INDEX = 0;
    private static final int PARAMETER_INDEX = 1;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();

        MappedStatement ms = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        Object params = args[PARAMETER_INDEX];

        /*
         * 파라미터에 추가되는 정보
         */
        Map<String, Object> current = new HashMap<>();

        if(SecurityUtils.isAdmin()) {
            // 1. 관리자 로그인
            Admin currentAdmin = SecurityUtils.getCurrentAdmin();
            current.put("adminYn", "Y");                             // 관리자 로그인 여부[Y=예]
            current.put("adminIdx", currentAdmin.getIdx());          // 관리자 로그인 계정 일련번호
            current.put("loginId", currentAdmin.getId());            // 관리자 로그인 계정 아이디
        } else {
            current.put("adminYn", "N");                             // 관리자 로그인 여부[N=아니오]
            // 2. 회원 로그인
        }

        // 3. 현재 시간
        current.put("dt", DateUtil.getCurrentDateTime());   // yyyy-MM-dd HH:mm:ss (String)

        // 4. 현재 요청 아이피
        if(RequestContextHolder.getRequestAttributes() != null) {
            current.put("ip", CommonUtil.getRealIp());      // xxx.xxx.xxx.xxx (String)
        }

        /*
         * 위에서 설정한 파라미터를 current 항목으로 주입한다.
         * (주의: 쿼리 실행시 parameter 객체가 Base, MapperMethod.ParamMap, HashMap 타입 세가지 중 하나로 존재해야 주입가능)
         */
        if (params instanceof Base) {
            Base entity = (Base) params;
            entity.setCurrent(current);
        } else if (params instanceof MapperMethod.ParamMap) {
            ((MapperMethod.ParamMap) params).put("current", current);
        } else if (params instanceof HashMap) {
            ((HashMap) params).put("current", current);
        }

        Object result = invocation.proceed();

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
