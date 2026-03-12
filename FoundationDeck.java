package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

public class FoundationDeck {
    Palo palo;
    Pila<CartaInglesa> cartas = new Pila<>();

    public FoundationDeck(Palo palo) {
        this.palo = palo;
    }

    public FoundationDeck(CartaInglesa carta) {
        palo = carta.getPalo();
        if (carta.getValorBajo() == 1) {
            cartas.push(carta);
        }
    }

    public boolean agregarCarta(CartaInglesa carta) {
        boolean agregado = false;
        if (carta.tieneElMismoPalo(palo)) {
            if (cartas.isEmpty()) {
                if (carta.getValorBajo() == 1) {
                    cartas.push(carta);
                    agregado = true;
                }
            } else {
                CartaInglesa ultimaCarta = cartas.peek();
                if (ultimaCarta.getValorBajo() + 1 == carta.getValorBajo()) {
                    cartas.push(carta);
                    agregado = true;
                }
            }
        }
        return agregado;
    }

    CartaInglesa removerUltimaCarta() {
        return cartas.pop();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (cartas.isEmpty()) {
            builder.append("---");
        } else {
            //creamos la pila temporal invertida
            Pila<CartaInglesa> temporal = cartas.voltear();

            //usamos un while y pop() en lugar del for-each
            while (!temporal.isEmpty()) {
                builder.append(temporal.pop().toString());
            }
        }
        return builder.toString();
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    public CartaInglesa getUltimaCarta() {
        return cartas.peek();
    }
}