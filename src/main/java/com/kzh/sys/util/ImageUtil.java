package com.kzh.sys.util;

/**
 * Created with IntelliJ IDEA.
 * kzh
 * Date: 2014/09/19
 * Time: 下午4:34
 */

import com.kzh.sys.util.sign.Base64Util;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("restriction")
public class ImageUtil {
    /**
     * 图片的url路径，如http://.....xx.jpg
     * @param imageUrl
     * @return
     */
    public static String encodeImageToBase64(URL imageUrl) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageUrl);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return Base64Util.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
    }

    /**
     * 将本地图片进行Base64位编码
     *
     * @param imageFile 图片的url路径，如http://.....xx.jpg
     * @return
     */
    public static String encodeImageToBase64(File imageFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        ByteArrayOutputStream outputStream = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return Base64Util.encode(outputStream.toByteArray());// 返回Base64编码过的字节数组字符串
    }

    /**
     * 将Base64位编码的图片进行解码，并保存到指定目录
     *
     * @param base64 base64编码的图片信息
     * @return
     */
    public static void decodeBase64ToImage(String base64, String path, String imgName) {
        try {
            File f = new File(path + imgName);
            if (f.exists()) {
                System.out.println("文件存在");
            } else {
                System.out.println("文件不存在，正在创建...");
                if (f.createNewFile()) {
                    System.out.println("文件创建成功！");
                } else {
                    System.out.println("文件创建失败！");
                }
            }
            FileOutputStream write = new FileOutputStream(f);
            byte[] decoderBytes = Base64Util.decode(base64);
            write.write(decoderBytes);
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String GetImageStr() {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = "d://hb.png";//待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(new File(imgFile));
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder e = new BASE64Encoder();
        return e.encode(data);
//        return Base64Util.encode(data);//返回Base64编码过的字节数组字符串
    }
    public static void main(String[] args) throws Exception {
        try {
            String imageBse64 = ImageUtil.GetImageStr();
//            String imageBse64 = ImageUtil.encodeImageToBase64(new File("d://hb.png")); 有红色阴影  需要处理
            System.out.println(imageBse64);
            imageBse64 = "iVBORw0KGgoAAAANSUhEUgAAAJYAAACWCAIAAACzY+a1AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NzQ2ODYyREYzRUQxMTFFNEI4MzVFQzQwRDI0RkEyODYiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NzQ2ODYyRTAzRUQxMTFFNEI4MzVFQzQwRDI0RkEyODYiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3NDY4NjJERDNFRDExMUU0QjgzNUVDNDBEMjRGQTI4NiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3NDY4NjJERTNFRDExMUU0QjgzNUVDNDBEMjRGQTI4NiIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PrKdbAkAAEWcSURBVHja7H0HnGZl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";
//            imageBse64 = "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";
            ImageUtil.decodeBase64ToImage(imageBse64,"d:/","xxxxx11.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

