package com.github.mikybars.challenge;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(considerNestedRepositories = true)
public class JpaConfiguration {

}
