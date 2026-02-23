package com.doacoes.api.config;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(ResourceRuntimeHints.ResourcesRegistrar.class)
public class ResourceRuntimeHints {
    static class ResourcesRegistrar implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        	hints.reflection().registerType(java.util.UUID[].class);
        	
        	hints.resources()
            	.registerPattern("handlebars/*.html");
        	hints.resources()
        		.registerPattern("db/migration/*.sql");
        	
        	hints.resources().registerPattern("helpers.nashorn.js");
        }
    }
}
