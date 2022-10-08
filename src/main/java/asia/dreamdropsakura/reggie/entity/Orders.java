package asia.dreamdropsakura.reggie.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author 童话的爱
 * @since 2022-09-25
 */
@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     *
     */
    private Long id;

    /**
     * 订单号
     *
     */
    private String number;

    /**
     * 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
     *
     */
    private Integer status;

    /**
     * 下单用户
     *
     */
    private Long userId;

    /**
     * 地址id
     *
     */
    private Long addressBookId;

    /**
     * 下单时间
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime orderTime;

    /**
     * 结账时间
     *
     */
    private LocalDateTime checkoutTime;

    /**
     * 支付方式 1微信,2支付宝
     *
     */
    private Integer payMethod;

    /**
     * 实收金额
     *
     */
    private BigDecimal amount;

    /**
     * 备注
     *
     */
    private String remark;

    private String phone;

    private String address;

    private String userName;

    private String consignee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getAddressBookId() {
        return addressBookId;
    }

    public void setAddressBookId(Long addressBookId) {
        this.addressBookId = addressBookId;
    }
    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }
    public LocalDateTime getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(LocalDateTime checkoutTime) {
        this.checkoutTime = checkoutTime;
    }
    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    @Override
    public String toString() {
        return "Orders{" +
            "id=" + id +
            ", number=" + number +
            ", status=" + status +
            ", userId=" + userId +
            ", addressBookId=" + addressBookId +
            ", orderTime=" + orderTime +
            ", checkoutTime=" + checkoutTime +
            ", payMethod=" + payMethod +
            ", amount=" + amount +
            ", remark=" + remark +
            ", phone=" + phone +
            ", address=" + address +
            ", userName=" + userName +
            ", consignee=" + consignee +
        "}";
    }
}
