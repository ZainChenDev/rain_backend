package org.example.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Servlet 工具类
 */
public class ServletUtils {

    /**
     * 在非 Controller 层（如工具类、拦截器、过滤器、服务层）中获取当前线程绑定的 HttpServletRequest 和 HttpServletResponse 的方式
     *
     * @return ServletRequestAttributes
     */
    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 获取当前线程绑定的 HttpServletRequest 对象
     *
     * @return 当前请求的 HttpServletRequest 对象
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取当前线程绑定的 HttpServletResponse 对象
     *
     * @return 当前请求的 HttpServletResponse 对象
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }
}
