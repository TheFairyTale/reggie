package asia.dreamdropsakura.reggie.service.impl;

import asia.dreamdropsakura.reggie.entity.Dish;
import asia.dreamdropsakura.reggie.entity.ShoppingCart;
import asia.dreamdropsakura.reggie.mapper.ShoppingCartMapper;
import asia.dreamdropsakura.reggie.service.ShoppingCartService;
import asia.dreamdropsakura.reggie.util.LocalThreadVariablePoolUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCart addDishOrSetmealByMapParam(Map map) {
        String name = (String) map.get("name");
        String imageFileName = (String) map.get("image");
        String dishId = (String) map.get("dishId");
        String dishFlavor = (String) map.get("dishFlavor");
        String setmealId = (String) map.get("setmealId");
        Integer amountTemp = (Integer) map.get("amount");
        BigDecimal amount = new BigDecimal(amountTemp);

        // 构建一个ShoppingCart 对象，前端选菜列表需要它才能得知当前菜肴已经选了几份
        ShoppingCart shoppingCart = null;

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, LocalThreadVariablePoolUtil.getCurrentThreadUserid());
        // 先判断传来的是套餐还是菜肴，如果菜肴为空则表示上传的是套餐
        if (dishId == null) {
            // 上传的是套餐的话
            // 向数据库查询当前用户提交的当前套餐是否已在表中有记录
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
            shoppingCart = this.getOne(shoppingCartLambdaQueryWrapper);
            if (shoppingCart != null) {
                // 如果表中有记录, 则更新记录使该条记录的Number + 1
                boolean b = increaseNumberForSetmealById(LocalThreadVariablePoolUtil.getCurrentThreadUserid(), Long.valueOf(setmealId));
                shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            } else {
                // 如果表中无记录, 则新增记录
                shoppingCart = new ShoppingCart();
                shoppingCart.setSetmealId(Long.valueOf(setmealId));
                shoppingCart.setImage(imageFileName);
                shoppingCart.setAmount(amount);
                shoppingCart.setUserId(LocalThreadVariablePoolUtil.getCurrentThreadUserid());
                shoppingCart.setName(name);
                shoppingCart.setNumber(1);
                boolean save = this.save(shoppingCart);
            }
            return shoppingCart;
        } else {
            // todo 如果传来的菜肴口味变量为空的，会导致重复记录。
            //  例如之前点过相同口味，只是点了+1 的，口味变量也为空（前端只传了dishId、name、image）
            //  例如本身无口味的大米饭 会被重复记录
            // 上传的是菜肴的话
            // 向数据库查询当前用户提交的当前菜肴是否已在表中有记录
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
            // 使该条件无论是否为空都能查到. 例如大米没有口味
            // 暂时不考虑这个新增相同菜品不同口味的功能。前端没有实现
            //shoppingCartLambdaQueryWrapper.eq(dishFlavor != null, ShoppingCart::getDishFlavor, dishFlavor);
            shoppingCart = this.getOne(shoppingCartLambdaQueryWrapper);
            if (shoppingCart != null) {
                // 如果表中有菜肴与菜肴口味都相同的记录, 则更新记录使该条记录的Number + 1
                boolean b = increaseNumberForDishById(LocalThreadVariablePoolUtil.getCurrentThreadUserid(), Long.valueOf(dishId));
                shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            } else {
                // 如果表中无匹配记录, 则新增记录
                shoppingCart = new ShoppingCart();
                shoppingCart.setDishId(Long.valueOf(dishId));
                shoppingCart.setDishFlavor(dishFlavor);
                shoppingCart.setImage(imageFileName);
                shoppingCart.setAmount(amount);
                shoppingCart.setUserId(LocalThreadVariablePoolUtil.getCurrentThreadUserid());
                shoppingCart.setName(name);
                shoppingCart.setNumber(1);
                boolean save = this.save(shoppingCart);
            }
            return shoppingCart;
        }
    }

    @Override
    public boolean increaseNumberForDishById(Long userId, Long dishId) {
        if (userId != null && dishId != null) {
            return shoppingCartMapper.updateAddDishNumberIfExist(userId, dishId) > 0;
        }
        return false;
    }

    @Override
    public ShoppingCart decreaseNumberForDishById(Long userId, Long dishId) {
        if (userId != null && dishId != null) {
            ShoppingCart one = this.getOne(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getDishId, dishId));

            // 获取数量
            if (one.getNumber() <= 1) {
                boolean b = this.removeById(one);
                one.setNumber(0);
            } else {
                one.setNumber(one.getNumber() - 1);
                boolean b = this.updateById(one);
            }
            return one;
        }
        return null;
    }

    @Override
    public boolean increaseNumberForSetmealById(Long userId, Long dishId) {
        if (userId != null && dishId != null) {
            return shoppingCartMapper.updateAddSetmealNumberIfExist(userId, dishId) > 0;
        }
        return false;
    }

    @Override
    public ShoppingCart decreaseNumberForSetmealById(Long userId, Long setmealId) {
        if (userId != null && setmealId != null) {
            ShoppingCart one = this.getOne(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId).eq(ShoppingCart::getSetmealId, setmealId));
            // 获取数量
            if (one.getNumber() <= 1) {
                boolean b = this.removeById(one);
                one.setNumber(0);
            } else {
                one.setNumber(one.getNumber() - 1);
                boolean b = this.updateById(one);
            }
            return one;
        }
        return null;
    }
}
