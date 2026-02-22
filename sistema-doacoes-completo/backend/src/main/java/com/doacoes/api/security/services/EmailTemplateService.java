package com.doacoes.api.security.services;

import java.io.IOException;
import java.util.Map;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;

public class EmailTemplateService {

    private final Handlebars handlebars = new Handlebars(new ClassPathTemplateLoader("/handlebars", ".html"));

    public String renderEmail(String templateName, Map<String, Object> variables) throws IOException {
        Template template = handlebars.compile(templateName);
        return template.apply(variables);
    }
}
