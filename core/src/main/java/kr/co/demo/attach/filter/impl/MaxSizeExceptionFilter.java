package kr.co.demo.attach.filter.impl;

import kr.co.demo.attach.filter.AttachFilter;
import kr.co.demo.attach.filter.AttachFilterChain;

import java.io.File;
import java.util.Map;

public class MaxSizeExceptionFilter implements AttachFilter {
    public static final String PARAM_MAX_SIZE = "maxSize";

    public long maxSize = 0;
    public void config(Map<String, Object> context) {
        if(context.containsKey(PARAM_MAX_SIZE)) {
            maxSize = (long)context.get(PARAM_MAX_SIZE);
        }

    }

    public void doFilter(AttachFilterChain chain, File file) throws Exception {
        if(file.length() > maxSize) {
            throw new MaxSizeExceedException("max size exceeded");
        }

        chain.doFilter(file);
    }

    public class MaxSizeExceedException extends Exception {
        /**
         * Constructor for MaxSizeExceedException.
         *
         * @param message exception message
         */
        public MaxSizeExceedException(final String message) {
            super(message);
        }
    }
}
