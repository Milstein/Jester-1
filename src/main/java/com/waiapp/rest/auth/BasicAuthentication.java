package com.waiapp.rest.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a resource class or method should use basic authentication. This annotation will only work if you
 * have registered the {@link com.waiapp.rest.auth.BasicAuthenticationFeature}. The annotation placed on a
 * method will take precedence over a class level annotation. In this way you can mark an entire class as
 * requiring authentication but more one or more features as unauthenticated.
 *
 * @author Robert DiFalco (robert.difalco@waiapp.com)
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BasicAuthentication {
    boolean value() default true;
}
