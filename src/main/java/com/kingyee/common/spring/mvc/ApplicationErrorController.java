package com.kingyee.common.spring.mvc;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * 统一出错处理
 * @author ph
 */
@Controller
@RequestMapping(value = "/")
public class ApplicationErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (statusCode >= HttpStatus.INTERNAL_SERVER_ERROR.value() || statusCode <= HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value()) {
                return "error/5xx";
            }
        }
        return "error/error";
    }
}