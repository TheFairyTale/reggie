package asia.dreamdropsakura.reggie.common;

import asia.dreamdropsakura.reggie.util.LocalThreadVariablePoolUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器
 *
 * @author 童话的爱
 * @since 2022-9-19
 */
@Component
@Slf4j
public class CommonEntityFieldAutoFillHandler implements MetaObjectHandler {
    /**
     * 插入数据时自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentThreadUserid = LocalThreadVariablePoolUtil.getCurrentThreadUserid();
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", currentThreadUserid);
        metaObject.setValue("updateUser", currentThreadUserid);
    }

    /**
     * 更新数据时自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentThreadUserid = LocalThreadVariablePoolUtil.getCurrentThreadUserid();
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", currentThreadUserid);
    }
}
