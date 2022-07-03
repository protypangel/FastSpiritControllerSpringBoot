package org.fast.spirit.annotation.custom;

import org.fast.spirit.annotation.secondary.FastSpiritRoleAccess;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@Component
public @interface FastSpiritCustomAuthorization {
  FastSpiritRoleAccess roles() default @FastSpiritRoleAccess;
  FastSpiritRoleAccess privileges() default @FastSpiritRoleAccess;
}
