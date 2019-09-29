package com.kzh.sys.util.sign;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String md5(final String password, final String charset) {
        if (password == null) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            if (StringUtils.hasText(charset)) {
                messageDigest.update(password.getBytes(charset));
            } else {
                messageDigest.update(password.getBytes());
            }
            final byte[] digest = messageDigest.digest();

            return getFormattedText(digest);
        } catch (final NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(final byte[] bytes) {
        final StringBuilder buf = new StringBuilder(bytes.length * 2);

        for (int j = 0; j < bytes.length; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String md5(String content) {
        return md5(content, null);
    }


    /**
     * 签名字符串
     *
     * @param content       需要签名的字符串
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String content, String key, String input_charset) {
        content = content + key;
        return md5(content, input_charset);
    }

    /**
     * 签名字符串
     *
     * @param content       需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String content, String sign, String key, String input_charset) {
        content = content + key;
        String mySign = md5(content, input_charset);

        if (mySign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws java.security.SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }


    //原来javacat里面提供的方法
    public static String digestBufferToString(byte[] buffer) {
        int len;
        if ((len = buffer != null ? buffer.length : 0) <= 0) {
            return null;
        } else {
            StringBuffer sb = new StringBuffer();

            for (int ii = 0; ii < len; ++ii) {
                int bt = buffer[ii] & 255;
                sb.append(String.format("%02x", bt));
            }

            return sb.toString();
        }
    }

    public static String md5L32(String reStr) {
        try {
            reStr = digestBufferToString(MessageDigest.getInstance("MD5").digest(reStr.getBytes()));
            return reStr;
        } catch (NoSuchAlgorithmException var2) {
            return null;
        } catch (Exception var3) {
            return null;
        }
    }

    public static String md5U32(String reStr) {
        if ((reStr = md5L32(reStr)) != null) {
            reStr = reStr.toUpperCase();
        }

        return reStr;
    }

    public static String md5U16(String reStr) {
        if ((reStr = md5L32(reStr)) != null) {
            reStr = reStr.toUpperCase().substring(8, 24);
        }

        return reStr;
    }

    public static String md5L16(String reStr) {
        if ((reStr = md5L32(reStr)) != null) {
            reStr = reStr.substring(8, 24);
        }

        return reStr;
    }

    private static String a(File file) {
        if (file != null && file.isFile()) {
            FileInputStream fis = null;
            boolean var9 = false;

            String md5;
            label109:
            {
                try {
                    var9 = true;
                    fis = new FileInputStream(file);
                    byte[] buffer = new byte[8192];
                    MessageDigest md = MessageDigest.getInstance("MD5");

                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        md.update(buffer, 0, len);
                    }

                    md5 = digestBufferToString(md.digest());
                    var9 = false;
                    break label109;
                } catch (Exception var13) {
                    var9 = false;
                } finally {
                    if (var9) {
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException var10) {
                            }
                        }

                    }
                }

                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException var11) {
                    }
                }

                return null;
            }

            try {
                fis.close();
            } catch (IOException var12) {
            }

            return md5;
        } else {
            return null;
        }
    }

    private static String b(File file) {
        if (file != null && file.isFile()) {
            FileInputStream fis = null;
            boolean var4 = false;

            String md5;
            label72:
            {
                try {
                    var4 = true;
                    md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis = new FileInputStream(file)));
                    var4 = false;
                    break label72;
                } catch (Exception var5) {
                    var4 = false;
                } finally {
                    if (var4) {
                        if (fis != null) {
                            IOUtils.closeQuietly(fis);
                        }

                    }
                }

                if (fis != null) {
                    IOUtils.closeQuietly(fis);
                }

                return null;
            }

            IOUtils.closeQuietly(fis);
            return md5;
        } else {
            return null;
        }
    }

    public static String md5L32(File file) {
        if (file != null && file.isFile()) {
            return file.length() > 1073741824L ? a(file) : b(file);
        } else {
            return null;
        }
    }

    public static String md5U32(File file) {
        String reStr;
        if ((reStr = md5L32(file)) != null) {
            reStr = reStr.toUpperCase();
        }

        return reStr;
    }

}