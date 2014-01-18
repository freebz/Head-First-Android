package com.freebz.tayopang.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BoardTest {

	@Test
	public void testInitBoard() {
		Board board = new Board();
		board.initBoard();
		
		for (int[] row : board.getBoard()) {
			for (int cell : row) {
				//assertThat(cell, is(0));
			}
		}
	}
	
	@Test
	public void testCheckSuccessBlock() {
		Board board = new Board();
		board.initBoard();
		int[][] brd = board.getBoard();
		
		brd[0][0] = 1;
		brd[0][1] = 1;
		brd[0][2] = 1;
		brd[1][0] = 1;
		brd[2][0] = 1;
		
//		int[][] marker = board.checkSuccessBlock();
//		
//		int total = 0;
//		
//		for (int[] row : marker) {
//			for (int cell : row) {
//				total += cell;
//			}
//		}
		
//		assertThat(total, is(0));
	}
}
