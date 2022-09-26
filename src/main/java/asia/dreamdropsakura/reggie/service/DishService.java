package asia.dreamdropsakura.reggie.service;

import asia.dreamdropsakura.reggie.dto.DishDto;
import asia.dreamdropsakura.reggie.entity.Dish;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * 菜肴业务代码抽象类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
public interface DishService extends IService<Dish> {
    /**
     * 分页查询菜肴和菜系套餐分类表
     *
     * @param page 当前页数
     * @param pageSize 每页记录数
     * @param name 模糊查询指定的名称
     * @return
     */
    Page<DishDto> selectByPaginationWithDishAndCategoryTable(int page, int pageSize, String name);

    /**
     * 保存菜肴及菜肴口味信息
     * 由于前端传来的菜肴口味数据中，不带有菜肴id，但存入数据库时需要有菜肴id，故在存入前手动设置菜肴id 到每个口味对象中，这是该方法负责的事
     *
     * @param dishDto
     * @return
     */
    boolean saveWithFlavor(DishDto dishDto);

    /**
     * 更新菜肴及菜肴口味信息
     * 由于前端传来的菜肴口味数据中，包含id及菜肴id，故不需要手动设置菜肴id 到每个口味对象中。
     * 不过由于该表采用逻辑删除，而传来的菜肴口味中的id 和被删除的相同，故还需要改它的id才可以正常插入新的记录
     *
     * @param dishDto
     * @return
     */
    boolean saveWithFlavorById(DishDto dishDto);

    /**
     * 根据id 获取菜肴及其菜肴口味信息
     *
     * @param id
     * @return
     */
    DishDto selectById(long id);

    /**
     * 根据一个或多个id 批量删除菜肴，但售卖中的菜肴不可被删除
     *
     * @param ids
     * @return
     */
    boolean deleteByIds(Collection<Long> ids);
}
