package asia.dreamdropsakura.reggie.service;

import asia.dreamdropsakura.reggie.dto.OrderDto;
import asia.dreamdropsakura.reggie.entity.Orders;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 下订单
     *
     * @return
     */
    boolean submitOrders(Orders orders);

    /**
     * 分页查询订单
     *
     * @return
     */
    IPage<OrderDto> paginationOrders(long page, long pageSize);
}
