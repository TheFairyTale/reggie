package asia.dreamdropsakura.reggie.dto;

import asia.dreamdropsakura.reggie.entity.Dish;
import asia.dreamdropsakura.reggie.entity.DishFlavor;
import asia.dreamdropsakura.reggie.entity.Setmeal;
import asia.dreamdropsakura.reggie.entity.SetmealDish;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 新增套餐数据封装类，用于将前端传来的一个json 数据集内包含的两个不同的实体类封装到一起
 * 同时它也承担Setmeal 与Category 表的连表查询结果集封装
 *
 * @author 童话的爱
 * @since 2022-9-24
 */
@Data
public class SetmealDto extends Setmeal implements Serializable {

    /**
     * 套餐所属的菜肴
     *
     */
    private List<SetmealDish> setmealDishes;

    /**
     * 套餐名称
     *
     */
    private String categoryName;

    @Override
    public String toString() {
        return super.toString() + "SetmealDto{" + "setmealDishes=" + setmealDishes + ", categoryName='" + categoryName + "'" + "}";
    }
}
