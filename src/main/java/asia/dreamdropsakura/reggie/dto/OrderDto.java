package asia.dreamdropsakura.reggie.dto;

import asia.dreamdropsakura.reggie.entity.OrderDetail;
import asia.dreamdropsakura.reggie.entity.Orders;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author 童话的爱
 * @since 2022-10-8
 */
@Data
public class OrderDto extends Orders implements Serializable {

    /**
     * 订单明细
     *
     */
    private List<OrderDetail> orderDetails;

    /**
     * 订单总金额
     *
     */
    private BigDecimal sumNum;

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + this.getId() +
                ", number=" + this.getNumber() +
                ", status=" + this.getStatus() +
                ", userId=" + this.getUserId() +
                ", addressBookId=" + this.getAddressBookId() +
                ", orderTime=" + this.getOrderTime() +
                ", checkoutTime=" + this.getCheckoutTime() +
                ", payMethod=" + this.getPayMethod() +
                ", amount=" + this.getAmount() +
                ", remark=" + this.getRemark() +
                ", phone=" + this.getPhone() +
                ", address=" + this.getAddress() +
                ", userName=" + this.getUserName() +
                ", consignee=" + this.getConsignee() +
                "orderDetails=" + orderDetails +
                ", sumNum=" + sumNum +
                '}';
    }
}
