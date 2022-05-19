package kr.co.demo.attach.filter.impl;

import kr.co.demo.attach.filter.AbstractImageFilter;
import kr.co.demo.attach.filter.AttachFilterChain;
import kr.co.demo.attach.util.ImageResizeUtil;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class ResizeImageFilter extends AbstractImageFilter {
    public static final String PARAM_MAX_WITH = "maxWith";
    public static final String PARAM_MAX_HEIGHT = "maxHeight";
    public static final String PARAM_ENGINE = "engine";

    private int maxWith = 0;
    private int maxHeight = 0;

    public void config(Map<String, Object> context) {
        if(context.containsKey(PARAM_MAX_WITH)) {
            maxWith = (int)context.get(PARAM_MAX_WITH);
        }
        if(context.containsKey(PARAM_MAX_HEIGHT)) {
            maxHeight = (int)context.get(PARAM_MAX_HEIGHT);
        }
    }

    public void doFilter(AttachFilterChain chain, File file) throws Exception {
        BufferedImage bi = ImageIO.read(file);
        int actualWidth = bi.getWidth();
        int actualHeight = bi.getHeight();
        Dimension actualDimension = new Dimension(actualWidth, actualHeight);
        Dimension tobeDimension = getResizeDimension(actualDimension, maxWith, maxHeight);
        if(actualDimension.equals(tobeDimension)) {
            chain.doFilter(file);
            return;
        }

        File tempOutFile = createTempOutFile(file);

        BufferedImage resizedBi;
        resizedBi = Scalr.resize(bi, Scalr.Method.ULTRA_QUALITY, (int)tobeDimension.getWidth(), (int)tobeDimension.getHeight());

        ImageResizeUtil.write(resizedBi, tempOutFile, chain.getContentType());

        moveTempFileToOriginalFile(tempOutFile, file);

        chain.doFilter(file);
    }

}
