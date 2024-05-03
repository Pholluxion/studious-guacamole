package com.gopark.core.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EntityScan("com.gopark.core.domain")
@EnableJpaRepositories("com.gopark.core.repos")
@EnableTransactionManagement
class DomainConfig
