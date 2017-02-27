package com.example.manuel.sudo;



public interface OnSudokuChangedListener {
    /**
     * Quando il sudoku è completato e corretto
     */
    void onSuccess();

    void onRowError(int row);

    void onColError(int col);

    void onBlockError(int row, int col);
}
