package test.configuration;

import org.fast.spirit.annotation.custom.FastSpiritCustomAuthorization;
import org.fast.spirit.annotation.secondary.FastSpiritRoleAccess;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@FastSpiritCustomAuthorization(privileges = @FastSpiritRoleAccess("ACCESS"))
public @interface ADMIN {
}
