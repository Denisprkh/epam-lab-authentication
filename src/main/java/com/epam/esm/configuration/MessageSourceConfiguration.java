package com.epam.esm.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfiguration {

    private static final String ENCODING = "UTF-8";
    private static final String BASENAME = "messages/messages";

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(BASENAME);
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding(ENCODING);
        return source;
    }

}
