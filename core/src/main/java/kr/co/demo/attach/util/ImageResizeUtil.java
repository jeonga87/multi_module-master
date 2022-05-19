package kr.co.demo.attach.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ImageResizeUtil {
    private final static Logger log = LoggerFactory.getLogger(ImageResizeUtil.class);

    //scale limit to when apply 2-phase downscaling or not
    public static final float SCALE_LIMIT = 0.25f;

    public static BufferedImage resizeJava2DNative(BufferedImage source, int destWidth, int destHeight,
                                                   Object interpolation) {
        if (source == null)
            throw new NullPointerException("source image is NULL!");
        if (destWidth <= 0 && destHeight <= 0)
            throw new IllegalArgumentException("destination width & height are both <=0!");
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        double xScale = ((double) destWidth) / (double) sourceWidth;
        double yScale = ((double) destHeight) / (double) sourceHeight;
        if (destWidth <= 0) {
            xScale = yScale;
            destWidth = (int) Math.rint(xScale * sourceWidth);
        }
        if (destHeight <= 0) {
            yScale = xScale;
            destHeight = (int) Math.rint(yScale * sourceHeight);
        }
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(destWidth, destHeight, source.getColorModel().getTransparency());
        Graphics2D g2d = null;
        try {
            g2d = result.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
            AffineTransform at =
                    AffineTransform.getScaleInstance(xScale, yScale);
            g2d.drawRenderedImage(source, at);
        } finally {
            if (g2d != null)
                g2d.dispose();
        }
        return result;
    }

    public static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }


    public static void writeJPEG(BufferedImage input, String name) throws IOException {
        writeJPEG(input, new File(name));
    }

    public static void writeJPEG(BufferedImage input, File file) throws IOException {
        Iterator iter = ImageIO.getImageWritersByFormatName("JPG");
        if (iter.hasNext()) {
            ImageWriter writer = (ImageWriter) iter.next();
            ImageWriteParam iwp =  writer.getDefaultWriteParam();
            if (iwp.canWriteCompressed()) {
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwp.setCompressionQuality(0.95f);
            }
            FileImageOutputStream output = new FileImageOutputStream(file);
            writer.setOutput(output);
            IIOImage image = new IIOImage(input, null, null);
            writer.write(null, image, iwp);
            output.close();
        }else {
            throw new IOException("no writer found for JPG");
        }
    }

    public static void write(BufferedImage input, File file, String mimeType) throws IOException {
        Iterator iter = ImageIO.getImageWritersByMIMEType(mimeType);

        if (iter.hasNext()) {
            ImageWriter writer = (ImageWriter) iter.next();
            ImageWriteParam iwp =  writer.getDefaultWriteParam();
            if (iwp.canWriteCompressed()) {
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwp.setCompressionQuality(0.95f);
            }
            FileImageOutputStream output = new FileImageOutputStream(file);
            writer.setOutput(output);
            IIOImage image = new IIOImage(input, null, null);
            writer.write(null, image, iwp);
            output.close();
        }else {
            throw new IOException("no writer found for mime-type : " + mimeType);
        }
    }
}
