package com.freebz.tayopang.model;

import android.graphics.Point;
import android.util.Log;

public class Board {

	private static int MAX_ITEM_TYPE = 4;
	
	private int[][] board = {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}};
	private int[][] marker = {{0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}};
	
	public void initBoard() {
		
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = (int) (Math.random() * MAX_ITEM_TYPE);
				
				if(j > 0 && board[i][j] == board[i][j-1]) {
					j--;
					continue;
				}
				
				if(i > 0 && board[i][j] == board[i-1][j]) {
					j--;
					continue;
				}
			}
		}
	}
	
	public int[][] getBoard() {
		return board;
	}
	
	public void checkSuccessBlock() {
		resetMarker();
		makingSuccessBlock();
		updateBlockState();
	}
	
	private void resetMarker() {
		for (int i = 0; i < marker.length; i++) {
			for (int j = 0; j < marker[i].length; j++) {
				marker[i][j] = 0;
			}
		}
	}
	
	private void makingSuccessBlock() {
		
		for (int i = 0; i < board.length; i++) {
			
			int rowChkValue = board[i][0];
			int colChkValue = board[0][i];
			
			for (int j = 1; j < board[i].length; j++) {
				if (rowChkValue == board[i][j]) {
					marker[i][j] = 1;
					marker[i][j-1] = 1;
				} else {
					rowChkValue = board[i][j];
				}
				
				if (colChkValue == board[j][i]) {
					marker[j][i] = 1;
					marker[j-1][i] = 1;
				} else {
					colChkValue = board[j][i];
				}
			}
		}
	}
	
	private void updateBlockState() {
		for (int i = 0; i < marker.length; i++) {
			for (int j = 0; j < marker[i].length; j++) {
				if (marker[i][j] == 1) {
					board[i][j] = -1;
				}
			}
		}
	}
	
	private void swapBlock(Point p1, Point p2) {
		if (p1.x < 0 && p1.x > board[0].length) {
			return;
		}
		
		if (p2.x < 0 && p2.x > board[0].length) {
			return;
		}
		
		if (p1.y < 0 && p1.y > board.length) {
			return;
		}
		
		if (p2.y < 0 && p2.y > board.length) {
			return;
		}
		
		int temp = board[p1.y][p1.x];
		board[p1.y][p1.x] = board[p2.y][p2.x];
		board[p2.y][p2.x] = temp; 
	}
}
