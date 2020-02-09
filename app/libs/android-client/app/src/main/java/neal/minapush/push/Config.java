package neal.minapush.push;

/**
 * Created by neal on 2014/12/2.
 */
public class Config {
    /**
     * 服务器地址
     */
    public static String HOSTNAME = "192.168.4.1";
    /**
     * 服务器端口号
     */
    public static final int PORT = 8080;
    /**
     * 连接超时时间，30 seconds
     */
    public static final long SOCKET_CONNECT_TIMEOUT = 30 * 1000L;
    /**
     * 长连接心跳包发送频率，10 seconds
     */
    public static final int KEEP_ALIVE_TIME_INTERVAL = 10;
    /**
     * 长连接心跳包应答超时
     */
    public static final int KEEP_ALIVE_RESPONSE_TIMEOUT = 5;
    /**
     * 心跳包 ping message
     */
    public static final String PING_MESSAGE="yzt";
    /**
     * 心跳包 pong message
     */
    public static final String PONG_MESSAGE="zzy";

}
