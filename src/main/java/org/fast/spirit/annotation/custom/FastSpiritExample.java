package org.fast.spirit.annotation.custom;

import org.fast.spirit.annotation.secondary.FastSpiritNote;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FastSpiritExample {
  String value() default "";
  String description() default "";
  FastSpiritNote[] notes() default {};
}
