package com.takalace;

import java.util.*;

public class BingoCard {
    private final int[][] grid;
    private final boolean[][] marked;
    private final String id;

    public BingoCard(String id) {
        this.id = id;
        this.grid = new int[5][5];
        this.marked = new boolean[5][5];
        generateCard();
    }

    /**
     * Genera un cartón de Bingo siguiendo las reglas.
     */
    private void generateCard() {
        Random random = new Random();
        int[][] ranges = {
            {1, 15},   // B
            {16, 30},  // I
            {31, 45},  // N
            {46, 60},  // G
            {61, 75}   // O
        };

        for (int col = 0; col < 5; col++) {
            Set<Integer> columnNumbers = new HashSet<>();
            while (columnNumbers.size() < 5) {
                int number = random.nextInt(ranges[col][1] - ranges[col][0] + 1) + ranges[col][0];
                columnNumbers.add(number);
            }
            Iterator<Integer> iterator = columnNumbers.iterator();
            for (int row = 0; row < 5; row++) {
                grid[row][col] = iterator.next();
            }
        }

        // Espacio libre en el centro
        grid[2][2] = 0;
        marked[2][2] = true;
    }

    /**
     * Marca un número en el cartón si está presente.
     * @param number Número a marcar.
     */
    public void markNumber(int number) {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (grid[row][col] == number) {
                    marked[row][col] = true;
                }
            }
        }
    }

    public boolean markNumberAndCheck(int number) {
        markNumber(number);
        return checkBingo();
    }

    public int[][] getGrid() {
        return grid;
    }

    public String getId() {
        return id;
    }

    /**
     * Verifica si el cartón tiene Bingo.
     * @return True si hay Bingo, False de lo contrario.
     */
    public boolean checkBingo() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (!marked[row][col]) {
                    return false; // Si hay una casilla sin marcar, no hay Bingo
                }
            }
        }
        return true; // Si todas las casillas están marcadas, hay Bingo
    }

    /*
    // Métodos auxiliares
    private boolean isComplete(boolean[] line) {
        for (boolean mark : line) {
            if (!mark) return false;
        }
        return true;
    }

    private boolean[] getColumn(boolean[][] matrix, int col) {
        boolean[] column = new boolean[5];
        for (int i = 0; i < 5; i++) {
            column[i] = matrix[i][col];
        }
        return column;
    }

    private boolean[] getDiagonal(boolean[][] matrix, boolean main) {
        boolean[] diagonal = new boolean[5];
        for (int i = 0; i < 5; i++) {
            diagonal[i] = main ? matrix[i][i] : matrix[i][4 - i];
        }
        return diagonal;
    }

    */

    /**
     * Devuelve una representación del cartón en texto.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("ID: " + id + "\n");
        for (int[] row : grid) {
            for (int number : row) {
                sb.append(number == 0 ? "  " : String.format("%2d", number)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
