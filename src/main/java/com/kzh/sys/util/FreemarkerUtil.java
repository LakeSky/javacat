package com.kzh.sys.util;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.*;

/**
 * Created by shang.li on 2015/12/8.
 */
public class FreemarkerUtil {

    Configuration configuration;

    public void setEn(String path) throws Exception {
        Version v = new Version("2.3.21");
        configuration = new Configuration(v);
        configuration.setDirectoryForTemplateLoading(new File(path));
    }

    public void process(String templateName, String destName, Object o) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templateName);
        File f = new File(destName);
        if (!f.exists())
            FileUtils.createF(f);
        template.process(o, new OutputStreamWriter(new FileOutputStream(f)));
    }

    public String getFileString(String templateName, Object dataModel) throws IOException, TemplateException {
        Template template = configuration.getTemplate(templateName);
        StringWriter sw = new StringWriter();
        template.process(dataModel, sw);
        return sw.toString();
    }
}
