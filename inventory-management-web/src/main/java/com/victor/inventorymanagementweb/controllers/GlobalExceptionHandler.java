package com.victor.inventorymanagementweb.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                     RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Invalid input — please enter valid numbers");
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex,
                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Something went wrong, please try again");
        return "redirect:/";
    }
}
