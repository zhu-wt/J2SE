package com.se.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * httpClient完整封装
 *
 * @author: wtz
 * Date:2018/12/7
 */
public class HttpClientUtil {

    private static HttpClientUtil httpClient = new HttpClientUtil();
    private HttpClient client = null;
    private String charset = "utf-8";
    private int timeout = 20000;
    private boolean useProxy = false;
    private String proxyHost = null;
    private int proxyPort;
    private String proxyUsername = null;
    private String proxyPassword = null;
    private boolean initialized = false;


    public static HttpClientUtil getInstance() {
        return httpClient;
    }

    private HttpClientUtil() {
        /**
         * 多线程同时访问httpclient，例如同时从一个站点上下载多个文件。对于同一个HttpConnection同一个时间只能有一个线程访问，
         * 为了保证多线程工作环境下不产生冲突，httpclient使用了一个多线程连接管理器
         */
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
        init();
    }

    public synchronized void init() {
        if (charset != null && !charset.trim().equals("")) {
            client.getParams().setParameter("http.protocol.content-charset", charset);
            client.getParams().setContentCharset(charset);
        }
        if (timeout > 0) {
            client.getParams().setSoTimeout(timeout);
        }
        if (useProxy && proxyHost != null &&
                !proxyHost.trim().equals("") && proxyPort > 0) {
            HostConfiguration hc = new HostConfiguration();
            hc.setProxy(proxyHost, proxyPort);
            client.setHostConfiguration(hc);
            if (proxyUsername != null && !proxyUsername.trim().equals("") &&
                    proxyPassword != null && !proxyPassword.trim().equals("")) {
                client.getState().setProxyCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(proxyUsername, proxyPassword));
            }
        }
        initialized = true;
        System.out.println("HttpInvoker初始化完成");
    }

    /**
     * get 方法
     *
     * @param url
     * @throws Exception
     */
    public String get(String url, Map<String, Object> params) throws Exception {
        return invoke(url, params, false);
    }

    /**
     * post
     *
     * @param url
     * @throws Exception
     */
    public String post(String url, Map<String, Object> params) throws Exception {
        return invoke(url, params, true);
    }


    public String invoke(String url, Map<String, Object> params, boolean isPost) throws Exception {
        System.out.println("HTTP调用[" + (isPost ? "POST" : "GET") + "][" + url + "][" + params + "]");
        HttpMethod httpMethod = null;
        String result = "";
        try {
            if (isPost && params != null && params.size() > 0) {
                Iterator paramKeys = params.keySet().iterator();
                httpMethod = new PostMethod(url);
                NameValuePair[] form = new NameValuePair[params.size()];
                int formIndex = 0;
                while (paramKeys.hasNext()) {
                    String key = (String) paramKeys.next();
                    Object value = params.get(key);
                    if (value != null && value instanceof String && !value.equals("")) {
                        form[formIndex] = new NameValuePair(key, (String) value);
                        formIndex++;
                    } else if (value != null && value instanceof String[] &&
                            ((String[]) value).length > 0) {
                        NameValuePair[] tempForm =
                                new NameValuePair[form.length + ((String[]) value).length - 1];
                        for (int i = 0; i < formIndex; i++) {
                            tempForm[i] = form[i];
                        }
                        form = tempForm;
                        for (String v : (String[]) value) {
                            form[formIndex] = new NameValuePair(key, (String) v);
                            formIndex++;
                        }
                    } else {
                        form[formIndex] = new NameValuePair(key, JSON.toJSONString(value));
                        formIndex++;
                    }
                }
                ((PostMethod) httpMethod).setRequestBody(form);
            } else {
                if (params != null && params.size() > 0) {
                    Iterator paramKeys = params.keySet().iterator();
                    StringBuffer getUrl = new StringBuffer(url.trim());
                    if (url.trim().indexOf("?") > -1) {
                        if (url.trim().indexOf("?") < url.trim().length() - 1 &&
                                url.trim().indexOf("&") < url.trim().length() - 1) {
                            getUrl.append("&");
                        }
                    } else {
                        getUrl.append("?");
                    }
                    while (paramKeys.hasNext()) {
                        String key = (String) paramKeys.next();
                        Object value = params.get(key);
                        if (value != null && value instanceof String && !value.equals("")) {
                            getUrl.append(key).append("=").append(value).append("&");
                        } else if (value != null && value instanceof String[] &&
                                ((String[]) value).length > 0) {
                            for (String v : (String[]) value) {
                                getUrl.append(key).append("=").append(v).append("&");
                            }
                        } else {
                            getUrl.append(key).append("=").append(JSON.toJSONString(value)).append("&");
                        }
                    }
                    if (getUrl.lastIndexOf("&") == getUrl.length() - 1) {
                        httpMethod = new GetMethod(getUrl.substring(0, getUrl.length() - 1));
                    } else {
                        httpMethod = new GetMethod(getUrl.toString());
                    }
                } else {
                    httpMethod = new GetMethod(url);
                }
            }

            client.executeMethod(httpMethod);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpMethod.getResponseBodyAsStream(), "ISO-8859-1"));
            String line = null;
            String html = null;
            while ((line = reader.readLine()) != null) {
                if (html == null) {
                    html = "";
                } else {
                    html += "\r\n";
                }
                html += line;
            }
            if (html != null) {
                result = new String(html.getBytes("ISO-8859-1"), charset);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("连接超时[" + url + "]");
            throw e;
        } catch (java.net.ConnectException e) {
            System.out.println("连接失败[" + url + "]");
            throw e;
        } catch (Exception e) {
            System.out.println("连接时出现异常[" + url + "]");
            throw e;
        } finally {
            if (httpMethod != null) {
                try {
                    httpMethod.releaseConnection();
                } catch (Exception e) {
                    System.out.println("释放网络连接失败[" + url + "]");
                    throw e;
                }
            }
        }

        return result;
    }


    public HttpClientUtil(String charset, int timeout, boolean useProxy,
                          String proxyHost, int proxyPort, String proxyUsername,
                          String proxyPassword) {
        client = new HttpClient(new MultiThreadedHttpConnectionManager());
        if (charset != null && !charset.trim().equals("")) {
            this.charset = charset;
        }
        if (timeout > 0) {
            this.timeout = timeout;
        }
        client.getParams().setParameter("http.protocol.content-charset", charset);
        client.getParams().setContentCharset(charset);
        client.getParams().setSoTimeout(timeout);
        if (useProxy && proxyHost != null &&
                !proxyHost.trim().equals("") && proxyPort > 0) {
            HostConfiguration hc = new HostConfiguration();
            hc.setProxy(proxyHost, proxyPort);
            client.setHostConfiguration(hc);
            if (proxyUsername != null && !proxyUsername.trim().equals("") &&
                    proxyPassword != null && !proxyPassword.trim().equals("")) {
                client.getState().setProxyCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(proxyUsername, proxyPassword));
            }
        }
        initialized = true;
        System.out.println("HttpInvoker初始化完成");
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public synchronized boolean isInitialized() {
        return initialized;
    }


    public static void main(String[] args) throws Exception {
        String url = "http://192.168.1.21:8088/settlement/ticketContent/details.json?id=2";
        String rs = HttpClientUtil.getInstance().get(url, null);
        System.out.println(rs);


        url = "http://192.168.1.21:8088/settlement/management/customer/details.json";
        Map<String, Object> map = new HashMap<>();
        map.put("token", "USER_TOKEN_administrator_0de6ebfe-4798-4678-96ed-bbb81841e4c3");
        map.put("id", 1);
        rs = HttpClientUtil.getInstance().post(url, map);
        System.out.println(rs);
    }

}

