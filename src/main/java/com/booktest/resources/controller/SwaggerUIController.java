package com.booktest.resources.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author John Adeshola
 * <pre>
 *     Controller to redirect to swagger-ui page
 * </pre>
 */
@Controller
public class SwaggerUIController {

    @RequestMapping(value = "/swagger-ui")
    public String index() {
        return "redirect:swagger-ui.html";
    }
}
