package com.example.manuel.sudo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.example.manuel.sudo.C;
import com.example.manuel.sudo.LogUtil;

public class SudokuGenerator {
    private SudokuField sudoku;
    private int blockSize;
    private Level level;


    public int getFieldSize() {
        return sudoku.getFieldSize();
    }


    public int getBlockSize() {
        return blockSize;
    }

    public enum Level {
        HARD, EASY, MID
    }


    public enum State {
        ROW_ERROR, COL_ERROR, BLOCK_ERROR, SUCCESS, ROW_NOT_FILLED, COL_NOT_FILLED, BLOCK_NOT_FILLED
    }

    public void setBlockSize(int size) {
        sudoku = new SudokuField(size);
        this.blockSize = size;
        sudoku.generate();
    }


    public SudokuField getSudoku() {
        return sudoku;
    }


    public void generateProblems(Level level) {
        this.level = level;
        if (level == Level.HARD) {
            for (int i = 0; i < sudoku.numberOfCells() / 16 * 15; i++) {
                int x = getRandomIndex();
                int y = getRandomIndex();
                if (sudoku.isFilled(x, y)) {
                    sudoku.hide(x, y);
                }
            }
        } else if (level == Level.MID) {
            for (int i = 0; i < sudoku.numberOfCells() / 16 * 7; i++) {
                int x = getRandomIndex();
                int y = getRandomIndex();
                if (sudoku.isFilled(x, y)) {
                    sudoku.hide(x, y);
                }
            }
        } else if (level == Level.EASY) {
            for (int i = 0; i < sudoku.numberOfCells() / 3; i++) {
                int x = getRandomIndex();
                int y = getRandomIndex();
                if (sudoku.isFilled(x, y)) {
                    sudoku.hide(x, y);
                }
            }
        }
    }

    private int getRandomIndex() {
        return (int) ((Math.random() * 10) % sudoku.getFieldSize() + 1);
    }


    public State checkRow(int row) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < sudoku.getFieldSize(); i++) {
            if (C.DEBUG) {
                LogUtil.log(sudoku.getField()[row][i].toString() + "row");
            }
            if (!sudoku.getField()[row][i].isFilled()) {
                return State.ROW_NOT_FILLED;
            }
            set.add(sudoku.getField()[row][i].getValue());
        }
        if (set.size() == blockSize * blockSize) {
            return State.SUCCESS;
        } else {
            return State.ROW_ERROR;
        }
    }


    public State checkColumn(int col) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < sudoku.getFieldSize(); i++) {
            if (C.DEBUG) {
                LogUtil.log(sudoku.getField()[i][col].toString() + "col");
            }
            if (!sudoku.getField()[i][col].isFilled()) {
                return State.COL_NOT_FILLED;
            }
            set.add(sudoku.getField()[i][col].getValue());
        }
        if (set.size() == blockSize * blockSize) {
            return State.SUCCESS;
        } else {
            return State.COL_ERROR;
        }
    }


    public State checkBlock(int row, int column) {
        HashSet<Integer> set = new HashSet<>();
        int left = column / blockSize * 3;
        int bottom = row / blockSize * 3;
        for (int i = bottom; i < bottom + 3; i++) {
            for (int j = left; j < left + 3; j++) {
                if (!sudoku.getField()[i][j].isFilled()) {
                    return State.BLOCK_NOT_FILLED;
                }
                set.add(sudoku.getField()[i][j].getValue());
            }
        }
        if (set.size() == blockSize * blockSize) {
            return State.SUCCESS;
        } else {
            return State.BLOCK_ERROR;
        }
    }

    @Override
    public String toString() {
        return sudoku.toString();
    }



    public List<SudokuCell> asList() {
        List<SudokuCell> list = new ArrayList<>();
        for (int i = 1; i <= sudoku.getFieldSize(); i++) {
            for (int j = 1; j <= sudoku.getFieldSize(); j++) {
                list.add(sudoku.get(i, j));
            }
        }
        return list;
    }
}
