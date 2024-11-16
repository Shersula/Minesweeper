package com.minesweeper.api.demo.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minesweeper.api.demo.DTO.Turn;
import com.minesweeper.api.demo.Errors.Errors;
import com.minesweeper.api.demo.Models.MineSweeper;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestController
@RequestMapping(path = "/api", produces="application/json")
public class MainController
{
	List<MineSweeper> GamesList = new ArrayList<>();

	@CrossOrigin(origins = {"https://minesweeper-test.studiotg.ru"})
	@PostMapping("/new")
	public MineSweeper createNewGame(@RequestBody @Validated MineSweeper GameInfo)
	{	
		MineSweeper game = new MineSweeper(GameInfo.getWidth(), GameInfo.getHeight(), GameInfo.getMines_count());

		GamesList.add(game);

		return game;
	}

	@CrossOrigin(origins = {"https://minesweeper-test.studiotg.ru"})
	@PostMapping("/turn")
	public MineSweeper turnGame(@RequestBody Turn turn) {

		MineSweeper game = GamesList.stream().filter(g -> g.getGame_id().equals(turn.getGame_id())).findFirst().orElse(null);

		if(game != null) game.click(turn.getCol(), turn.getRow());

		return game;
	}

	@ExceptionHandler
	public ResponseEntity<Errors> ExHandler(MethodArgumentNotValidException e)
	{
		String errMsg = "";
		try
		{
			FieldError fieldError =  e.getFieldErrors().get(0);
			errMsg = fieldError.getField() + " " + fieldError.getDefaultMessage();
		}
		catch(IndexOutOfBoundsException ex)
		{
			errMsg =  e.getAllErrors().get(0).getDefaultMessage();
		}
		ResponseEntity<Errors> response = new ResponseEntity<>(new Errors(errMsg), HttpStatus.BAD_REQUEST);
		
		return response;
	}
}
