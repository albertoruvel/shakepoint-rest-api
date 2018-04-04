package com.shakepoint.web.api.core.service.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jose.rubalcaba
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface AllowedUsers {

    /**
     * Allowed roles to be allowed on the application resources
     *
     * @return
     */
    SecurityRole[] securityRoles() default SecurityRole.ALL;

}
