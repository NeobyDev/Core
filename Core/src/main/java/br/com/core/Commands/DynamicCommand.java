package br.com.core.Commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.TYPE})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface DynamicCommand {
    public String name();

    public String description() default "";

    public String usage() default "";

    public boolean console() default false;

    public String[] aliases() default {};
}

