package com.minesweeper.api.demo.Models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.minesweeper.api.demo.Validation.Annotation.MineCounts;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Component
@MineCounts
public class MineSweeper
{
	@Min(2)
	@Max(30)
	private int width = 2;

	@Min(2)
	@Max(30)
	private int height = 2;

	private int mines_count = 0;

	private List<List<Character>> field;
	private boolean completed;
	private UUID game_id;

	@JsonIgnore
	private Set<BombPosition> MinePosition;

	public MineSweeper(int width, int height, int mines_count)
	{
		this.width = width;
		this.height = height;
		this.mines_count = mines_count;

		field = new ArrayList<>();

		for(int i = 0; i < height; i++)
		{
			List<Character> row = new ArrayList<>();

			for(int j = 0; j < width; j++) row.add(' ');

			field.add(row);
		}

		setMine();

		game_id = UUID.randomUUID();
	}

	public void click(int col, int row)
	{
		if(checkMine(col, row)) complete('X');
		else
		{
			open(col, row);
			if(getCountCloseCell() == MinePosition.size()) complete('M');
		}
	}

	private void open(int col, int row)
	{
		if(col >= width || row >= height || col < 0 || row < 0) return;
		if(field.get(row).get(col) != ' ') return;

		int MineCount = getCountMineNear(col, row);

		field.get(row).set(col, (char)(MineCount+'0'));

		if(MineCount == 0)
		{
			for(int NewCol = col-1; NewCol <= col+1; NewCol++)
			{
				for(int NewRow = row-1; NewRow <= row+1; NewRow++)
				{
					if(checkMine(NewCol, NewRow) || (NewRow == row && NewCol == col)) continue;
					else open(NewCol, NewRow);
				}
			}
		}
	}

	private boolean checkMine(int col, int row)
	{
		return (MinePosition.stream().filter(p -> p.getCol() == col && p.getRow() == row).count() > 0);
	}

	private int getCountMineNear(int col, int row)
	{
		int count = 0;

		for(BombPosition p : MinePosition)
		{
			if(p.getCol() >= col-1 && p.getCol() <= col+1 && p.getRow() >= row-1 && p.getRow() <= row+1) count++;
		}

		return count;
	}

	private int getCountCloseCell()
	{
		int Count = 0;

		for(List<Character> row : field)
		{
			Count += row.stream().filter(c -> c.equals(' ')).count();
		}

		return Count;
	}

	private void setMine()
	{
		MinePosition = new HashSet<>();
		Random random = new Random();

		while(MinePosition.size() < mines_count) MinePosition.add(new BombPosition(random.nextInt(0, width), random.nextInt(0, height)));
	}

	private void complete(char MineMark)
	{
		for(BombPosition p : MinePosition)
		{
			field.get(p.getRow()).set(p.getCol(), MineMark);
		}

		setCompleted(true);
	}
}
