package com.cell.net.io;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})//只对应属性
@Retention(RetentionPolicy.RUNTIME)//在运行时保留
public @interface Comment {
	String[] value();
}
