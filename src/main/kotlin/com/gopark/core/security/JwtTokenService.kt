package com.gopark.core.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.JWTVerifier
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Duration
import java.time.Instant
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service


@Service
class JwtTokenService(
    @Value("classpath:certs/public.pem")
    publicKey: RSAPublicKey,
    @Value("classpath:certs/private.pem")
    privateKey: RSAPrivateKey
) {

    private final val rsa256: Algorithm = Algorithm.RSA256(publicKey, privateKey)

    private final val verifier: JWTVerifier = JWT.require(this.rsa256).build()

    fun generateToken(userDetails: UserDetails): String {
        val now = Instant.now()
        return JWT.create()
                .withSubject(userDetails.username)
                // only for client information
                .withArrayClaim("roles", userDetails.authorities
                        .map { grantedAuth -> grantedAuth.authority }
                        .toTypedArray())
                .withIssuer("app")
                .withIssuedAt(now)
                .withExpiresAt(now.plusMillis(JWT_TOKEN_VALIDITY.toMillis()))
                .sign(this.rsa256)
    }

    fun validateTokenAndGetUsername(token: String): String? = try {
        verifier.verify(token).subject
    } catch (verificationEx: JWTVerificationException) {
        log.warn("token invalid: {}", verificationEx.message)
        null
    }


    companion object {

        val log: Logger = LoggerFactory.getLogger(JwtTokenService::class.java)

        val JWT_TOKEN_VALIDITY: Duration = Duration.ofMinutes(20)

    }

}
