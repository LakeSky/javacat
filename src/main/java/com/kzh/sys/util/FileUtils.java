package com.kzh.sys.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 〈一句话功能简述〉文件工具包 <br>
 *
 * @author lishang
 */
public class FileUtils {
    public static final int BUFFER_SIZE = 1024;

    /**
     * 功能描述: delete file or floder <br>
     * 〈功能详细描述〉
     * if file  is file ,delete  this file ;
     * if file  is directory ,delete this directory and  all files in the floder
     *
     * @param file 需要删除的文件或者目录
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static boolean deleteAllFile(File file) {
        //判断文件是否存在
        if (file == null || !file.exists()) {
            return false;
        }
        //删除目录下所有文件盒文件夹
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            //迭代
            for (File f : fs) {
                deleteAllFile(f);
            }
            return file.delete();
        } else {
            return file.delete();
        }
    }

    /**
     * delete file or floder <br>
     *
     * @param file
     * @param delThisFlg 如果file is folder , weather del this file
     * @return
     */
    public static boolean deleteAllFile(File file, boolean delThisFlg) {
        //判断文件是否存在
        if (file == null || !file.exists()) {
            return false;
        }
        //删除目录下所有文件盒文件夹
        if (file.isDirectory()) {
            File[] fs = file.listFiles();
            //迭代
            for (File f : fs) {
                deleteAllFile(f);
            }
            if (delThisFlg)
                return file.delete();
            return true;
        } else {
            return file.delete();
        }
    }

    /**
     * 创建文件
     *
     * @param f
     * @throws IOException
     */
    public static void createF(File f) throws IOException {
        createF(f, false);
    }

    /**
     * 创建指定文件或目录；如果父目录不存在，则创建父目录
     *
     * @param f
     * @param folder 是否是目录
     * @throws IOException
     */
    public static void createF(File f, boolean folder) throws IOException {
        if (!f.exists()) {
            if (folder) {
                f.mkdirs();
            } else {
                createF(f.getParentFile(), true);
                f.createNewFile();
            }
        }
    }

    public static StringBuffer readFile(String path) throws Exception {
        File file = new File(path);
        StringBuffer buffer = new StringBuffer();
        if (file.exists() && file.isFile()) {
            InputStream is = new FileInputStream(file);
            BufferedReader fi = new BufferedReader(new InputStreamReader(is));
            while (fi.ready()) {
                buffer.append(fi.readLine()).append("\n");
            }
        }
        return buffer;
    }

    public static void writeFile(byte[] b, String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            File parentDir = new File(file.getParent());
            parentDir.mkdirs();
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(b);
        fo.flush();
        fo.close();
    }

    public static String getFileNameNoExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, lastIndex);
    }

    public static String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex);
    }

    public static List<String> fileToList(String path) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        List<String> list = new ArrayList<>();
        String str = "";
        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);
            while ((str = bufferedReader.readLine()) != null) {
                if (str.trim().length() > 2) {
                    list.add(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return list;
    }

    public static List<String> fileToList(File file) {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        List<String> list = new ArrayList<>();
        String str = "";
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            while ((str = bufferedReader.readLine()) != null) {
                if (str.trim().length() > 2) {
                    list.add(str);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return list;
    }

    public static void copy(File file, File toFile) throws Exception {
        byte[] b = new byte[1024];
        int a;
        FileInputStream fis;
        FileOutputStream fos;
        if (file.isDirectory()) {
            String filepath = file.getAbsolutePath();
            filepath = filepath.replaceAll("\\\\", "/");
            String toFilepath = toFile.getAbsolutePath();
            toFilepath = toFilepath.replaceAll("\\\\", "/");
            int lastIndexOf = filepath.lastIndexOf("/");
            toFilepath = toFilepath + filepath.substring(lastIndexOf, filepath.length());
            File copy = new File(toFilepath);
            //复制文件夹 
            if (!copy.exists()) {
                copy.mkdir();
            }
            //遍历文件夹 
            for (File f : file.listFiles()) {
                copy(f, copy);
            }
        } else {
            if (toFile.isDirectory()) {
                String filepath = file.getAbsolutePath();
                filepath = filepath.replaceAll("\\\\", "/");
                String toFilepath = toFile.getAbsolutePath();
                toFilepath = toFilepath.replaceAll("\\\\", "/");
                int lastIndexOf = filepath.lastIndexOf("/");
                toFilepath = toFilepath + filepath.substring(lastIndexOf, filepath.length());

                //写文件 
                File newFile = new File(toFilepath);
                fis = new FileInputStream(file);
                fos = new FileOutputStream(newFile);
                while ((a = fis.read(b)) != -1) {
                    fos.write(b, 0, a);
                }
            } else {
                //写文件 
                fis = new FileInputStream(file);
                fos = new FileOutputStream(toFile);
                while ((a = fis.read(b)) != -1) {
                    fos.write(b, 0, a);
                }
            }
            fis.close();
            fos.close();
        }
    }

    public static List<String> unZip(File zipFile, String destDir, String encoding) throws Exception {
        destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
        ZipArchiveInputStream is = null;
        List<String> fileNames = new ArrayList<>();
        try {
            is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE), encoding);
            ZipArchiveEntry entry = null;
            while ((entry = is.getNextZipEntry()) != null) {
                fileNames.add(entry.getName());
                File file = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    org.apache.commons.io.FileUtils.forceMkdir(file); // 创建文件夹，如果中间有路径会自动创建
                } else {
                    OutputStream os = null;
                    try {
                        org.apache.commons.io.FileUtils.touch(file);
                        os = new FileOutputStream(new File(destDir, entry.getName()));
                        IOUtils.copy(is, os);
                    } finally {
                        IOUtils.closeQuietly(os);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            IOUtils.closeQuietly(is);
        }

        return fileNames;
    }

    /**
     * 解压 zip 文件
     *
     * @param zipFile  zip 压缩文件的路径
     * @param destDir  zip 压缩文件解压后保存的目录
     * @param encoding zip 文件的编码
     * @return 返回 zip 压缩文件里的文件名的 list
     * @throws Exception
     */
    public static List<String> unZip(String zipFile, String destDir, String encoding) throws Exception {
        File zipfile = new File(zipFile);
        return unZip(zipfile, destDir, encoding);
    }

    public static List<String> unZip(String zipFile, String destDir) throws Exception {
        return unZip(zipFile, destDir, "UTF-8");
    }

    /**
     * 改变图片的尺寸
     *
     * @param newWidth, newHeight, path
     * @return boolean
     */
    public static boolean changeSize(int newWidth, int newHeight, String path) {
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(path));

            //字节流转图片对象
            Image bi = ImageIO.read(in);
            //构建图片流
            BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            //绘制改变尺寸后的图
            tag.getGraphics().drawImage(bi, 0, 0, newWidth, newHeight, null);
            //输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(tag);
            ImageIO.write(tag, "PNG", out);
            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    //测试方法
    private void test() throws Exception {
        StringBuffer b = FileUtils.readFile("C:\\Users\\winyLee\\Desktop\\Noname1.txt");
        String path = "C:\\Users\\winyLee\\Desktop\\1t.txt";
        String str = b.toString();
        str = str.replace("),", "),\r\n");
        writeFile(str.getBytes(), path);
    }
}
