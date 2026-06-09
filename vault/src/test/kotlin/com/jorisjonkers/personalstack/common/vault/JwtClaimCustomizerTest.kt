package com.jorisjonkers.personalstack.common.vault

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2TokenType
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext

class JwtClaimCustomizerTest {
    @Test
    fun `customizer receives generic context and writes claims`() {
        val customizer =
            CompositeJwtClaimCustomizer(
                listOf(
                    JwtClaimCustomizer {
                        it.claims["tenant"] = "test"
                        it.claims["client"] = it.clientId
                    },
                ),
            )
        val registeredClient =
            RegisteredClient
                .withId("id")
                .clientId("client-a")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("https://example.test/callback")
                .scope("openid")
                .build()
        val context =
            JwtEncodingContext
                .with(JwsHeader.with(SignatureAlgorithm.RS256), JwtClaimsSet.builder())
                .registeredClient(registeredClient)
                .principal(TestingAuthenticationToken("alice", "n/a"))
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizedScopes(setOf("openid"))
                .build()

        customizer.customize(context)
        val claims = context.claims.build().claims

        assertThat(claims["tenant"]).isEqualTo("test")
        assertThat(claims["client"]).isEqualTo("client-a")
    }
}
