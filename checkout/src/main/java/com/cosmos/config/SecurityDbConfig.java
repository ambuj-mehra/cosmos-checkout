package com.cosmos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Security db config.
 *
 * @author ambujmehra
 */
@Configuration
@PropertySource("classpath:application.properties")
@ImportResource("classpath:service-auth-repository.xml")
public class SecurityDbConfig {
}
