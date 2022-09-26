package asia.dreamdropsakura.reggie.mapper;

import asia.dreamdropsakura.reggie.dto.SetmealDto;
import asia.dreamdropsakura.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;

/**
 * 套餐数据库表映射类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
public interface SetmealMapper extends BaseMapper<Setmeal> {
    /**
     * 左连接查询Setmeal 与Category
     *
     * @param startIndex 起始索引，最小以0开始
     * @param pageSize 每页记录数
     * @return
     */
    List<SetmealDto> selectWith(int startIndex, int pageSize, String name);
}
