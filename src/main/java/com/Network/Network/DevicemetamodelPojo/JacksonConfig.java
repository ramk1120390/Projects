package com.Network.Network.DevicemetamodelPojo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Import JavaTimeModule
import org.hibernate.proxy.HibernateProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register the JavaTimeModule for Java 8 date/time support
        objectMapper.registerModule(new JavaTimeModule());

        // Create a SimpleModule to register custom serializers
        SimpleModule module = new SimpleModule();
        // Register HibernateProxySerializer for Hibernate proxy objects
        module.addSerializer(HibernateProxy.class, new HibernateProxySerializer());

        // Register the SimpleModule with the ObjectMapper
        objectMapper.registerModule(module);

        return objectMapper;
    }
}
