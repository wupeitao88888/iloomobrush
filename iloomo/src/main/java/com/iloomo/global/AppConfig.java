package com.iloomo.global;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn 名称：AbAppConfig.java 描述：初始设置类.
 *
 * @author wpt
 * @version v1.0
 * @date：2014-07-03 下午1:33:39
 */
public class AppConfig {

    //微信支付
    public static final String WX_APP_ID = "wxab37c2bd3bbb2b0b";
    public static final String WX_API_KEY = "mnbvcxzlkjhgfdsapoiuytrewq098765";
    
    // 支付宝商户PID
    public static final String PARTNER = "2088121413289894";
    //  支付宝商户收款账号
    public static final String SELLER = "3295822272@qq.com";
    //  支付宝商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOtGffx6x0pnsZulNvqgJWrFAXi8FWDms9+BPXt774iFYQn5Z8pv29Zczt37qiF5MjI4SWkgLzExboT+WnOGg1QEQ5uPJLnmGFqD1RfqcIpWdddctHSmyxgGZyvPNpoCIOGO5CozrsBwdolgNGFGtbZdLzjY3QxSy4AhK2vu4agDAgMBAAECgYEAzo4/06bzSmaJLo25HAX3c6psJVRzLHS+3G25zay8Wk5HKJuXPMpB5gvq7kahUev2XtK92JaR+A1e61HnlV818aEyqUFcaaZHoVjWXXoH3FpZBjPuyRvLeEa58pmfbHFNsuFsr/9d3+5Pp6L+QusZwLoZraIkLh8pZeb3A7vaankCQQD2AgAY+92YAYH7bG/IVuD0rEZNwlYcjrq2zcecpwUAsUDyu1C1rN/gVK0Buip8ZaoKzWoTWaG4CuRE2rZhGnG1AkEA9NTlLp29TbkgkKsDLFmWb5ZjYcdVpGG/Tl7EDQkHuA2M50ido8zjl5h5qIBD9oyYWeZLx7m5Ogx6awAmvGkl1wJBANLOLgU6ts2N5TNs3XEAOa6QfYIemKnKCOei+H0dhiTsv2oA5412wTU7ejEztTp557dwBIjmUrUh6C+0d3pILc0CQFNvUgMG4oJgOobSoKw2g5vXhlrGEZuJbCaLCQjgMO86PvB6wK4XnuAEqEESJRcaZrkMUHrqAgOUSQu563l949UCQA7anrBVh/KMQjPq8WQ+UZ1sThJWV4U/VcQ5eElag3I8w8/qm8l0ioP8QcoaANxjsKLHBV7eq0VXTXlIYMr8J18=";
    
  
    
    
    
    public static final String ACTION_ZHIFUBAO_SUCCESS = "com.iloomo.paysdk.ACTION_ZFBSHARESHARE";
    public static final String ACTION_ZHIFUBAO_FAILD = "com.iloomo.paysdk.ACTION_ZFBSHAREFAILD";
    public static final String ACTION_ZHIFUBAO_CHECKING = "com.iloomo.paysdk.ACTION_ZFBSHARECHECKING";
    public static final String ACTION_WXSHARE_SUCCESS = "com.iloomo.paysdk.ACTION_WXSHARESHARE";
    public static final String ACTION_WXSHARE_FAILD = "com.iloomo.paysdk.ACTION_WXSHAREFAILD";
    public static final String ACTION_WXPAY_SUCCESS = "com.iloomo.paysdk.ACTION_WXPAYSUCCESS";
    public static final String ACTION_WXPAY_CANCLE = "com.iloomo.paysdk.ACTION_WXPAYCANCLE";
    public static final String ACTION_WXPAY_FAILD = "com.iloomo.paysdk.ACTION_WXPAYFALID";
    public static final String ACTION_WXPAY_UNSUPPORT = "com.iloomo.paysdk.ACTION_WXPAYUNSUPPORT";
    
    
    public static final String BASEURL="http://www.tangbeian.cn";
    public static final String API="/api/";
    //微信订单下单地址
    public static final String GET_WEIXINPAY=BASEURL+API+"Weixin_payUnifiedorder";
    //获取验证码请求参数  String phonenumber,String vtype//应用到什么地方的类型比如是注册还是找回密码等等
    public static final String GET_CODE=BASEURL+"/yanhuibao/front/account/sendValidate.html";
    // 校验验证码  String phonenumber,String vcode vcode是验证码校验验证码请求参数
    public static final String SEND_CODE=BASEURL+"/yanhuibao/front/account/checkVcode.html";





        /**  UI设计的基准宽度. */
        public static int UI_WIDTH = 720;

        /**  UI设计的基准高度. */
        public static int UI_HEIGHT = 1080;

        /** 默认 SharePreferences文件名. */
        public static String SHARED_PATH = "app_share";

        /** 默认下载文件地址. */
        public static  String DOWNLOAD_ROOT_DIR = "download";

        /** 默认下载图片文件地址. */
        public static  String DOWNLOAD_IMAGE_DIR = "images";

        /** 默认下载文件地址. */
        public static  String DOWNLOAD_FILE_DIR = "files";

        /** APP缓存目录. */
        public static  String CACHE_DIR = "cache";

        /** DB目录. */
        public static  String DB_DIR = "db";

        /** 默认缓存超时时间设置. */
        public static int IMAGE_CACHE_EXPIRES_TIME = 3600*24*3;

        /** 缓存大小  单位10M. */
        public static int MAX_CACHE_SIZE_INBYTES = 10*1024*1024;
    /** Textview为空. */
    public static String TEXTVIEW_NULL = "TextView为空";
    /** Text为空. */
    public static String TEXT_NULL = "Text为空";

}