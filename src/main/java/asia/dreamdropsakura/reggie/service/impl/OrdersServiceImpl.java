package asia.dreamdropsakura.reggie.service.impl;

import asia.dreamdropsakura.reggie.dto.OrderDto;
import asia.dreamdropsakura.reggie.entity.OrderDetail;
import asia.dreamdropsakura.reggie.entity.Orders;
import asia.dreamdropsakura.reggie.entity.ShoppingCart;
import asia.dreamdropsakura.reggie.mapper.OrdersMapper;
import asia.dreamdropsakura.reggie.service.OrderDetailService;
import asia.dreamdropsakura.reggie.service.OrdersService;
import asia.dreamdropsakura.reggie.service.ShoppingCartService;
import asia.dreamdropsakura.reggie.util.LocalThreadVariablePoolUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    @Transactional
    public boolean submitOrders(Orders orders) {
        if (orders != null) {
            // orders 包含 addressBookId，payMethod，remark
            // 设置订单号
            long orderId = UUID.randomUUID().getMostSignificantBits();
            orders.setNumber("" + orderId);
            // 设置下单用户
            orders.setUserId(LocalThreadVariablePoolUtil.getCurrentThreadUserid());
            // 设置结账时间
            orders.setCheckoutTime(LocalDateTime.now());

            // 获取所有菜肴与套餐，然后将其加入订单明细表
            List<ShoppingCart> list = shoppingCartService.list(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, LocalThreadVariablePoolUtil.getCurrentThreadUserid()));
            ArrayList<OrderDetail> orderDetails = new ArrayList<>();
            for (ShoppingCart shoppingCart : list) {
                OrderDetail orderDetail = new OrderDetail();
                // 为订单明细设置订单id
                orderDetail.setOrderId(orderId);
                // 重置菜肴或套餐的主键id，否则本次下订单下的菜如果和下次的菜一样，则携带来的主键id 也一样，而主键id 不可重复
                shoppingCart.setId(UUID.randomUUID().getMostSignificantBits());
                BeanUtils.copyProperties(shoppingCart, orderDetail, "userId", "createTime", "updateTime", "createUser", "updateUser");
                orderDetails.add(orderDetail);
            }

            // 将所有菜肴或套餐加入订单明细表
            boolean b = orderDetailService.saveBatch(orderDetails);

            // 计算总金额
            BigDecimal totalAmount = new BigDecimal(0);
            for (ShoppingCart shoppingCart : list) {
                totalAmount = totalAmount.add(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())));
            }

            // 设置实收金额
            orders.setAmount(totalAmount);
            // 设置用户电话
            orders.setPhone(orders.getPhone());
            // 设置用户名
            orders.setUserName(orders.getUserName());

            // 提交订单
            boolean save = this.save(orders);

            // 下单成功后需要清空购物车
            boolean remove = shoppingCartService.remove(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, LocalThreadVariablePoolUtil.getCurrentThreadUserid()));
            return true;
        }

        return false;
    }

    @Override
    public IPage<OrderDto> paginationOrders(long page, long pageSize) {
        // order 分页对象
        IPage<Orders> ordersPage = new Page<>(page, pageSize);
        IPage<Orders> pagedOrders = this.page(ordersPage, null);

        List<Orders> records = pagedOrders.getRecords();
        // 用于承载包含订单基本信息与详细信息对象的列表
        ArrayList<OrderDto> orderDtos = new ArrayList<>();
        for (Orders record : records) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(record, orderDto);
            // 通过订单号获取该订单明细，并设置orderDto 对象中的订单明细
            orderDto.setOrderDetails(orderDetailService.list(new LambdaQueryWrapper<OrderDetail>().eq(OrderDetail::getOrderId, record.getNumber())));
            // 设置订单总金额
            orderDto.setSumNum(record.getAmount());
            orderDtos.add(orderDto);
        }

        // orderDto 分页对象
        IPage<OrderDto> orderDtoPage = new Page<>(page, pageSize);
        orderDtoPage.setRecords(orderDtos).setTotal(pagedOrders.getTotal());

        return orderDtoPage;
    }
}
