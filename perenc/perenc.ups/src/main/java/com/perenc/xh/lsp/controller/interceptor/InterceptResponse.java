package com.perenc.xh.lsp.controller.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class InterceptResponse implements ResponseBodyAdvice<Object>{

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        return true;
    }

    /**
     * 此方法是拦截返回值,并且操作返回值的,这是一个全局过滤
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        ServletServerHttpRequest req=(ServletServerHttpRequest)request;
        HttpServletRequest servletRequest = req.getServletRequest();
        servletRequest.setAttribute("response", JSONObject.toJSONString(body));
        return body;
    }

}