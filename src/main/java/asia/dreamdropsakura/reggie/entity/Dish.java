package asia.dreamdropsakura.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜肴实体类
 *
 * @author 童话的爱
 * @since 2022-9-20
 */
@Data
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    /**
     * 菜肴名称
     *
     */
    private String name;


    /**
     * 菜肴分类id
     *
     */
    private Long categoryId;


    /**
     * 菜肴价格
     *
     */
    private BigDecimal price;


    /**
     * 商品码
     *
     */
    private String code;


    /**
     * 图片
     *
     */
    private String image;


    /**
     * 描述信息
     *
     */
    private String description;


    /**
     * 商品状态 -
     * 0 停售 1 起售
     *
     */
    private Integer status;


    /**
     * 顺序
     *
     */
    private Integer sort;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 逻辑删除
     *
     */
    @TableLogic
    private Integer isDeleted;
}
