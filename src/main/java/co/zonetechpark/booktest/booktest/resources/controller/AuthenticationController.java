package co.zonetechpark.booktest.booktest.resources.controller;

import co.zonetechpark.booktest.booktest.core.CustomException;
import co.zonetechpark.booktest.booktest.core.security.JwtAuthenticationRequest;
import co.zonetechpark.booktest.booktest.core.security.TokenHelper;
import co.zonetechpark.booktest.booktest.core.security.UserTokenState;
import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.jpa.repos.UserRepository;
import io.swagger.annotations.Api;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Api(tags = "authentication")
public class AuthenticationController {

    private TokenHelper tokenHelper;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    @Value("${jwt.expires.in}")
    private String expiresIn;

    @Autowired
    public AuthenticationController(TokenHelper tokenHelper, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.tokenHelper = tokenHelper;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("login")
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
            String jwtToken = tokenHelper.createToken(authenticationRequest.getUsername(), optionalUser.get().getRoles());

            log.info("#########TOKEN::: {}", jwtToken);
            return new ResponseEntity<>(new UserTokenState(jwtToken, Long.valueOf(expiresIn)), HttpStatus.OK);
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("logout")
    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Optional<User> user = userRepository.findUserByUsername(userDetails.getUsername());
            user.ifPresent(user1 -> user1.setLastLogoutDate(new Timestamp(System.currentTimeMillis())));
            user.ifPresent(user1 -> userRepository.saveAndFlush(user1));
            auth.setAuthenticated(false);
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return new ResponseEntity<Object>(auth, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("csrf-token")
    public ResponseEntity<?> getCsrfToken(HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute((CsrfToken.class.getName()));
        log.info("csrf token -> {} ", csrfToken.getToken());
        return new ResponseEntity<>(csrfToken, HttpStatus.OK);
    }

}
