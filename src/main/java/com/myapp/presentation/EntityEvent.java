package com.myapp.presentation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/*
 * Qualifier for CDI events used to decouple StudentEditor from the StudentUI
 */
@Qualifier
@Retention(RUNTIME)
@Target({FIELD,PARAMETER})
public abstract @interface EntityEvent {
    
    Type value();

    public enum Type {
        SAVE, REFRESH
    }
}

