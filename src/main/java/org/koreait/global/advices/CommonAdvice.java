package org.koreait.global.advices;

import jakarta.servlet.http.HttpServletRequest;
import org.koreait.global.exceptions.CommonException;
import org.koreait.global.exceptions.script.AlertBackException;
import org.koreait.global.exceptions.script.AlertException;
import org.koreait.global.exceptions.script.AlertRedirectException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;

@ControllerAdvice(annotations = Controller.class)
public class CommonAdvice {
    @ExceptionHandler(Exception.class)
    public ModelAndView errorHandler(Exception e, HttpServletRequest request) {

        ModelAndView mv = new ModelAndView();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 기본 응답 코드 500
        String tpl = "error/error";


        if (e instanceof CommonException commonException) {
            status = commonException.getStatus();

            if (e instanceof AlertException) {
                tpl = "common/_execute_script";
                String script = String.format("alert('%s');", e.getMessage());

                if (e instanceof AlertBackException alertBackException) {
                    script += String.format("%s.history.back();", alertBackException.getTarget());
                }

                if (e instanceof AlertRedirectException alertRedirectException) {
                    String url = alertRedirectException.getUrl();
                    if (!url.startsWith("http")) { // 외부 URL이 아닌 경우
                        url = request.getContextPath() + url;
                    }

                    script += String.format("%s.location.replace('%s');", alertRedirectException.getTarget(), url);
                }

                mv.addObject("script", script);
            }

        } else if (e instanceof AccessDeniedException) {
            status = HttpStatus.UNAUTHORIZED;
        }

        String url = request.getRequestURI();
        String qs = request.getQueryString();

        if (StringUtils.hasText(qs)) url += "?" + qs;

        e.printStackTrace();

        mv.addObject("message", e.getMessage());
        mv.addObject("status", status.value());
        mv.addObject("method", request.getMethod());
        mv.addObject("path", url);
        mv.setStatus(status);
        mv.setViewName(tpl);

        e.printStackTrace();

        return mv;
    }
}