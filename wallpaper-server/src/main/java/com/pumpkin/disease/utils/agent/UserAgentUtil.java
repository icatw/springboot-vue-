package com.pumpkin.disease.utils.agent;

import com.alibaba.fastjson.JSON;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * ip工具类
 *
 * @author 11921
 */
@SuppressWarnings("all")
public class UserAgentUtil {
    private static Logger logger = LoggerFactory.getLogger(UserAgentUtil.class);

    /**
     * 获取用户ip地址
     *
     * @param request 请求
     * @return ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * 解析ip地址
     *
     * @param ipAddress ip地址
     * @return 解析后的ip地址
     */
    public static String getIpSource(String ipAddress) {
        try {
            URL url = new URL("http://opendata.baidu.com/api.php?query=" + ipAddress + "&co=&resource_id=6006&oe=utf8");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "utf-8"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            Map map = JSON.parseObject(result.toString(), Map.class);
            List<Map<String, String>> data = (List) map.get("data");
            return data.get(0).get("location");
        } catch (Exception e) {
            return "";
        }
    }

    ///**
    // * 获取访问设备
    // *
    // * @param request 请求
    // * @return {@link UserAgent} 访问设备
    // */
    //public static UserAgent getUserAgent(HttpServletRequest request) {
    //    return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    //}


