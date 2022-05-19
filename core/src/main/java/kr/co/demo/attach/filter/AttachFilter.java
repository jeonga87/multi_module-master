package kr.co.demo.attach.filter;

import java.io.File;
import java.util.Map;

public interface AttachFilter {
    void config(Map<String, Object> context);

    void doFilter(AttachFilterChain chain, File file) throws Exception;
}
