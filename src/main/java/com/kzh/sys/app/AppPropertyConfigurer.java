package com.kzh.sys.app;

import com.kzh.sys.util.AES;

/**
 * Created by gang on 2019/6/17.
 */
public class AppPropertyConfigurer extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if ("password".equals(propertyName) && propertyValue.length() == 32) {
            return AES.decrypt(propertyValue, AppConstant.encryptKey);
        }
        return propertyValue;
    }
}
