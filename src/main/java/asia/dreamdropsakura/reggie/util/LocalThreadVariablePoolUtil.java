package asia.dreamdropsakura.reggie.util;

/**
 * 用于获取与设置线程自己的变量
 *
 * @author 童话的爱
 * @since 2022-9-19
 */
public class LocalThreadVariablePoolUtil {

    private static final ThreadLocal<Long> THREAD_LOCAL_USERID = new ThreadLocal<>();

    /**
     * 设置线程独有的id 变量
     *
     * @param id
     */
    public static void setCurrentThreadUserid(long id) {
        THREAD_LOCAL_USERID.set(id);
    }

    /**
     * 获取线程独有的id 变量
     *
     * @return
     */
    public static Long getCurrentThreadUserid() {
        return THREAD_LOCAL_USERID.get();
    }
}
