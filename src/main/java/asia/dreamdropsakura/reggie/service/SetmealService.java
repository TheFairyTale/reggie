package asia.dreamdropsakura.reggie.service;

import asia.dreamdropsakura.reggie.dto.SetmealDto;
import asia.dreamdropsakura.reggie.entity.Setmeal;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.Set;

/**
 * 套餐业务代码抽象类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 根据name 分页查询套餐列表
     *
     * @param name like 语句查询参数，为空则不使用like 查询条件
     * @param page 当前是第几页
     * @param pageSize 每页记录数
     * @return
     */
    IPage<SetmealDto> paginationSetmeals(int page, int pageSize, String name);

    /**
     * 根据id 获取指定的套餐，且需要包含套餐下的所有菜肴
     *
     * @param id
     * @return
     */
    SetmealDto selectSetmealById(Long id);

    /**
     * 根据套餐id 更新套餐信息及其套餐所属菜肴。注意菜肴的更新是先删除原先所属的所有菜肴再添加传来的所有菜肴
     *
     * @param setmealDto
     * @return
     */
    boolean updateSetmeal(SetmealDto setmealDto);

    /**
     * 新增套餐，该功能需要多表插入值, 注意插入失败时需要回滚事务
     *
     * @param setmealDto
     * @return
     */
    boolean addSetmeal(SetmealDto setmealDto);

    /**
     * 根据一个或多个id 批量删除套餐，但售卖中的套餐不可被删除
     *
     * @param ids
     * @return
     */
    boolean deleteByIds(Collection<Long> ids);
}
