package asia.dreamdropsakura.reggie.service.impl;

import asia.dreamdropsakura.reggie.dto.DishDto;
import asia.dreamdropsakura.reggie.entity.Dish;
import asia.dreamdropsakura.reggie.entity.DishFlavor;
import asia.dreamdropsakura.reggie.exception.ApplicationException;
import asia.dreamdropsakura.reggie.mapper.DishFlavorMapper;
import asia.dreamdropsakura.reggie.mapper.DishMapper;
import asia.dreamdropsakura.reggie.service.DishFlavorService;
import asia.dreamdropsakura.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * 菜肴业务代码实现类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    public Page<DishDto> selectByPaginationWithDishAndCategoryTable(int page, int pageSize, String name) {
        if (name != null) {
            name = "%" + name + "%";
        }

        // 分页公式:
        // limit 起始索引, 查询条目数
        // 起始索引：(当前页码 - 1) * 每页显示条数
        List<DishDto> dishDtos = dishMapper.selectWithDishAndCategoryTable((page - 1) * pageSize, pageSize, name);
        // 查询总记录数
        Long totalRecords = dishMapper.selectCount(null);
        return new Page<DishDto>().setCurrent(page).setRecords(dishDtos).setTotal(totalRecords);
    }

    @Override
    // 开启事务
    @Transactional
    public boolean saveWithFlavor(DishDto dishDto) {
        // 1. 保存菜品基本信息 ;
        int result = dishMapper.insert(dishDto);

        // 2. 获取保存的菜品ID ;
        Long dishId = dishDto.getId();

        // 3. 获取菜品口味列表，遍历列表，为菜品口味对象属性dishId赋值;
        List<DishFlavor> flavors = dishDto.getFlavors();
        /*flavors.stream().forEach(new Consumer<DishFlavor>() {
            @Override
            public void accept(DishFlavor dishFlavor) {
                dishFlavor.setDishId(dishId);
            }
        });*/
        for (int i = 0; i < flavors.size(); i++) {
            DishFlavor dishFlavor = flavors.get(i);
            // 另外再判断一下该dishFlavor 对象的name 口味名称和value 口味选项是否为空，为空就移除它并跳到下一个，不为空才能加。
            // 防止没有名字的口味选项出现在菜肴当中。
            if ("".equals(dishFlavor.getName().trim()) || "[]".equals(dishFlavor.getValue().trim())) {
                flavors.remove(i);
                // 想知道这里为啥要i--吗？你自己debug 下看看好了
                i--;
                continue;
            }
            dishFlavor.setDishId(dishId);
            flavors.set(i, dishFlavor);
        }

        // 4. 批量保存菜品口味列表;
        boolean b = dishFlavorService.saveBatch(flavors);

        return b;
    }

    @Override
    @Transactional
    public boolean saveWithFlavorById(DishDto dishDto) {
        // 1. 更新菜肴的基本信息
        int result = dishMapper.updateById(dishDto);

        // 2. 获取保存的菜肴ID
        Long dishId = dishDto.getId();

        // 3.0 菜品口味可能需要先清除再添加, 因为有可能传来的口味中已经将以前的口味删除，变成了新的口味
        // 注意像大米饭是没有口味的，如果一个菜肴没有口味则remove 会返回false
        boolean remove = dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dishDto.getId()));
        log.debug(remove ? "已移除" + dishDto.getId() + "所属的所有菜肴口味" : "未能移除菜肴口味，可能已经被移除？");

        // 3. 获取菜品口味列表，遍历列表，为菜品口味对象属性dishId赋值;
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (int i = 0; i < flavors.size(); i++) {
            DishFlavor dishFlavor = flavors.get(i);
            // 另外再判断一下该dishFlavor 对象的name 口味名称和value 口味选项是否任意一个为空，为空就移除它并跳到下一个，都不为空才能加。
            // 防止没有名字或内容的口味选项出现在菜肴当中。
            if ("".equals(dishFlavor.getName().trim()) || "[]".equals(dishFlavor.getValue().trim())) {
                flavors.remove(i);
                // 想知道这里为啥要i--吗？你自己debug 下看看好了
                i--;
                continue;
            }
            // 由于接收到的DishDto 对象中的flavorList 中的各个flavor 是带id (dish_flavor表下的 id) 的，该id 为表中逻辑删除前的id
            // 为了避免id 主键冲突，需要重新设置接收来的flavor 中的id
            // 由于该表的id 配置了主键自增，故id设置为null 即可
            dishFlavor.setId(null);
            dishFlavor.setDishId(dishDto.getId());
            flavors.set(i, dishFlavor);
        }

        // 4. 批量保存菜品口味列表;
        boolean b = dishFlavorService.saveBatch(flavors);

        return b;
    }


    @Override
    public DishDto selectById(long id) {
        Dish byId = dishMapper.selectById(id);
        List<DishFlavor> list = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, id));

        if (byId == null) {
            throw new ApplicationException("指定的记录不存在。");
        }

        // 利用对象拷贝，将dish 中的数据拷贝到dishDto 中
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId, dishDto);

        // 设置dishDto 中的菜肴口味集合
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public boolean deleteByIds(Collection<Long> ids) {
        // 查找id 为ids 中的所有id，且status为启售的记录。
        List<Dish> dishList = dishMapper.selectList(new LambdaQueryWrapper<Dish>().in(Dish::getId, ids).eq(Dish::getStatus, 1));

        // 只有当启售的记录为空时才可删除
        if (dishList.size() > 0) {
            throw new ApplicationException("无法删除菜肴，有菜肴仍在售卖中。");
        }
        // 移除菜肴及菜肴所属的所有口味
        int i = dishMapper.deleteBatchIds(ids);
        // 注意当此处删除时没有符合规则的目标对象而不是删除失败的，也会返回false. 最终导致整条SQL语句事务回滚
        boolean remove = dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().in(DishFlavor::getDishId, ids));
        if (i <= 0) {
            throw new ApplicationException("删除菜肴失败，可能菜肴已经被删除");
        }

        return true;
    }
}
