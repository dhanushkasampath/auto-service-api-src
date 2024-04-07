package com.auto.care.autoserviceapisrc.service;

import com.auto.care.autoserviceapisrc.exception.AutoServiceException;

public interface EmailService {
    void sendEmail(String toAddress, String content, String topic) throws AutoServiceException;
}
