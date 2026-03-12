package solitaire;

import DeckOfCards.CartaInglesa;

public class WastePile {
    private Pila<CartaInglesa> cartas;

    public WastePile() {
        cartas = new Pila<>();
    }

    public void addCartas(Pila<CartaInglesa> nuevas) {
        //volteamos las cartas nuevas para que mantengan el orden correcto al apilarlas
        Pila<CartaInglesa> temporales = nuevas.voltear();
        while (!temporales.isEmpty()) {
            cartas.push(temporales.pop());
        }
    }

    public Pila<CartaInglesa> emptyPile() {
        Pila<CartaInglesa> pile = new Pila<>();
        while (!cartas.isEmpty()) {
            pile.push(cartas.pop());
        }
        return pile.voltear(); //restauramos el orden original
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