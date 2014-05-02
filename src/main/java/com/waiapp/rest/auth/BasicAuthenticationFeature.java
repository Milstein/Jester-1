package com.waiapp.rest.auth;

import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import java.lang.reflect.AnnotatedElement;

/**
 * Configures a Basic Authentication feature for classes or methods that are marked with the
 * {@link com.waiapp.rest.auth.BasicAuthentication} annotation.
 * <p/>
 * @author Robert DiFalco (robert.difalco@waiapp.com)
 * Copyright (c) 2014
 */
public class BasicAuthenticationFeature implements DynamicFeature {

    private final ContainerRequestFilter authenticator;

    public BasicAuthenticationFeature(String key, String secret) {
        this(new BasicAuthContainerRequestFilter(key, secret));
    }

    public BasicAuthenticationFeature(BasicAuthContainerRequestFilter authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (isAuthenticationRequired(resourceInfo)) {
            context.register(authenticator);
        }
    }

    private boolean isAuthenticationRequired(ResourceInfo info) {
        // On the method is the last word (if present)
        Boolean methodRequired = isAuthenticationRequired(info.getResourceMethod());
        if (methodRequired != null) {
            return methodRequired;
        }

        Boolean classRequired = isAuthenticationRequired(info.getResourceClass());
        return classRequired == null ? false : classRequired;
    }

    private Boolean isAuthenticationRequired(AnnotatedElement element) {
        BasicAuthentication authenticated = element.getAnnotation(BasicAuthentication.class);
        if (authenticated == null) {
            return null;
        }

        return authenticated.value();
    }
}
