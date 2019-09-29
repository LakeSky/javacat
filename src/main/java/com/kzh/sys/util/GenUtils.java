package com.kzh.sys.util;

import java.lang.reflect.Field;

/**
 * Created by gang on 2016/12/4.
 */
public class GenUtils {
    public static void printToDTO(Class clazz) {
        Field[] fields =clazz.getDeclaredFields();
        String className = clazz.getSimpleName();
        System.out.println("public " + className + "DTO toDTO() {");
        System.out.println("\t" + className + "DTO dto = new " + className + "DTO();");
        for (Field field : fields) {
            System.out.println("\tdto.set" + SysUtil.capFirst(field.getName()) + "(this.get" + SysUtil.capFirst(field.getName()) + "())");
        }
        System.out.println("\t return dto;");
        System.out.println("}");
    }
}
