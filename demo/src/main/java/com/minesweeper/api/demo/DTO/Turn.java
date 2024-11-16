package com.minesweeper.api.demo.DTO;

import java.util.UUID;

import lombok.Data;

@Data
public class Turn {
	private final int col;
	private final int row;
	private final UUID game_id;
}
