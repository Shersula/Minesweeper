package com.minesweeper.api.demo.Validation;

import com.minesweeper.api.demo.Models.MineSweeper;
import com.minesweeper.api.demo.Validation.Annotation.MineCounts;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MineCountsValidator implements ConstraintValidator<MineCounts, MineSweeper> {

	@Override
	public boolean isValid(MineSweeper value, ConstraintValidatorContext context)
	{
		return (value.getWidth()*value.getHeight() > value.getMines_count());
	}

}
