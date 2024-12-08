package com.takalace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BingoNumberManager {
    private final List<Integer> numbersPool;
    private final List<Integer> calledNumbers;

    public BingoNumberManager() {
        numbersPool = new ArrayList<>();
        for (int i = 1; i <= 75; i++) {
            numbersPool.add(i);
        }
        Collections.shuffle(numbersPool);
        calledNumbers = new ArrayList<>();
    }

    /**
     * Genera el siguiente número si quedan disponibles.
     * @return El siguiente número, o -1 si ya no quedan números.
     */
    public int generateNextNumber() {
        if (numbersPool.isEmpty()) {
            return -1; // No quedan números
        }
        int nextNumber = numbersPool.remove(0);
        calledNumbers.add(nextNumber);
        return nextNumber;
    }

    /**
     * Devuelve el último número llamado.
     * @return El último número, o -1 si aún no se ha llamado ningún número.
     */
    public int getLastNumber() {
        if (calledNumbers.isEmpty()) {
            return -1; //No se ha llamado ningún número
        }
        return calledNumbers.get(calledNumbers.size() - 1);
    }

    /**
     * Devuelve todos los números llamados hasta ahora.
     * @return Lista de números llamados en orden.
     */
    public List<Integer> getCalledNumbers() {
        return new ArrayList<>(calledNumbers);
    }


    /**
     * Reinicia el flujo de números.
     */
    public void reset() {
        numbersPool.clear();
        calledNumbers.clear();
        for (int i = 1; i <= 75; i++) {
            numbersPool.add(i);
        }
        Collections.shuffle(numbersPool);
    }
}
