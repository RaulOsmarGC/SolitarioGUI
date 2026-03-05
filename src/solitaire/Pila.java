package solitaire;

public class Pila<T> {
    private Object[] elementos;
    private int tamano;
    private static final int CAPACIDAD_INICIAL = 10;

    public Pila() {
        elementos = new Object[CAPACIDAD_INICIAL];
        tamano = 0;
    }

    private void expandirCapacidad() {
        Object[] nuevoArreglo = new Object[elementos.length * 2];
        System.arraycopy(elementos, 0, nuevoArreglo, 0, tamano);
        elementos = nuevoArreglo;
    }

    public void push(T elemento) {
        if (tamano == elementos.length) {
            expandirCapacidad();
        }
        elementos[tamano] = elemento;
        tamano++;
    }

    public T pop() {
        if (isEmpty()) return null;

        tamano--;
        T dato = (T) elementos[tamano];
        elementos[tamano] = null;

        return dato;
    }

    public T peek() {
        if (isEmpty()) return null;
        return (T) elementos[tamano - 1];
    }

    public boolean isEmpty() {
        return tamano == 0;
    }

    public int size() {
        return tamano;
    }

    public void clear() {
        for (int i = 0; i < tamano; i++) {
            elementos[i] = null;
        }
        tamano = 0;
    }


    public Pila<T> voltear() {
        Pila<T> invertida = new Pila<>();
        for (int i = tamano - 1; i >= 0; i--) {
            invertida.push((T) elementos[i]);
        }
        return invertida;
    }
}