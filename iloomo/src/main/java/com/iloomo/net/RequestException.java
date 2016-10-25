package com.iloomo.net;


public class RequestException extends Exception{
	private static final long serialVersionUID = 1L;
	/**
	 * {@link java.io.EOFException}
	 * 抛出此类异常，表示连接丢失，也就是说网络连接的另一端非正常关闭连接（可能是主机断电、网线出现故障等导致）
	 */
	public final  static int SERVER_CLOSED_EXCEPTION = 0x01;

	/**
	 * {@link java.net.SocketException}
	 * 抛出此类异常，表示无法连接，也就是说当前主机不存在
	 */
	public final  static int CONNECT_EXCEPTION = 0X02;

	/**
	 * {@link java.net.SocketException}
	 * 抛出此类异常，表示
	 * <ul>
	 * <li>1、连接正常关闭，也就是说另一端主动关闭连接</li>
	 * <li>2、表示一端关闭连接，而另一端此时在读数据</li>
	 * <li>3、表示一端关闭连接，而另一端此时在发送数据</li>
	 * <li>4、表示连接已关闭，但还继续使用（也就是读/写操作）此连接</li>
	 * </ul>
	 */
	public final  static int SOCKET_EXCEPTION = 0x03;

	/**
	 * {@link java.net.BindException}
	 *  抛出此类异常，表示端口已经被占用
	 */
	public final  static int BIND_EXCEPTION = 0x04;

	/**
	 * {@link org.apache.http.conn.ConnectTimeoutException}
	 * 连接超时
	 */
	public final  static int CONNECT_TIMEOUT_EXCEPTION = 0x05;

	/**
	 * {@link java.io.UnsupportedEncodingException}
	 * 不支持的编码异常
	 */
	public final  static int UNSUPPORTED_ENCODEING_EXCEPTION = 0x06;

	/**
	 * {@link java.net.SocketTimeoutException}
	 * socket 超时异常
	 */
	public final  static int  SOCKET_TIMEOUT_EXCEPTION = 0x06;

	/**
	 * {@link org.apache.http.client.ClientProtocolException}
	 * 客户端协议异常
	 */
	public final  static int  CLIENT_PROTOL_EXCEPTION = 0x07;

	/**
	 * {@link java.io.IOException}
	 * 读取异常
	 */
	public final  static int IO_EXCEPTION = 0x08;
	
	
	private int code;
	
	public RequestException(int code, Throwable throwable) {
		super(throwable);
		this.code = code;
	}

	public RequestException(int code, String msg) {
		super(msg);
		this.code = code;
	}
	
}
