package asia.dreamdropsakura.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一结果返回类
 *
 * @author 童话的爱
 * @since 2022-9-17
 */
@Data
public class Result<T> {

    /**
     * 编码：1成功，其它数字为失败
     *
     */
    private Integer code;

    /**
     * 错误信息
     *
     */
    private String msg;

    /**
     * 数据
     *
     */
    private T data;

    /**
     * 动态数据
     */
    private Map map = new HashMap();

    /**
     * 返回成功对象
     *
     * @param object
     * @return
     * @param <T>
     */
    public static <T> Result<T> success(T object) {
        Result<T> r = new Result<>();
        r.data = object;
        r.code = 1;
        return r;
    }

    /**
     * 返回错误对象
     *
     * @param msg
     * @return
     * @param <T>
     */
    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
