package com.auto.care.autoserviceapisrc.controller;

import com.auto.care.autoserviceapisrc.beans.UserLoginRequestDto;
import com.auto.care.autoserviceapisrc.beans.UserLoginResponseDto;
import com.auto.care.autoserviceapisrc.exception.AutoServiceException;
import com.auto.care.autoserviceapisrc.service.UserService;
import com.auto.care.autoserviceapisrc.util.UserLoginTypeEnum;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * This endpoint get called
     * 1. when user makes the general login with username and password
     *
     * 2. when user add a new password after clicking on password-reset link which gets via a mail.
     * @param userLoginType
     * @param userLoginRequestDto
     * @return
     * @throws AutoServiceException
     */
    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseDto> userLogin(
            @RequestParam
            UserLoginTypeEnum userLoginType,
            @Valid
            @RequestBody
            UserLoginRequestDto userLoginRequestDto) throws AutoServiceException {

        logger.info("Request received to authenticate, username : {} ", userLoginRequestDto.getUserName());

        switch(userLoginType){
            case GENERAL_LOGIN -> {
                UserLoginResponseDto userLoginResponseDto = userService.userGeneralLogin(userLoginRequestDto, false);
                logger.debug("User authenticated successfully for username : {}", userLoginRequestDto.getUserName());
                return new ResponseEntity<>(userLoginResponseDto, HttpStatus.OK);
            }

            case TRIGGER_OTP -> {
                userService.userGeneralLogin(userLoginRequestDto, true);
                return new ResponseEntity<>(HttpStatus.OK);
            }

            case FORGET_PASSWORD_LOGIN -> {// This case is called when user added a new password after clicking on password-reset link
                logger.debug("Login to set a new password for forget password with userName:{}", userLoginRequestDto.getUserName());
                userService.userSpecialLogin(userLoginRequestDto, UserLoginTypeEnum.FORGET_PASSWORD_LOGIN);
                logger.debug("Password updated successfully for username : {}", userLoginRequestDto.getUserName());
                return new ResponseEntity<>(HttpStatus.OK);
            }

            default -> {
                logger.error("Provided login type is not valid:{}", userLoginType);
                throw new AutoServiceException(HttpStatus.BAD_REQUEST, "Invalid Login Type");
            }

        }
    }

    /**
     * This end point get called when user clicks forget password link -> enter the  email given to system ->
     * if user entered one similar to system saved email ->
     * notifying user saying that password reset link send to particular email
     *
     * @param email
     * @return
     * @throws AutoServiceException
     * @throws UnsupportedEncodingException
     */
    @PutMapping(path = "/forget-password")
    public ResponseEntity<String> forgetPassword(
            @RequestParam
            String email) throws AutoServiceException, UnsupportedEncodingException {
        logger.info("Request received to send a mail to {} with invitation link. user is going to reset the password", email);
        userService.forgetPassword(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
