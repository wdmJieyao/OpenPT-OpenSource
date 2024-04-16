package fun.hzaw.commonbean.constants;

/**
 * 线程常量
 */
public class ThreadConstant {

    private ThreadConstant() {
    }

    /**
     * CPU核心线程数
     */
    public static final int CPU_CORE_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * 最大线程数
     */
    public static final int DF_MAXIMUM_POOL_SIZE = 30;

    /**
     * 等待队列大小
     */
    public static final int DF_WAIT_LIST_SIZE = 200;

    /**
     * 空闲线程存活时间
     */
    public static final long DF_KEEP_ALIVE_TIME = 10;
}
