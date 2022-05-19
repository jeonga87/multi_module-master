package kr.co.demo.attach.filter;

import java.awt.*;

public abstract class AbstractImageFilter extends AbstractAttachFilter {


    public Dimension getResizeDimension(Dimension targetDimension, int maxWidth, int maxHeight) {
        double width = 0;
        double height = 0;

        boolean fitToWidth = Boolean.FALSE;
        if(targetDimension.getWidth() > maxWidth || targetDimension.getHeight() > maxHeight) {
            if(maxWidth > 0 && maxHeight > 0) {
                double ratio = maxHeight / maxWidth;
                if(targetDimension.getWidth() * ratio < targetDimension.getHeight()) {
                    fitToWidth = Boolean.TRUE;
                }
            }else if(maxWidth > 0 && targetDimension.getWidth() > maxWidth) {
                fitToWidth = Boolean.TRUE;
            }else if(maxHeight > 0 && targetDimension.getHeight() > maxHeight) {
                fitToWidth = Boolean.FALSE;
            }else {
                return targetDimension;
            }
        }else {
            return targetDimension;
        }

        if(fitToWidth == Boolean.TRUE) {
            width = maxWidth;
            height = maxWidth * (targetDimension.getHeight() / targetDimension.getWidth());
        }else if(fitToWidth == Boolean.FALSE) {
            height = maxHeight;
            width = maxHeight * (targetDimension.getWidth() / targetDimension.getHeight());
        }

        return new Dimension((int)width, (int)height);
    }

}
