package com.auto.care.autoserviceapisrc.controller;

import com.auto.care.autoserviceapisrc.beans.UserLoginRequestDto;
import com.auto.care.autoserviceapisrc.beans.UserLoginResponseDto;
import com.auto.care.autoserviceapisrc.exception.AutoServiceException;
import com.auto.care.autoserviceapisrc.service.UserService;
import com.auto.care.autoserviceapisrc.util.UserLoginTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseDto> userLogin(
            @RequestParam
            UserLoginTypeEnum userLoginType,
            @Valid
            @RequestBody
            UserLoginRequestDto userLoginRequestDto, HttpServletRequest request) throws AutoServiceException {

        logger.info("Request received to authenticate, username : {} ", userLoginRequestDto.getUserName());

        if (userLoginType.toString().equals(UserLoginTypeEnum.GENERAL_LOGIN.toString())) {

            UserLoginResponseDto userLoginResponseDto = userService.userGeneralLogin(userLoginRequestDto);
            logger.debug("User authenticated successfully for username : {}", userLoginRequestDto.getUserName());
            return new ResponseEntity<>(userLoginResponseDto, HttpStatus.OK);

        } else if (userLoginType.toString().equals(UserLoginTypeEnum.INITIAL_LOGIN.toString())) {

            logger.debug("Login for the first time with user name " + userLoginRequestDto.getUserName());
            userService.userSpecialLogin(userLoginRequestDto, UserLoginTypeEnum.INITIAL_LOGIN);
            logger.debug("Initial login successfully completed for username : {}", userLoginRequestDto.getUserName());
            return new ResponseEntity<>(HttpStatus.OK);

        } else if (userLoginType.toString().equals(UserLoginTypeEnum.FORGET_PASSWORD_LOGIN.toString())) {

            logger.debug("Login to set a new password for forget password with userName:{}", userLoginRequestDto.getUserName());
            userService.userSpecialLogin(userLoginRequestDto, UserLoginTypeEnum.FORGET_PASSWORD_LOGIN);
            logger.debug("Password updated successfully for username : {}", userLoginRequestDto.getUserName());
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            logger.error("Provided login type is not valid:{}", userLoginType);
            throw new AutoServiceException(HttpStatus.BAD_REQUEST, "Invalid Login Type");
        }
    }

}
