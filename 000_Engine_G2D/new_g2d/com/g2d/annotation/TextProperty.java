package com.g2d.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TextProperty
{

}
