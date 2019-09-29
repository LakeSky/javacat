package com.kzh.sys.util;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketUtil {
    public static String sendClientSocket(String host, int port, String content, int timeout, String eof) {
        Socket client = null;
        String response = null;
        try {
            //与服务端建立连接
            client = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (client != null) {
            //建立连接后就可以往服务端写数据了
            Writer writer = null;
            try {
                writer = new OutputStreamWriter(client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (writer != null) {
                try {
                    writer.write(content);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Reader reader = null;
                try {
                    reader = new InputStreamReader(client.getInputStream());
                    client.setSoTimeout(timeout);
                    char chars[] = new char[64];
                    int len;
                    StringBuffer sb = new StringBuffer();
                    String temp;
                    int index;
                    //每次读64位
                    while ((len = reader.read(chars)) != -1) {
                        temp = new String(chars, 0, len);
                        if ((index = temp.indexOf(eof)) != -1) {
                            sb.append(temp.substring(0, index));
                            break;
                        }
                        sb.append(new String(chars, 0, len));
                    }
                    response = sb.toString();
                    writer.close();
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
