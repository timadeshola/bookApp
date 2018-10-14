package co.zonetechpark.booktest.booktest.core.security;

import co.zonetechpark.booktest.booktest.core.exceptions.CustomException;
import co.zonetechpark.booktest.booktest.jpa.entity.Role;
import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.jpa.repos.UserRepository;
import co.zonetechpark.booktest.booktest.service.ApplicationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenHelper {

    private MyUserDetails myUserDetails;
    private PasswordEncoder passwordEncoder;
    private ApplicationService applicationService;
    private UserRepository userRepository;
    private AuthorityDetails authorityDetails;

    @Autowired
    public TokenHelper(MyUserDetails myUserDetails, PasswordEncoder passwordEncoder, ApplicationService applicationService, UserRepository userRepository, AuthorityDetails authorityDetails) {
        this.myUserDetails = myUserDetails;
        this.passwordEncoder = passwordEncoder;
        this.applicationService = applicationService;
        this.userRepository = userRepository;
        this.authorityDetails = authorityDetails;
    }

    @Value("${jwt.app.secret}")
    private String jwtSecretKey;

    @Value("${jwt.expires.in}")
    private Long jwtExpiresIn;

    @Value("${jwt.token.prefix}")
    private String jwtTokenPrefix;

    @Value("${jwt.header}")
    private String jwtHeader;

    private String secretKey = "";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(applicationService.encryptStringData(passwordEncoder.encode(jwtSecretKey)).getBytes());
    }

    public String createToken(String username, Set<Role> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpiresIn);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS512, secretKey)//
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        Optional<User> userByUsername = userRepository.findUserByUsername(getUsername(token));
        if(userByUsername.isPresent()) {
            User user = userByUsername.get();
            return new UsernamePasswordAuthenticationToken(userDetails, authorityDetails.getAuthorities(user), userDetails.getAuthorities());
        }
        return null;
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(jwtHeader);
        if (bearerToken != null && bearerToken.startsWith(jwtTokenPrefix)) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("error validating token ==> {}", e.getMessage());
            throw new CustomException("Expired or invalid JWT token", HttpStatus.UNAUTHORIZED);
        }
    }

}
