package solitaire;

import DeckOfCards.CartaInglesa;

public class WastePile {
    private Pila<CartaInglesa> cartas;

    public WastePile() {
        cartas = new Pila<>();
    }

    public void addCartas(Pila<CartaInglesa> nuevas) {
        while (!nuevas.isEmpty()) {
            cartas.push(nuevas.pop());
        }
    }

    public Pila<CartaInglesa> emptyPile() {
        Pila<CartaInglesa> temporal = new Pila<>();
        //esto saca las cartas y las invierte
        while (!cartas.isEmpty()) {
            temporal.push(cartas.pop());
        }
        return temporal;
    }

    public CartaInglesa verCarta() {
        return cartas.peek();
    }

    public CartaInglesa getCarta() {
        return cartas.pop();
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (cartas.isEmpty()) {
            stb.append("---");
        } else {
            CartaInglesa regresar = cartas.peek();
            regresar.makeFaceUp();
            stb.append(regresar.toString());
        }
        return stb.toString();
    }

    public boolean hayCartas() {
        return !cartas.isEmpty();
    }
}