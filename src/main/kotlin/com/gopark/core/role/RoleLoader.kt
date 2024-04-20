package com.gopark.core.role

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component


@Component
class RoleLoader(
    private val roleRepository: RoleRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        if (roleRepository.count() != 0L) {
            return
        }
        log.info("initializing roles")
        val suRole = Role()
        suRole.name = "SU"
        roleRepository.save(suRole)
        val adminRole = Role()
        adminRole.name = "ADMIN"
        roleRepository.save(adminRole)
    }


    companion object {

        val log: Logger = LoggerFactory.getLogger(RoleLoader::class.java)

    }

}
