package com.auto.care.autoserviceapisrc.service.impl;

import com.auto.care.autoserviceapisrc.beans.UserLoginRequestDto;
import com.auto.care.autoserviceapisrc.beans.UserLoginResponseDto;
import com.auto.care.autoserviceapisrc.config.security.UserJwtTokenCreator;
import com.auto.care.autoserviceapisrc.entity.User;
import com.auto.care.autoserviceapisrc.exception.AutoServiceException;
import com.auto.care.autoserviceapisrc.repository.UserRepository;
import com.auto.care.autoserviceapisrc.service.EmailService;
import com.auto.care.autoserviceapisrc.service.UserService;
import com.auto.care.autoserviceapisrc.util.EmailConstants;
import com.auto.care.autoserviceapisrc.util.JwtTokenTypeEnum;
import com.auto.care.autoserviceapisrc.util.UserLoginTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Value("${user.password.encryption.key}")
    private String secretKey;
    @Value("${invitation.email.forget.password.url}")
    private String forgetPasswordLink;

    private final UserRepository userRepository;
    private final UserJwtTokenCreator userJwtTokenCreator;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, UserJwtTokenCreator userJwtTokenCreator, EmailService emailService) {
        this.userRepository = userRepository;
        this.userJwtTokenCreator = userJwtTokenCreator;
        this.emailService = emailService;
    }

    @Override
    public UserLoginResponseDto userGeneralLogin(UserLoginRequestDto userLoginRequestDto) throws AutoServiceException {
        logger.debug("userGeneralLogin method started. Login requested user_name : {}", userLoginRequestDto.getUserName());

        String providedEncryptedPassword = userLoginRequestDto.getPassword();
        String userNameProvided = userLoginRequestDto.getUserName();
        User user = userRepository.findByUserName(userNameProvided);

        if (user == null) {
            logger.error("User Authentication Failed");
            throw new AutoServiceException(HttpStatus.UNAUTHORIZED, "User Authentication Failed");
        }

        String persistPassword = user.getPassword();//this is already an encrypted one
        if (!providedEncryptedPassword.equalsIgnoreCase(persistPassword)) {
            logger.error("Password not matched for user name:{}", userLoginRequestDto.getUserName());
            throw new AutoServiceException(HttpStatus.UNAUTHORIZED, "Invalid User Credentials");
        } else {
            String token = userJwtTokenCreator.generateJwtToken(user, JwtTokenTypeEnum.AUTHORIZED_TOKEN);
            logger.debug("user successfully logged in.");
            return new UserLoginResponseDto(token);
        }
    }

    @Override
    public void userSpecialLogin(UserLoginRequestDto userLoginRequestDto, UserLoginTypeEnum userLoginType) throws AutoServiceException {
        logger.debug("userSpecialLogin method started. Login requested user_name : {}", userLoginRequestDto.getUserName());
        User user = null;
        String providedEncryptedPassword = userLoginRequestDto.getPassword();//should be an encoded one
        String providedUserName = userLoginRequestDto.getUserName();

        if (userLoginType.equals(UserLoginTypeEnum.INITIAL_LOGIN)) {

            user = userRepository.findByUserName(providedUserName);

            if (user.getPassword() != null) {
                logger.error("Password already exist for user, userId: {}, Access Denied", user.getUserId());
                throw new AutoServiceException(HttpStatus.UNAUTHORIZED, "Password already exist for user");
            }

        } else if (userLoginType.equals(UserLoginTypeEnum.FORGET_PASSWORD_LOGIN)) {
            user = userRepository.findOneByEmail(providedUserName);
        }

        if (user != null) {
            validateAndUpdatePassword(providedEncryptedPassword, user);
        } else {
            logger.error("User authentication is invalid. Please try again.");
            throw new AutoServiceException(HttpStatus.UNAUTHORIZED, "User authentication is invalid. Please try again.");
        }
    }

    @Override
    public void forgetPassword(String email) throws AutoServiceException, UnsupportedEncodingException {
        User user = userRepository.findOneByEmail(email);
        if (user != null) {
            String content;
            if (!Objects.isNull(user.getPassword())) {
                String token = userJwtTokenCreator.generateJwtToken(user, JwtTokenTypeEnum.INVITATION_TOKEN);
                String resetLink = forgetPasswordLink.concat(encodeValue(token));
                content = String.format(EmailConstants.INVITATION_EMAIL_CONTENT, resetLink);
            } else {
                content = EmailConstants.INVITATION_EMAIL_CONTACT_ADMIN_CONTENT;
            }
            emailService.sendEmail(email, content, "Forgot password");
        } else {
            logger.error("User with email:{} not found.", email);
            throw new AutoServiceException(HttpStatus.BAD_REQUEST, String.format("User with email:%s not found.", email));
        }
    }

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, String.valueOf(StandardCharsets.UTF_8));
    }

    private void validateAndUpdatePassword(String providedEncryptedPassword, User user) throws AutoServiceException {
        Optional<User> optionalUser = userRepository.findById(user.getUserId());
        User userToBeUpdated = null;
        if (optionalUser.isPresent()) {
            userToBeUpdated = optionalUser.get();
            userToBeUpdated.setPassword(providedEncryptedPassword);
            userRepository.save(userToBeUpdated);
        } else {
            logger.error("Password update failed.User not found for Id:{}", user.getUserId());
            throw new AutoServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Password update failed. User not found");
        }
    }
}
