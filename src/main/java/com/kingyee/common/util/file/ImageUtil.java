package com.kingyee.common.util.file;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * 保存图片
 *
 * @author 张宏亮
 * @version 2013-6-22
 */
public class ImageUtil {
    public static BufferedImage resize(BufferedImage source, int targetW, int targetH) {
        // targetW，targetH分别表示目标长和宽
        int type = source.getType();
        BufferedImage target = null;
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放
        // 则将下面的if else语句注释即可
        if (sx > sy) {
            sx = sy;
            targetW = (int) (sx * source.getWidth());
        } else {
            sy = sx;
            targetH = (int) (sy * source.getHeight());
        }
        if (type == BufferedImage.TYPE_CUSTOM) { // handmade
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else
            target = new BufferedImage(targetW, targetH, type);
        Graphics2D g = target.createGraphics();
        // smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    /**
     * @param fromFileStr
     * @param saveToFileStr
     * @param width
     * @param height
     * @throws Exception
     */
    public static void saveImage(String fromFileStr, String saveToFileStr, int width, int height)
            throws IOException {
        BufferedImage srcImage;
        //String ex = saveToFileStr.substring(saveToFileStr.lastIndexOf("."));
        String imgType = "JPEG";
        if (saveToFileStr.toLowerCase().endsWith(".png")) {
            imgType = "PNG";
        }
        File saveFile = new File(saveToFileStr);
        File fromFile = new File(fromFileStr);
        srcImage = ImageIO.read(fromFile);
        if (srcImage != null) {
            if (width > 0 || height > 0) {
                srcImage = resize(srcImage, width, height);
            }
            ImageIO.write(srcImage, imgType, saveFile);
        }

    }

    /**
     * 自动压缩,宽度大于1920时改分辨率压缩
     * 宽度小于1920时,不改分辨率压缩
     * 画质几乎不损失,大小减为原来的1/N
     * @param file
     * @param saveFile
     * @throws IOException
     */
    public static void saveImage(File file, File saveFile) throws IOException {
        BufferedImage srcImage;
        String imgType = "JPEG";
        if (file.getName().toLowerCase().endsWith(".png")) {
            imgType = "PNG";
        }
        saveFile.mkdirs();
        srcImage = ImageIO.read(file);
        if (srcImage != null) {
            if (srcImage.getWidth() > 1920) {
                srcImage = resize(srcImage, 1920, 1080);
            }
            ImageIO.write(srcImage, imgType, saveFile);
        }
    }

    public static void main(String argv[]) {
        try {
            // 参数1(from),参数2(to),参数3(宽),参数4(高)
            ImageUtil.saveImage("E:/Document/My Pictures/3.gif", "c:/6.gif", 50, 50);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
