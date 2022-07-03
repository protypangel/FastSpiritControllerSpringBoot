package org.fast.spirit.annotation.principal;

import org.fast.spirit.annotation.secondary.FastSpiritNote;
import org.fast.spirit.annotation.secondary.FastSpiritResponse;
import org.fast.spirit.annotation.secondary.FastSpiritRoleAccess;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface FastSpiritMapping {
  FastSpiritNote[] notes() default {};
  String value() default "";
  String[] tags() default {};
  // TODO
  FastSpiritResponse[] responses() default {};
  FastSpiritRoleAccess roles() default @FastSpiritRoleAccess;
  FastSpiritRoleAccess privileges() default @FastSpiritRoleAccess;
  boolean display() default true;
  String description() default "";
}
