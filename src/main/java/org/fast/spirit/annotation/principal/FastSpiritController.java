package org.fast.spirit.annotation.principal;

import org.fast.spirit.annotation.secondary.FastSpiritRoleAccess;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FastSpiritController {
  String value() default "";
  String description() default "";
  FastSpiritRoleAccess roles() default @FastSpiritRoleAccess;
  FastSpiritRoleAccess privileges() default @FastSpiritRoleAccess;
  boolean display() default true;
}
