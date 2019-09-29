package com.kzh.sys.service.sys;

import com.kzh.sys.app.AppConfigKey;
import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.core.exception.WorldValidateException;
import com.kzh.sys.dao.ConfigDao;
import com.kzh.sys.model.Config;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gang on 2017/3/14.
 */
@Service
@Transactional
public class ConfigService extends BaseService<Config> {
    private static final Logger logger = Logger.getLogger(ConfigService.class);
    public static Map<String, Config> configMap = new ConcurrentHashMap<>();

    @Resource
    private ConfigDao configDao;

    @Override
    public GenericRepository getDao() {
        return configDao;
    }

    public void initConfigCache() {
        List<Config> os = configDao.findAll();
        if (CollectionUtil.isNotEmpty(os)) {
            for (Config o : os) {
                ConfigService.configMap.put(o.getConfigKey(), o);
            }
        }
    }

    public Result saveConfig(Config entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        Config tDb;
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            tDb = configDao.save(entity);
        } else {
            //编辑
            Config config = configDao.getOne(entity.getId());
            config.initModifyFields(entity);
            tDb = configDao.save(config);
        }
        return new Result(true, "成功", tDb);
    }

    public Result saveParam(String configKey, String configValue) {
        if (SysUtil.isEmpty(configKey)) {
            return new Result(false, "参数为空");
        }
        if (SysUtil.isEmpty(configValue)) {
            return new Result(false, "参数值为空");
        }

        /*if (AppConfigKey.allegro_path.name().equals(configKey)) {
            File file = new File(configValue);
            if (!file.exists()) {
                return new Result(false, "文件不存在");
            }
        }
        if (AppConfigKey.upload_task_filepath.name().equals(configKey)) {
            File file = new File(configValue);
            if (!file.exists()) {
                return new Result(false, "路径不存在");
            }
        }
        if (AppConfigKey.allegro_path.name().equals(configKey)) {
            File file = new File(configValue);
            if (file.isDirectory() && !configValue.endsWith(".exe")) {
                return new Result(false, "请填写exe文件的全路径");
            }
        }
        if (AppConfigKey.checklist_excel_path.name().equals(configKey)) {
            File file = new File(configValue);
            if (!file.exists()) {
                return new Result(false, "Excel文件不存在");
            }
        }
        if (AppConfigKey.max_parallel_num.name().equals(configKey)) {
            if (!NumberUtil.isIntegerNumber(configValue)) {
                return new Result(false, "请填写大于0的整数");
            }
            Integer value = Integer.valueOf(configValue);
            if (value < 1) {
                return new Result(false, "请填写大于0的整数");
            }
            configValue = value.toString();
        }*/
        Config config = configDao.findByConfigKey(configKey);
        if (config != null) {
            config.setConfigValue(configValue);
        } else {
            logger.info("参数不存在，默认将该参数加入到数据库中");
            //此处默认把参数加到数据库中，便于后面的配置
            config = new Config(configKey, configValue, "", "");
            configDao.save(config);
        }
        configMap.put(configKey, config);
        return new Result(true, "设置成功");
    }

    public static String getConfigValue(AppConfigKey appConfigKey) {
        Config config = configMap.get(appConfigKey.name());
        if (config == null) {
            throw new WorldValidateException(appConfigKey.getName() + "，未配置");
        }
        return config.getConfigValue();
    }
}
