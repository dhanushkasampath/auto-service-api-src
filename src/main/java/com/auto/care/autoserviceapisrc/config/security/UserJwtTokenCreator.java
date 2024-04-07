package com.auto.care.autoserviceapisrc.config.security;


import com.auto.care.autoserviceapisrc.entity.User;
import com.auto.care.autoserviceapisrc.util.Constants;
import com.auto.care.autoserviceapisrc.util.JwtTokenTypeEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserJwtTokenCreator {

    private final Logger logger = LoggerFactory.getLogger(UserJwtTokenCreator.class);

    private static final String JWT_HEADER_TYPE_KEY = "typ";
    private static final String JWT_HEADER_TYPE_VALUE = "JWT";

    @Value("${security.jwt.sign.key}")
    private String jwtSignKey;

    @Value("${user.default.password}")
    private String passwordKey;

    @Value("${security.jwt.expiretime.hours}")
    private Integer jwtExpireTimeInHours;

    @Value("${security.email.invitation.jwt.expiretime.hours}")
    private Integer emailInvitationJwtExpireTimeInHours;

    private final ZoneId zoneId;

    public UserJwtTokenCreator(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public String generateJwtToken(User user, JwtTokenTypeEnum type) {
        logger.debug("generating JWT token of type:{} to user:{}", type, user.getUserName());
        Date currentDateTime = getCurrentDateTime();
        long expireDateTime = currentDateTime.getTime() + (jwtExpireTimeInHours * 60 * 60 * 1000);
        long emailInvitationExpireDateTime = currentDateTime.getTime() + (emailInvitationJwtExpireTimeInHours * 60 * 60 * 1000);

        String jwtToken = null;

        if (type.equals(JwtTokenTypeEnum.AUTHORIZED_TOKEN)) {
            jwtToken = Jwts.builder()
                    .setIssuedAt(currentDateTime)
                    .setExpiration(new Date(expireDateTime))
                    .addClaims(getCustomerDetailMap(user, JwtTokenTypeEnum.AUTHORIZED_TOKEN))
                    .setHeaderParam(JWT_HEADER_TYPE_KEY, JWT_HEADER_TYPE_VALUE)
                    .signWith(
                            SignatureAlgorithm.HS256,
                            jwtSignKey.getBytes()
                    )
                    .compact();
        } else if (type.equals(JwtTokenTypeEnum.INVITATION_TOKEN)) {
            jwtToken = Jwts.builder()
                    .setIssuedAt(currentDateTime)
                    .setExpiration(new Date(emailInvitationExpireDateTime))
                    .addClaims(getCustomerDetailMap(user, JwtTokenTypeEnum.INVITATION_TOKEN))
                    .setHeaderParam(JWT_HEADER_TYPE_KEY, JWT_HEADER_TYPE_VALUE)
                    .signWith(
                            SignatureAlgorithm.HS256,
                            jwtSignKey.getBytes()
                    )
                    .compact();
        }

        return jwtToken;
    }

    public String refreshJwt(User user) {

        logger.debug("generate refresh token for userName : {}", user.getUserName());
        Map<String, Object> customerDetailsMap = new HashMap<>();

        customerDetailsMap.put(Constants.USER_ID_KEY, user.getUserId());
        customerDetailsMap.put(Constants.USER_NAME_KEY, user.getUserName());
        customerDetailsMap.put(Constants.USER_EMAIL_KEY, user.getEmail());

        customerDetailsMap.put(Constants.USER_TYPE_KEY, user.getUserType().toString());

        logger.debug("claims size : {}", customerDetailsMap.size());

        Date currentDateTime = getCurrentDateTime();
        long expireDateTime = currentDateTime.getTime() + (jwtExpireTimeInHours * 60 * 60 * 1000);
        return Jwts.builder()
                .setIssuedAt(currentDateTime)
                .setExpiration(new Date(expireDateTime))
                .addClaims(customerDetailsMap)
                .setHeaderParam(JWT_HEADER_TYPE_KEY, JWT_HEADER_TYPE_VALUE)
                .signWith(
                        SignatureAlgorithm.HS256,
                        jwtSignKey.getBytes()
                )
                .compact();
    }

    private Date getCurrentDateTime() {
        return Date.from(ZonedDateTime.now(zoneId).toInstant());
    }

    private Map<String, Object> getCustomerDetailMap(User user, JwtTokenTypeEnum type) {

        Map<String, Object> customerDetailsMap = new HashMap<>();

        customerDetailsMap.put(Constants.USER_ID_KEY, user.getUserId());
        customerDetailsMap.put(Constants.USER_NAME_KEY, user.getUserName());
        customerDetailsMap.put(Constants.USER_EMAIL_KEY, user.getEmail());
        customerDetailsMap.put(Constants.USER_TYPE_KEY, user.getUserType().toString());

        if (type.equals(JwtTokenTypeEnum.INVITATION_TOKEN)) {
            String pw = user.getPassword() == null ? passwordKey : user.getPassword();
            customerDetailsMap.put(Constants.USER_ONE_TIME_AUTH_KEY, (pw + "_" + user.getEmail()).hashCode());
        }
        return customerDetailsMap;
    }

}
