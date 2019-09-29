package com.kzh.sys.service.sys;

import com.kzh.sys.app.utils.AppUtils;
import com.kzh.sys.pojo.tree.ZTreeNode;
import com.kzh.sys.util.CollectionUtil;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by gang on 2017/6/17.
 */
@Service
@Transactional
public class ResourceService {
    public ZTreeNode resources() {
        ZTreeNode rootNode = new ZTreeNode();
        rootNode.setId("0");
        rootNode.setName("根");
        List<Class> controllerClasses = AppService.getControllers();
        if (CollectionUtil.isNotEmpty(controllerClasses)) {
            for (Class controllerClass : controllerClasses) {
                RequestMapping annotation = (RequestMapping) controllerClass.getAnnotation(RequestMapping.class);
                if (annotation != null && ArrayUtils.isNotEmpty(annotation.value())) {
                    String baseUrl = annotation.value()[0];
                    String controllerName = annotation.name();
                    ZTreeNode zTreeNode = new ZTreeNode(baseUrl, "0", controllerClass.getSimpleName() + " " + controllerName, false, false, null);
                    if (AppUtils.ifControl(annotation.name())) {
                        Method[] methods = controllerClass.getDeclaredMethods();
                        if (ArrayUtils.isNotEmpty(methods)) {
                            for (Method method : methods) {
                                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                                if (requestMapping != null && ArrayUtils.isNotEmpty(requestMapping.value())) {
                                    Class returnType = method.getReturnType();
                                    if (String.class == returnType) {
                                        String fullUrl = baseUrl + requestMapping.value()[0];
                                        String name = requestMapping.name();
                                        ZTreeNode zTreeNodeUrl = new ZTreeNode(fullUrl, baseUrl, fullUrl + " " + name, false, true, null);
                                        zTreeNode.getChildren().add(zTreeNodeUrl);
                                    }
                                }
                            }
                        }
                    }

                    if (CollectionUtil.isNotEmpty(zTreeNode.getChildren())) {
                        rootNode.getChildren().add(zTreeNode);
                    }
                }
            }
        }

        return rootNode;
    }
}
