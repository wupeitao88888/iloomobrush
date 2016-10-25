package com.iloomo.net;

public class HttpConstant {
	/**
	 * * 两种HTTP请求方式 * **
	 */
	public static final String HTTP_POST = "POST";
	public static final String HTTP_GET = "GET";
	/**
	 * * 是否启用gzip * **
	 */
	public static boolean isGzip = true;
	/**
	 * * 请求线程是否停止 * **
	 */
	public static boolean IS_STOP_REQUEST = false;
	/**
	 * * 最大连接次数 * **
	 */
	public static final int CONNECTION_COUNT = 3;
	/**
	 * * 连接超时、读取超时 * **
	 */
	public static final int NET_READ_TIMEOUT = 30000;
	public static final int NET_CONNECTION_TIMEOUT = 30000;
	/**
	 * * 连接失败的提示文本 * **
	 */
	public static final String CONNECT_TIME_OUT = "连接超时，请返回重试";
	public static final String ERROR_MESSAGE = "您的网络状况不是很好，数据拉取失败，请重试";

}
