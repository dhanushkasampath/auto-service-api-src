package com.auto.care.autoserviceapisrc.service.impl;

import com.auto.care.autoserviceapisrc.beans.UserLoginRequestDto;
import com.auto.care.autoserviceapisrc.beans.UserLoginResponseDto;
import com.auto.care.autoserviceapisrc.config.security.UserJwtTokenCreator;
import com.auto.care.autoserviceapisrc.entity.Otp;
import com.auto.care.autoserviceapisrc.entity.User;
import com.auto.care.autoserviceapisrc.exception.AutoServiceException;
import com.auto.care.autoserviceapisrc.repository.OtpRepository;
import com.auto.care.autoserviceapisrc.repository.UserRepository;
import com.auto.care.autoserviceapisrc.service.EmailService;
import com.auto.care.autoserviceapisrc.service.UserService;
import com.auto.care.autoserviceapisrc.util.EmailConstants;
import com.auto.care.autoserviceapisrc.util.JwtTokenTypeEnum;
import com.auto.care.autoserviceapisrc.util.UserLoginTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Value("${user.password.encryption.key}")
    private String secretKey;
    @Value("${invitation.email.forget.password.url}")
    private String forgetPasswordLink;

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final UserJwtTokenCreator userJwtTokenCreator;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, OtpRepository otpRepository,
                           UserJwtTokenCreator userJwtTokenCreator, EmailService emailService) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.userJwtTokenCreator = userJwtTokenCreator;
        this.emailService = emailService;
    }

    @Override
    public UserLoginResponseDto userGeneralLogin(UserLoginRequestDto userLoginRequestDto, boolean isTriggerOtp) throws AutoServiceException {
        logger.debug("userGeneralLogin method started. Login requested user_name : {}", userLoginRequestDto.getUserName());

        String providedEncryptedPassword = userLoginRequestDto.getPassword();
        String userNameProvided = userLoginRequestDto.getUserName();
        User user = userRepository.findByUserName(userNameProvided);

        if (user == null) {
            logger.error("User Authentication Failed");
            throw new AutoServiceException("603", "User Authentication Failed");
        }//HttpStatus.UNAUTHORIZED

        String persistPassword = user.getPassword();//this is already an encrypted one
        if (!providedEncryptedPassword.equalsIgnoreCase(persistPassword)) {
            logger.error("Password not matched for user name:{}", userLoginRequestDto.getUserName());
            throw new AutoServiceException("604", "Invalid User Credentials");
        } else {//HttpStatus.UNAUTHORIZED
            if (isTriggerOtp) {
                triggerOtp(user.getEmail());
                return new UserLoginResponseDto();
            } else {
                String token = userJwtTokenCreator.generateJwtToken(user, JwtTokenTypeEnum.AUTHORIZED_TOKEN);
                logger.debug("user successfully logged in.");
                return new UserLoginResponseDto(token);
            }
        }
    }

    private void triggerOtp(String email) {
        Random random = new Random();

        // Generate a 6-digit random number
        int randomNumber = random.nextInt(900000) + 100000;


        //persits the saved otp in db
        Otp otp = new Otp();
        otp.setCode(String.valueOf(randomNumber));
        otpRepository.save(otp);
        System.out.println("Generated random number: " + randomNumber);
    }

    @Override
    public void userSpecialLogin(UserLoginRequestDto userLoginRequestDto, UserLoginTypeEnum userLoginType) throws AutoServiceException {
        logger.debug("userSpecialLogin method started. Login requested user_name : {}", userLoginRequestDto.getUserName());
        User user = null;
        String providedEncryptedPassword = userLoginRequestDto.getPassword();//should be an encoded one
        String providedUserName = userLoginRequestDto.getUserName();

        if (userLoginType.equals(UserLoginTypeEnum.TRIGGER_OTP)) {

            user = userRepository.findByUserName(providedUserName);

            if (user.getPassword() != null) {
                logger.error("Password already exist for user, userId: {}, Access Denied", user.getUserId());
                throw new AutoServiceException("605", "Password already exist for user");
            }//HttpStatus.UNAUTHORIZED

        } else if (userLoginType.equals(UserLoginTypeEnum.FORGET_PASSWORD_LOGIN)) {
            user = userRepository.findOneByEmail(providedUserName);
        }

        if (user != null) {
            validateAndUpdatePassword(providedEncryptedPassword, user);
        } else {
            logger.error("User authentication is invalid. Please try again.");
            throw new AutoServiceException("606", "User authentication is invalid. Please try again.");
        }//HttpStatus.UNAUTHORIZED
    }

    @Override
    public void forgetPassword(String email) throws AutoServiceException, UnsupportedEncodingException {
        User user = userRepository.findOneByEmail(email);
        if (user != null) {
            String content;
            if (!Objects.isNull(user.getPassword())) {
                String token = userJwtTokenCreator.generateJwtToken(user, JwtTokenTypeEnum.INVITATION_TOKEN);
                String resetLink = forgetPasswordLink.concat(encodeValue(token));
                content = String.format(EmailConstants.FORGET_PASSWORD_EMAIL_CONTENT, resetLink);
            } else {
                content = EmailConstants.INVITATION_EMAIL_CONTACT_ADMIN_CONTENT;
            }
            emailService.sendEmail(email, content, "Forgot password");
        } else {
            logger.error("User with email:{} not found.", email);
            throw new AutoServiceException("608", String.format("User with email:%s not found.", email));
        }//HttpStatus.BAD_REQUEST
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
            throw new AutoServiceException("609", "Password update failed. User not found");
        }//HttpStatus.INTERNAL_SERVER_ERROR
    }
}
