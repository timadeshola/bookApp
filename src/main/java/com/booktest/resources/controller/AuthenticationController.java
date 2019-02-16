package com.booktest.resources.controller;

import com.booktest.core.exceptions.CustomException;
import com.booktest.core.security.AuthorityDetails;
import com.booktest.core.security.JwtAuthenticationRequest;
import com.booktest.core.security.TokenHelper;
import com.booktest.core.security.UserTokenState;
import com.booktest.jpa.entity.User;
import com.booktest.jpa.repos.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Api(value = "api/v1/auth", description = "Endpoint for authentication management", tags = "Authentication Management")
public class AuthenticationController {

    private TokenHelper tokenHelper;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private AuthorityDetails authorityDetails;

    @Value("${jwt.expires.in}")
    private String expiresIn;

    @Autowired
    public AuthenticationController(TokenHelper tokenHelper, AuthenticationManager authenticationManager, UserRepository userRepository, AuthorityDetails authorityDetails) {
        this.tokenHelper = tokenHelper;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authorityDetails = authorityDetails;
    }

    @PostMapping("login")
    @ApiOperation(httpMethod = "POST", value = "Resource to login into the system", responseReference = "Authentication token", nickname = "createAuthenticationToken")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Great! User authenticated successfully"),
            @ApiResponse(code = 400, message = "Something went wrong, check you request"),
            @ApiResponse(code = 401, message = "Sorry, you are not authenticated"),
            @ApiResponse(code = 404, message = "Resource not found, i guess your url is not correct"),
            @ApiResponse(code = 428, message = "Precondition Required, Illegal Argument supplied")
    })
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        try {
            // Perform the security
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

            // Inject into security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // token creation
            Optional<User> optionalUser = userRepository.findUserByUsername(authenticationRequest.getUsername());
            if (!optionalUser.isPresent()) {
                throw new CustomException("User not found", HttpStatus.NOT_FOUND);
            }
            User user = optionalUser.get();
            String jwtToken = tokenHelper.createToken(authenticationRequest.getUsername(), user.getRoles());

            log.info("#########TOKEN::: {}", jwtToken);
            return new ResponseEntity<>(new UserTokenState(jwtToken, Long.valueOf(expiresIn)), HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("csrf-token")
    public ResponseEntity<?> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute((CsrfToken.class.getName()));
        log.info("csrf token -> {} ", csrfToken.getToken());
        return new ResponseEntity<>(csrfToken, HttpStatus.OK);
    }

}
