package com.cloudsoft.ott.core.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

// ott-login에 대한 잘못된 접근을 모두 처리

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("statusCode", statusCode);

            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
            model.addAttribute("statusMessage", httpStatus.getReasonPhrase());
        } else {
            model.addAttribute("statusCode", "Unknown");
            model.addAttribute("statusMessage", "No specific error message");
        }

        model.addAttribute("errorMessage", message != null ? message.toString() : "An unexpected error occurred.");
        return "ott-login/error"; // error.html
    }
}
