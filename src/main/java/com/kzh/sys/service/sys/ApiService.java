package com.kzh.sys.service.sys;

import com.kzh.sys.app.utils.AppUtils;
import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.dao.ApiDao;
import com.kzh.sys.dao.RoleResourceDao;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.model.Api;
import com.kzh.sys.model.RoleResource;
import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.api.ApiInterface;
import com.kzh.sys.pojo.api.ApiReqParameter;
import com.kzh.sys.pojo.api.ApiResParameter;
import com.kzh.sys.pojo.tree.ZTreeNode;
import com.kzh.sys.service.generate.auto.QApi;
import com.kzh.sys.service.generate.auto.QField;
import com.kzh.sys.service.generate.auto.QFieldInfo;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.kzh.sys.service.generate.FieldService.getAllFieldInfo;

@Service
@Transactional
public class ApiService extends BaseService<Api> {
    private static final Logger logger = Logger.getLogger(ApiService.class);
    
    private static Map<String, Method> apiMethodMap = new ConcurrentHashMap<>();

    @Resource
    private ApiDao apiDetailDao;
    @Resource
    private UserDao userDao;
    @Resource
    private RoleResourceDao roleResourceDao;

    @Override
    public GenericRepository getDao() {
        return apiDetailDao;
    }

    public ZTreeNode getDocTree(String username, String roleId) {
        ZTreeNode rootNode = new ZTreeNode();
        rootNode.setId("0");
        rootNode.setName("根");
        List<Class> controllerClasses = AppService.getAllClasses(AppService.api_controller_pattern);
        if (CollectionUtil.isNotEmpty(controllerClasses)) {
            for (Class controllerClass : controllerClasses) {
                RequestMapping annotation = (RequestMapping) controllerClass.getAnnotation(RequestMapping.class);
                if (annotation != null && ArrayUtils.isNotEmpty(annotation.value())) {
                    String baseUrl = annotation.value()[0];
                    String controllerName = annotation.name();
                    boolean checkedParent = false;
                    ZTreeNode zTreeNode = new ZTreeNode(baseUrl, "0", controllerName, checkedParent, true, null);
                    if (AppUtils.ifControl(annotation.name())) {
                        Method[] methods = controllerClass.getDeclaredMethods();
                        if (ArrayUtils.isNotEmpty(methods)) {
                            for (Method method : methods) {
                                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
//                                ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
                                if (requestMapping != null && ArrayUtils.isNotEmpty(requestMapping.value())) {
                                    String fullUrl = baseUrl + requestMapping.value()[0];
                                    apiMethodMap.put(fullUrl, method);
                                    if (AppUtils.ifControl(requestMapping.name())) {
                                        String name = requestMapping.name();
                                        boolean checkedChild = false;
                                        ZTreeNode zTreeNodeUrl = new ZTreeNode(fullUrl, baseUrl, name, checkedChild, true, null);
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

    public ApiInterface getInterfaceDetail(String url) {
        ApiInterface apiInterface = new ApiInterface();
        Method method = apiMethodMap.get(url);
        if (method != null) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            apiInterface.setName(requestMapping.name());
            apiInterface.setAddress(url);
            apiInterface.setReturnType("json");
            apiInterface.setRequestType("post");
            List<ApiReqParameter> apiReqParameters = new ArrayList<>();
            apiInterface.setApiReqParameters(apiReqParameters);
            Class[] parameterTypes = method.getParameterTypes();
            for (Class parameter : parameterTypes) {
                List<QFieldInfo> qFieldInfos = getAllFieldInfo(parameter);
                if (CollectionUtil.isNotEmpty(qFieldInfos)) {
                    for (QFieldInfo qFieldInfo : qFieldInfos) {
                        ApiReqParameter apiReqParameter = new ApiReqParameter();
                        apiReqParameter.setType(qFieldInfo.getJavaFiledType());
                        apiReqParameter.setNick(qFieldInfo.getNickname());
                        apiReqParameter.setName(qFieldInfo.getName());
                        apiReqParameter.setMust(qFieldInfo.getNullable() ? "否" : "是");
                        apiReqParameter.setDesc(qFieldInfo.getNickname());
                        apiReqParameters.add(apiReqParameter);
                    }
                } else {
                    ApiReqParameter apiReqParameter = new ApiReqParameter();
                    apiReqParameter.setType(parameter.getSimpleName());
                    apiReqParameter.setName(parameter.getName());

                    apiReqParameters.add(apiReqParameter);
                }
            }

            List<ApiResParameter> apiResParameters = new ArrayList<>();
            apiInterface.setApiResParameters(apiResParameters);
            apiResParameters.addAll(getResParameters(Result.class, false));
            QApi qApi = method.getAnnotation(QApi.class);
            if (qApi != null) {
                Class<?> resDataType = qApi.dataClass();
                apiResParameters.addAll(getResParameters(resDataType, false));
            }

            Api sysApiDetail = findByUrl(url);
            if (sysApiDetail != null) {
                apiInterface.setResExample(sysApiDetail.getResExample());
                apiInterface.setReqExample(sysApiDetail.getReqExample());
                apiInterface.setBz(sysApiDetail.getBz());
            }
            if (SysUtil.isEmpty(apiInterface.getReqExample())) {
                String reqParams = "{\n";
                if (CollectionUtil.isNotEmpty(apiReqParameters)) {
                    for (int i = 0; i < apiReqParameters.size(); i++) {
                        ApiReqParameter apiReqParameter = apiReqParameters.get(i);
                        if (i != (apiReqParameters.size() - 1)) {
                            reqParams += "\"" + apiReqParameter.getName() + "\"" + ":\"\",\n";
                        } else {
                            reqParams += "\"" + apiReqParameter.getName() + "\"" + ":\"\"";
                        }
                    }
                }
                reqParams += "\n}";
                apiInterface.setReqExample(reqParams);
            }
        }

        return apiInterface;
    }

    private List<ApiResParameter> getResParameters(Class clazz, boolean space) {
        List<ApiResParameter> apiResParameters = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            QField qField = field.getAnnotation(QField.class);
            if (qField != null) {
                QFieldInfo fieldInfo = new QFieldInfo();
                fieldInfo.fromQField(field, qField);
                ApiResParameter apiResParameter = new ApiResParameter();

                apiResParameter.setType(fieldInfo.getJavaFiledType());
                apiResParameter.setNick(fieldInfo.getNickname());
                apiResParameter.setName(fieldInfo.getName());
                apiResParameter.setDesc(fieldInfo.getNickname());
                apiResParameter.setSpace(space);

                apiResParameters.add(apiResParameter);
                QApi qApi = field.getAnnotation(QApi.class);
                if (qApi != null) {
                    apiResParameters.addAll(getResParameters(qApi.dataClass(), true));
                }
            }
        }
        return apiResParameters;
    }

    public Api findByUrl(String url) {
        return apiDetailDao.findByUrl(url);
    }

    public Result saveReqExample(String api, String reqExample) {
        Api apiDetail = apiDetailDao.findByUrl(api);
        if (apiDetail == null) {
            apiDetail = new Api();
            apiDetail.setUrl(api);
        }
        apiDetail.setReqExample(reqExample);
        Api apiDetail1 = apiDetailDao.save(apiDetail);
        return new Result(true, "成功", apiDetail1);
    }

    public Result saveResExample(String api, String resExample) {
        Api apiDetail = apiDetailDao.findByUrl(api);
        if (apiDetail == null) {
            apiDetail = new Api();
            apiDetail.setUrl(api);
        }
        apiDetail.setResExample(resExample);
        Api apiDetail1 = apiDetailDao.save(apiDetail);
        return new Result(true, "成功", apiDetail1);
    }

    public Result saveBz(String api, String bz) {
        Api sysApiDetail = findByUrl(api);
        if (sysApiDetail == null) {
            sysApiDetail = new Api();
            sysApiDetail.setUrl(api);
        }
        sysApiDetail.setBz(bz);
        Api apiDetail1 = apiDetailDao.save(sysApiDetail);
        return new Result(true, "成功", apiDetail1);
    }

    public boolean verifyApiUrl(String userId, String url) {
        User user = userDao.getOne(userId);
        List<RoleResource> roleResources = roleResourceDao.findByUrl(url);
        //并没有角色控制该url，则默认该url是开放访问的
        if (CollectionUtil.isEmpty(roleResources)) {
            logger.warn("url:" + url + "未设置权限");
            return true;
        }
        Set<String> roleIds = new HashSet<>();
        for (RoleResource roleResource : roleResources) {
            roleIds.add(roleResource.getRoleId());
        }
        if (user.getRole() != null && roleIds.contains(user.getRole().getId())) {
            return true;
        }
        return false;
    }

}
