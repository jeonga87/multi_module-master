package kr.co.demo.filter;

import kr.co.demo.config.Constants;
import kr.co.demo.util.EnvironmentUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class DeviceDetectFilter extends OncePerRequestFilter {

    private static final String CURRENT_USER_SITE_TYPE_KEY = "currentSiteType";

    private DeviceResolver deviceResolver;

    private String pcDomain;

    private String mobileDomain;

    private String currentViewMode;

    private boolean disableFilter = false;

    protected void initFilterBean() throws ServletException {
        this.deviceResolver = new LiteDeviceResolver();

        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext
                (getServletContext());

        Environment env = applicationContext.getEnvironment();

        this.pcDomain = removeProtocol(env.getProperty("demo.url.pc-web"));
        this.mobileDomain = removeProtocol(env.getProperty("demo.url.mobile-web"));
        this.currentViewMode = EnvironmentUtil.isPcWeb() ? "desktop" : "mobile";

        String[] profiles = env.getActiveProfiles();
        for(String profile: profiles) {
            if(Constants.SpringProfileConst.PRODUCTION.equals(profile)) {
                disableFilter = false;
                break;
            }
        }
    }

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain)
            throws IOException, ServletException {

        if(disableFilter) {
            chain.doFilter(request, response);
            return;
        }

        boolean fromOuterLink = true;

        HttpSession session = request.getSession(false);

        // ???????????? ????????? ???, ???????????? ??????????????? ???????????? ????????????.
        // ?????? ??????????????? ????????? ?????? ????????????  redirect  ????????? ?????? ??????
        String preferredSiteType = request.getParameter("_pst_");   // _preferred_site_type_

        if(session != null) {
            if(StringUtils.equals(preferredSiteType, "desktop") || StringUtils.equals(preferredSiteType, "mobile")) {
                session.setAttribute(CURRENT_USER_SITE_TYPE_KEY, preferredSiteType);

            }else {
                preferredSiteType = (String)session.getAttribute(CURRENT_USER_SITE_TYPE_KEY);
            }
        }

        if(fromOuterLink) {
            Device device = deviceResolver.resolveDevice(request);
            if(device.isMobile() || device.isTablet()) {
                // ?????? ????????? ???????????? ????????? ????????? ??????
                if(StringUtils.equals(currentViewMode, "desktop")
                        && !StringUtils.equals(preferredSiteType, "desktop")) {

                    if(session != null) {
                        session.removeAttribute(CURRENT_USER_SITE_TYPE_KEY);
                    }

                    // ????????? ?????????????????? www.korswim.co.kr ??? ????????? m.korswim.co.kr?????? ??????
                    // ?????? ????????? WebApp ??? PC-Web ??????
                    String redirectUrl = "http://" + mobileDomain;
                    if(StringUtils.isNotBlank(request.getRequestURI())) {
                        redirectUrl += request.getRequestURI();
                    }
                    if(StringUtils.isNotBlank(request.getQueryString())) {
                        redirectUrl += "?" + request.getQueryString();
                    }
                    response.setHeader("Referer", "http://" + pcDomain);
                    response.sendRedirect(redirectUrl);
                    return;
                }
            }else {
                // ?????? ????????? ???????????? ???????????? ????????? ??????
                if(StringUtils.equals(currentViewMode, "mobile")
                        && !StringUtils.equals(preferredSiteType, "mobile")) {

                    if(session != null) {
                        session.removeAttribute(CURRENT_USER_SITE_TYPE_KEY);
                    }

                    String redirectUrl = "http://" + pcDomain;
                    if(StringUtils.isNotBlank(request.getRequestURI())) {
                        redirectUrl += request.getRequestURI();
                    }
                    if(StringUtils.isNotBlank(request.getQueryString())) {
                        redirectUrl += "?" + request.getQueryString();
                    }
                    response.setHeader("Referer", "http://" + mobileDomain);
                    response.sendRedirect(redirectUrl);
                    return;
                }
            }
        }

        chain.doFilter(request, response);

    }

    private String removeProtocol(String url) {
        url = StringUtils.removeStart(url, "http://");
        url = StringUtils.removeStart(url, "https://");
        return url;
    }

    private void setCurrentSiteType(HttpSession session, String siteType) {
        session.setAttribute(CURRENT_USER_SITE_TYPE_KEY, siteType);
    }

}
