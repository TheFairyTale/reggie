package asia.dreamdropsakura.reggie.service.impl;

import asia.dreamdropsakura.reggie.entity.Orders;
import asia.dreamdropsakura.reggie.mapper.OrdersMapper;
import asia.dreamdropsakura.reggie.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
