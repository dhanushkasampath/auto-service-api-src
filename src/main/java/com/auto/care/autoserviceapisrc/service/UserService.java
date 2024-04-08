package com.auto.care.autoserviceapisrc.service;

import com.auto.care.autoserviceapisrc.beans.UserLoginRequestDto;
import com.auto.care.autoserviceapisrc.beans.UserLoginResponseDto;
import com.auto.care.autoserviceapisrc.exception.AutoServiceException;
import com.auto.care.autoserviceapisrc.util.UserLoginTypeEnum;

import java.io.UnsupportedEncodingException;

public interface UserService extends GenericService {

    UserLoginResponseDto userGeneralLogin(UserLoginRequestDto userLoginRequestDto) throws AutoServiceException;

    void userSpecialLogin(UserLoginRequestDto userLoginRequestDto, UserLoginTypeEnum userLoginTypeEnum) throws AutoServiceException;

    void forgetPassword(String email) throws AutoServiceException, UnsupportedEncodingException;

}