    /**
     * 根据http获取userAgent信息
     *
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent;
    }

    /**
     * 根据request获取userAgent，然后解析出osVersion
     *
     * @param request
     * @return
     */
    public static String getOsVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOsVersion(userAgent);
    }

    /**
     * 根据userAgent解析出osVersion
     *
     * @param userAgent
     * @return
     */
    public static String getOsVersion(String userAgent) {
        String osVersion = "";
        if (StringUtils.isBlank(userAgent)) {
            return osVersion;
        }
        String[] strArr = userAgent.substring(userAgent.indexOf("(") + 1,
                userAgent.indexOf(")")).split(";");
        if (null == strArr || strArr.length == 0) {
            return osVersion;
        }

        osVersion = strArr[1];
        logger.info("osVersion is:{}", osVersion);
        return osVersion;
    }

    /**
     * 获取操作系统对象
     *
     * @param userAgent
     * @return
     */
    private static OperatingSystem getOperatingSystem(String userAgent) {
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        OperatingSystem operatingSystem = agent.getOperatingSystem();
        return operatingSystem;
    }


    /**
     * 获取os：Windows/ios/Android
     *
     * @param request
     * @return
     */
    public static String getOs(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOs(userAgent);
    }

    /**
     * 获取os：Windows/ios/Android
     *
     * @param userAgent
     * @return
     */
    public static String getOs(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String os = operatingSystem.getGroup().getName();
        logger.info("os is:{}", os);
        return os;
    }


    /**
     * 获取deviceType
     *
     * @param request
     * @return
     */
    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDeviceType(userAgent);
    }

    /**
     * 获取deviceType
     *
     * @param userAgent
     * @return
     */
    public static String getDeviceType(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String deviceType = operatingSystem.getDeviceType().toString();
        logger.info("deviceType is:{}", deviceType);
        return deviceType;
    }

    /**
     * 获取操作系统的名字
     *
     * @param request
     * @return
     */
    public static String getOsName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOsName(userAgent);
    }

    /**
     * 获取操作系统的名字
     *
     * @param userAgent
     * @return
     */
    public static String getOsName(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String osName = operatingSystem.getName();
        logger.info("osName is:{}", osName);
        return osName;
    }


    /**
     * 获取device的生产厂家
     *
     * @param request
     */
    public static String getDeviceManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDeviceManufacturer(userAgent);
    }

    /**
     * 获取device的生产厂家
     *
     * @param userAgent
     */
    public static String getDeviceManufacturer(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String deviceManufacturer = operatingSystem.getManufacturer().toString();
        logger.info("deviceManufacturer is:{}", deviceManufacturer);
        return deviceManufacturer;
    }

    /**
     * 获取浏览器对象
     *
     * @param userAgent
     * @return
     */
    public static Browser getBrowser(String agent) {
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        Browser browser = userAgent.getBrowser();
        return browser;
    }


    /**
     * 获取browser name
     *
     * @param request
     * @return
     */
    public static String getBorderName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderName(userAgent);
    }

    /**
     * 获取browser name
     *
     * @param userAgent
     * @return
     */
    public static String getBorderName(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String borderName = browser.getName();
        logger.info("borderName is:{}", borderName);
        return borderName;
    }


    /**
     * 获取浏览器的类型
     *
     * @param request
     * @return
     */
    public static String getBorderType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderType(userAgent);
    }

    /**
     * 获取浏览器的类型
     *
     * @param userAgent
     * @return
     */
    public static String getBorderType(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String borderType = browser.getBrowserType().getName();
        logger.info("borderType is:{}", borderType);
        return borderType;
    }

    /**
     * 获取浏览器组： CHROME、IE
     *
     * @param request
     * @return
     */
    public static String getBorderGroup(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderGroup(userAgent);
    }

    /**
     * 获取浏览器组： CHROME、IE
     *
     * @param userAgent
     * @return
     */
    public static String getBorderGroup(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String browerGroup = browser.getGroup().getName();
        logger.info("browerGroup is:{}", browerGroup);
        return browerGroup;
    }

    /**
     * 获取浏览器的生产厂商
     *
     * @param request
     * @return
     */
    public static String getBrowserManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBrowserManufacturer(userAgent);
    }


    /**
     * 获取浏览器的生产厂商
     *
     * @param userAgent
     * @return
     */
    public static String getBrowserManufacturer(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String browserManufacturer = browser.getManufacturer().getName();
        logger.info("browserManufacturer is:{}", browserManufacturer);
        return browserManufacturer;
    }


    /**
     * 获取浏览器使用的渲染引擎
     *
     * @param request
     * @return
     */
    public static String getBorderRenderingEngine(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderRenderingEngine(userAgent);
    }

    /**
     * 获取浏览器使用的渲染引擎
     *
     * @param userAgent
     * @return
     */
    public static String getBorderRenderingEngine(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String renderingEngine = browser.getRenderingEngine().name();
        logger.info("renderingEngine is:{}", renderingEngine);
        return renderingEngine;
    }


    /**
     * 获取浏览器版本
     *
     * @param request
     * @return
     */
    public static String getBrowserVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBrowserVersion(userAgent);
    }

    /**
     * 获取浏览器版本
     *
     * @param userAgent
     * @return
     */
    public static String getBrowserVersion(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String borderVersion = browser.getVersion(userAgent).toString();
        return borderVersion;
    }

    /**
     * 获取注册来源
     *
     * @param request 请求
     * @return int
     */
    public static int getRegisteredSource(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        if (userAgent.contains("MicroMessenger")) {
            return 2; // 小程序
        } else if (userAgent.contains("Android") || userAgent.contains("iOS")) {
            return 3; // app
        } else {
            return 1; // PC网页
        }
    }
    /**
     * 将设备类型设置为int
     * 1 PC；2安卓；3 ios
     * @param os os
     * @return int
     */
    public static int getDeviceTypeToInt(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        String lowercaseOs = getOsName(userAgent).toLowerCase();

        if (lowercaseOs.contains("windows")) {
            return 1; // PC
        } else if (lowercaseOs.contains("android")) {
            return 2; // 安卓
        } else if (lowercaseOs.contains("ios")) {
            return 3; // 苹果
        } else {
            return 0; // 未知设备类型
        }
    }

    public static void main(String[] args) {
		//String androidUserAgent = "Mozilla/5.0 (Linux; Android 8.0; LON-AL00 Build/HUAWEILON-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044204 Mobile Safari/537.36 V1_AND_SQ_7.7.8_908_YYB_D QQ/7.7.8.3705 NetType/WIFI WebP/0.3.0 Pixel/1440";
		String winUserAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16A366 QQ/7.7.8.421 V1_IPH_SQ_7.7.8_1_APP_A Pixel/750 Core/UIWebView Device/Apple(iPhone 6s) NetType/WIFI QBWebViewType/1";
        //String winUserAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36";
        //androidUserAgent  iosUserAgent  winUserAgent
        System.out.println("浏览器组：" + getBorderGroup(winUserAgent));
        System.out.println("浏览器名字：" + getBorderName(winUserAgent));
        System.out.println("浏览器类型" + getBorderType(winUserAgent));
        System.out.println("浏览器生产商：" + getBrowserManufacturer(winUserAgent));
        //System.out.println("浏览器版本：" + getBrowserVersion(winUserAgent));
        System.out.println("设备生产厂商:" + getDeviceManufacturer(winUserAgent));
        System.out.println("设备类型:" + getDeviceType(winUserAgent));
        System.out.println("设备操作系统：" + getOs(winUserAgent));
        System.out.println("操作系统的名字：" + getOsName(winUserAgent));
        System.out.println("操作系统的版本号：" + getOsVersion(winUserAgent));
        System.out.println("操作系统浏览器的渲染引擎:" + getBorderRenderingEngine(winUserAgent));
        if (getOs(winUserAgent).contains("Windows")) {
            System.out.println("是Windows");
        }
        if (getOs(winUserAgent).contains("Linux")) {
            System.out.println("是Linux");
        } else {
            System.out.println(getOs(winUserAgent));
            System.out.println("无法匹配");
        }
    }
}
