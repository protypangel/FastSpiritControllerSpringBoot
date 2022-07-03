package org.fast.spirit.annotation.principal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface FastSpiritCancelAuthorizationController {
  boolean doc() default true;
  boolean api() default true;
}
