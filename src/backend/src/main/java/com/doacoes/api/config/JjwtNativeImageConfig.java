package com.doacoes.api.config;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@RegisterReflectionForBinding({
    io.jsonwebtoken.impl.DefaultJwtParser.class,
    io.jsonwebtoken.impl.DefaultClaimsBuilder.class,
    io.jsonwebtoken.impl.security.StandardSecureDigestAlgorithms.class,
    io.jsonwebtoken.impl.security.StandardKeyOperations.class,
    io.jsonwebtoken.security.SignatureAlgorithm.class,
    io.jsonwebtoken.security.SignatureException.class,
    io.jsonwebtoken.impl.security.StandardEncryptionAlgorithms.class,
    io.jsonwebtoken.impl.security.StandardKeyAlgorithms.class,
    io.jsonwebtoken.impl.io.StandardCompressionAlgorithms.class,
    io.jsonwebtoken.impl.security.KeysBridge.class
})
public class JjwtNativeImageConfig {

}
