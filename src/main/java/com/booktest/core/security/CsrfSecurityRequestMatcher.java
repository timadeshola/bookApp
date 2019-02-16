package com.booktest.core.security;

import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

@Component
public class CsrfSecurityRequestMatcher implements RequestMatcher {

    private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

    private final RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("(/login*|/resources*/.*|/api*/.*)", "POST");

    @Override
    public boolean matches(HttpServletRequest request)  {
        if(unprotectedMatcher.matches(request)) {
            return false;
        }
        return !allowedMethods.matcher(request.getMethod()).matches();
    }

}
