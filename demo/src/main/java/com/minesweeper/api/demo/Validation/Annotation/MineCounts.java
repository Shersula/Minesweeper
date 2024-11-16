package com.minesweeper.api.demo.Validation.Annotation;

import java.lang.annotation.Target;

import com.minesweeper.api.demo.Validation.MineCountsValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target({TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MineCountsValidator.class)
@Documented
public @interface MineCounts
{
	String message() default "Должна быть хотя бы одна свободная ячейка";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
