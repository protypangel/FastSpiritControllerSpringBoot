package org.fast.spirit.annotation.principal;

import org.fast.spirit.annotation.custom.FastSpiritCustomAuthorization;
import org.fast.spirit.configuration.FastSpiritConfigurationController;
import org.fast.spirit.configuration.FastSpiritFilter;
import org.fast.spirit.configuration.SecurityConfiguration;
import org.fast.spirit.json.FastSpiritJsonBuilder;
import org.fast.spirit.util.JwtUtil;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import({FastSpiritFilter.class, FastSpiritJsonBuilder.class, FastSpiritConfigurationController.class, JwtUtil.class, SecurityConfiguration.class})
public @interface FastSpiritEnable {
}
