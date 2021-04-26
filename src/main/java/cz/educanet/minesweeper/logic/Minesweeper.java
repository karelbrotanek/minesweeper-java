package cz.educanet.minesweeper.logic;

import java.util.Random;

public class Minesweeper {

    private int rowsCount;
    private int columnsCount;
    private int field[][];// pole na typ zadani
    private boolean bomb[][];// pole na bomby

    public Minesweeper(int rows, int columns) {
        this.rowsCount = rows;
        this.columnsCount = columns;
        field = new int[columns][rows];// zadali jsme velikost pole
        bomb = new boolean[columns][rows];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                field[j][i] = 0;// schovaná políčka
                if (random.nextInt(100) % 5 == 0){// kdyz cislo bude delitelne peti tak tam bude bomba
                    bomb[j][i] = true;
                }
                else {
                    bomb[j][i] = false;
                }
            }
        }
    }

    /**
     * 0 - Hidden
     * 1 - Visible
     * 2 - Flag
     * 3 - Question mark
     *
     * @param x X
     * @param y Y
     * @return field type
     */
    public int getField(int x, int y) {
        return field[x][y];

    }

    /**
     * Toggles the field state, ie.
     * 0 -> 1,
     * 1 -> 2,
     * 2 -> 3 and
     * 3 -> 0
     *
     * @param x X
     * @param y Y
     */
    public void toggleFieldState(int x, int y) {
        if (getField(x, y) == 0){
            field[x][y] = 1;
        }
        else
        if (getField(x, y) == 1){
            field[x][y] = 2;
        }
        else
        if (getField(x, y) == 2){
            field[x][y] = 3;
        }
        else
        if (getField(x, y) == 3){
            field[x][y] = 0;
        }
        System.out.println("Toggle Reaveal");
    }

    /**
     * Reveals the field and all fields adjacent (with 0 adjacent bombs) and all fields adjacent to the adjacent fields... ect.
     *
     * @param x X
     * @param y Y
     */
    public void reveal(int x, int y) {
        //System.out.println("Reaveal");
        if (getField(x, y) != 1){
            field[x][y] = 1;
            for (int i = - 1; i < 2; i++) {
                for (int j = - 1; j < 2; j++) {
                    if (x + i >= 0 && x + i < columnsCount && y + j >= 0 && y + j < rowsCount && !(i == 0 && j == 0)){
                        if (getAdjacentBombCount(x, y) == 0){
                            reveal(x + i, y + j);
                        }
                    }
                }
            }
        }

    }
    /**
     * Returns the amount of adjacent bombs
     *
     * @param x X
     * @param y Y
     * @return number of adjacent bombs
     */
    public int getAdjacentBombCount(int x, int y) {// pocet bomb v okoli tohoto policka
        int adjacentBombCount = 0;
        for (int i = - 1; i < 2; i++) {
            for (int j = - 1; j < 2; j++) {
                if (x + i > 0 && x + i < columnsCount && y + j > 0 && y + j < rowsCount && !(i == 0 && j == 0)){
                    if (isBombOnPosition(x + i, y + j)){
                        adjacentBombCount++;
                    }
                }
            }
        }
        return adjacentBombCount;
    }

    /**
     * Checks if there is a bomb on the current position
     *
     * @param x X
     * @param y Y
     * @return true if bomb on position
     */
    public boolean isBombOnPosition(int x, int y) {
        return bomb[x][y];
    }

    /**
     * Returns the amount of bombs on the field
     *
     * @return bomb count
     */
    public int getBombCount() {// zjisti kolik je bomb celkem
        int bombcount = 0;
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                if (isBombOnPosition(j, i)){
                    bombcount++;
                }
            }
        }
        return bombcount;
    }

    public int getFlagCount(){// pocita vlajky
        int flagcount = 0;
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                if (getField(j, i) == 2){
                    flagcount++;
                }
            }
        }
        return flagcount;
    }

    /**
     * total bombs - number of flags
     *
     * @return remaining bomb count
     */
    public int getRemainingBombCount() {
        return getBombCount() - getFlagCount();// kolik jich zbívá
    }

    /**
     * returns true if every flag is on a bomb, else false
     *
     * @return if player won
     */
    public boolean didWin() {
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                if (isBombOnPosition(j, i) && getField(j, i) != 2){// pokud je bomba ale neni vlajka nevyhral
                    return false;
                }
                if (! isBombOnPosition(j, i) && getField(j, i) == 2){// pokud neni bomba ale je tam vlajka
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * returns true if player revealed a bomb, else false
     *
     * @return if player lost
     */
    public boolean didLoose() {
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                if (isBombOnPosition(j, i) && getField(j, i) == 1){
                    return true;
                }
            }
        }
        return false;
    }

    public int getRows() {
        return rowsCount;
    }

    public int getColumns() {
        return columnsCount;
    }

}
