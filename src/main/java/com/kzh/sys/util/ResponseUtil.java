package com.kzh.sys.util;

import com.kzh.sys.pojo.Result;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by gang on 2019/6/14.
 */
public class ResponseUtil {
    private static final Logger logger = Logger.getLogger(ResponseUtil.class);

    public static void returnJson(HttpServletRequest request, HttpServletResponse response, String msg) {
        Result result = new Result(false, msg);
        String json = JsonUtil.objectToJson(result);
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException ex) {
            logger.error("response error", ex);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public static void returnText(HttpServletRequest request, HttpServletResponse response, String msg) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            writer = response.getWriter();
            writer.print(msg);
        } catch (IOException ex) {
            logger.error("response error", ex);
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
