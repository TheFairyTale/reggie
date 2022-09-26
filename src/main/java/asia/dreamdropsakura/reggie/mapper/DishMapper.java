package asia.dreamdropsakura.reggie.mapper;

import asia.dreamdropsakura.reggie.dto.DishDto;
import asia.dreamdropsakura.reggie.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;
import java.util.List;

/**
 * 菜肴数据库表映射类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
public interface DishMapper extends BaseMapper<Dish> {
    /**
     * 左连接查询Dish 与Category 表
     *
     * @param startIndex 起始索引
     * @param pageSize 每页记录数
     * @param name like 查询条件
     * @return DishDto
     */
    List<DishDto> selectWithDishAndCategoryTable(int startIndex, int pageSize, String name);
}
