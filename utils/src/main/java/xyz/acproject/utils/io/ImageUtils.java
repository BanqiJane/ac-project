package xyz.acproject.utils.io;

import net.coobird.thumbnailator.Thumbnails;
import xyz.acproject.utils.entity.param.ImageParam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jane
 * @ClassName ImageUtils
 * @Description TODO
 * @date 2021/2/13 12:03
 * @Copyright:2021
 */
public class ImageUtils {



    public static byte[] handleImage(InputStream inputStream, ImageParam param) {
        byte[] image_bytes = readInputStream(inputStream);
        BufferedImage bufferedImage = bytesToBufImage(image_bytes);
        if(param.getWidth()==null)param.setWidth(bufferedImage.getWidth());
        if(param.getHeight()==null)param.setHeight(bufferedImage.getHeight());
        if(param.getCw()==null)param.setCw(bufferedImage.getWidth());
        if(param.getCh()==null)param.setCh(bufferedImage.getHeight());
        try {
            if (param.getHandleType() == ImageParam.HandleType.CROP) {
                //CROP
                if (param.getX()!=null&&param.getY()!=null) {
                    bufferedImage = Thumbnails.of(bufferedImage).sourceRegion(param.getX(), param.getY(), param.getCw(), param.getCh())
                            .size(param.getWidth(), param.getHeight()).rotate(param.getRotate()).keepAspectRatio(param.isKeepAspectRatio())
                            .outputQuality(param.getQuality())
                            .outputFormat(param.getSuffix()).asBufferedImage();
                } else {
                    bufferedImage = Thumbnails.of(bufferedImage).sourceRegion(param.getPositions(), param.getCw(), param.getCh()).rotate(param.getRotate())
                            .size(param.getWidth(), param.getHeight()).keepAspectRatio(param.isKeepAspectRatio())
                            .outputQuality(param.getQuality())
                            .outputFormat(param.getSuffix()).asBufferedImage();
                }
            } else {
                //RESIZE
                if (param.getScale()!=null) {
                    bufferedImage = Thumbnails.of(bufferedImage).scale(param.getScale()).rotate(param.getRotate())
                            .outputQuality(param.getQuality()).outputFormat(param.getSuffix())
                            .asBufferedImage();
                } else {

                    bufferedImage =  Thumbnails.of(bufferedImage).size(param.getWidth(), param.getHeight()).rotate(param.getRotate())
                            .keepAspectRatio(param.isKeepAspectRatio())
                            .outputQuality(param.getQuality()).outputFormat(param.getSuffix())
                            .asBufferedImage();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageToBytes(bufferedImage,param.getSuffix());

    }

    public static byte[] reSizeImage(BufferedImage buffImage,int w,int h,String suffix) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage= Thumbnails.of(buffImage).size(w,h).outputFormat(suffix).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageToBytes(bufferedImage,suffix);
    }


    public static byte[] reSizeImage(InputStream inputStream,int w,int h,String suffix) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage= Thumbnails.of(inputStream).size(w,h).outputFormat(suffix).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageToBytes(bufferedImage,suffix);
    }


    public static byte[] reSizeImage(String url,int w,int h,String suffix) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage= Thumbnails.of(url).size(w,h).outputFormat(suffix).asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageToBytes(bufferedImage,suffix);
    }
    /**
     * 转换BufferedImage 数据为byte数组
     *
     * @param bImage
     * Image对象
     * @param format
     * image格式字符串.如"gif","png"
     * @return byte数组
     */
    public static byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * 转换byte数组为Image
     *
     * @param bytes
     * @return Image
     */
    public static Image bytesToImage(byte[] bytes) {
        Image image = Toolkit.getDefaultToolkit().createImage(bytes);
        try {
            MediaTracker mt = new MediaTracker(new Label());
            mt.addImage(image, 0);
            mt.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static  BufferedImage bytesToBufImage(byte[] bytes){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        BufferedImage image = null;
        try {
            image = ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static  byte[] readInputStream(InputStream inputStream) {
        if(inputStream==null) return null;
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
//            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return bos.toByteArray();
    }

//    public static void drawWords(BufferedImage bufferedImage, String words, Boolean isAddFontSpace, String wordsFont, int fontSize, int wordsX,
//                                 int wordsY, int wordsWidth, int wordsHeight) {
//        Graphics2D g2d = bufferedImage.createGraphics();
//        // 抗锯齿 添加文字
//        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB); // VALUE_TEXT_ANTIALIAS_ON 改为 VALUE_TEXT_ANTIALIAS_LCD_HRGB
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.getDeviceConfiguration().createCompatibleImage(wordsWidth, wordsHeight, Transparency.TRANSLUCENT);
//        Font font = new Font(wordsFont, Font.BOLD, fontSize);
//        g2d.setFont(font);
//        Color color = new Color(255, 255, 255);
//        g2d.setColor(color);
//        // 换行算法
//        drawWordAndLineFeed(g2d, font, words, wordsX, wordsY, wordsWidth);
//        g2d.dispose();
//    }
//    private static void drawWordAndLineFeed(Graphics2D g2d, Font font, String words, int wordsX, int wordsY, int wordsWidth) {
//        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
//        // 获取字符的最高的高度
//        int height = metrics.getHeight();
//        Color color = new Color(255, 255, 255);
//        int width = 0;
//        int count = 0;
//        int total = words.length();
//        String subWords = words;
//        int b = 0;
//        for (int i = 0; i < total; i++) {
//            // 统计字符串宽度 并与 预设好的宽度 作比较
//            if (width <= wordsWidth) {
//                width += metrics.charWidth(words.charAt(i)); // 获取每个字符的宽度
//                count++;
//            } else {
//                // 画 除了最后一行的前几行
//                String substring = subWords.substring(0, count);
//                //阴影
//                g2d.setColor(Color.BLACK);
//                g2d.drawString(substring, wordsX,wordsY + (b * height) );
//                g2d.setColor(color);
//                g2d.drawString(substring, wordsX-3, wordsY + (b * height)-3);
//                subWords = subWords.substring(count);
//                b++;
//                width = 0;
//                count = 0;
//            }
//            // 画 最后一行字符串
//            if (i == total - 1) {
//                g2d.setColor(Color.BLACK);
//                g2d.drawString(subWords, wordsX, wordsY + (b * height));
//                g2d.setColor(color);
//                g2d.drawString(subWords, wordsX-3, wordsY + (b * height)-3);
//            }
//        }
//    }
}
