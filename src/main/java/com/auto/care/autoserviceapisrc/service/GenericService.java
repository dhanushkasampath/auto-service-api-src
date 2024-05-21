package com.auto.care.autoserviceapisrc.service;

import com.auto.care.autoserviceapisrc.exception.AutoServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import java.lang.reflect.Type;

public interface GenericService {
    Logger logger = LoggerFactory.getLogger(GenericService.class);
    ObjectMapper objectMapper = new ObjectMapper();

    ModelMapper modelMapper = new ModelMapper();

    default < T > T map(Object sourceEntity, Class < T > destinationType) throws AutoServiceException {
        try {
            return modelMapper.map(sourceEntity, destinationType);
        }
        catch (IllegalArgumentException | ConfigurationException | MappingException e ) {
            logger.error("Source:{}, to destination by class type: {}, mapping exception: ", sourceEntity, destinationType, e);
            throw new AutoServiceException("601", "Mapping exception occurred: " + e.getMessage());
        }//HttpStatus.INTERNAL_SERVER_ERROR
    }

    default < T > T map(Object sourceEntity, Type destinationType) throws AutoServiceException {
        try {
            return modelMapper.map(sourceEntity, destinationType);
        }
        catch ( IllegalArgumentException | ConfigurationException | MappingException e ) {
            logger.error("Source:{}, to destination by type:{}, mapping exception : ", sourceEntity, destinationType, e);
            throw new AutoServiceException("601", "Mapping exception occurred: " + e.getMessage());
        }
    }
}
