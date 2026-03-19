package solitaire;

import DeckOfCards.CartaInglesa;

public class DrawPile {
    private Pila<CartaInglesa> cartas;
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        DeckOfCards.Mazo mazo = new DeckOfCards.Mazo();
        cartas = new Pila<>();
        //pasamos las cartas del mazo la pila
        for (CartaInglesa c : mazo.getCartas()) {
            cartas.push(c);
        }
        setCuantasCartasSeEntregan(3);
    }

    public void setCuantasCartasSeEntregan(int cuantasCartasSeEntregan) {
        this.cuantasCartasSeEntregan = cuantasCartasSeEntregan;
    }

    public int getCuantasCartasSeEntregan() {
        return cuantasCartasSeEntregan;
    }

    public Pila<CartaInglesa> getCartas(int cantidad) {
        Pila<CartaInglesa> retiradas = new Pila<>();
        for (int i = 0; i < cantidad; i++) {
            if (!cartas.isEmpty()) {
                retiradas.push(cartas.pop());
            }
        }
        return retiradas.voltear();
    }

    public Pila<CartaInglesa> retirarCartas() {
        Pila<CartaInglesa> retiradas = new Pila<>();
        int maximoARetirar = cartas.size() < cuantasCartasSeEntregan ? cartas.size() : cuantasCartasSeEntregan;

        for (int i = 0; i < maximoARetirar; i++) {
            CartaInglesa retirada = cartas.pop();
            retirada.makeFaceUp();
            retiradas.push(retirada);
        }
        return retiradas.voltear();
    }

    public boolean hayCartas() {
        return !cartas.isEmpty();
    }

    public CartaInglesa verCarta() {
        return cartas.peek();
    }

    public void recargar(Pila<CartaInglesa> cartasAgregar) {
        cartas.clear();

        //volteamos la pila para no arruinar el orden que ya traía del WastePile
        Pila<CartaInglesa> ordenadas = cartasAgregar.voltear();

        while (!ordenadas.isEmpty()) {
            CartaInglesa c = ordenadas.pop();
            c.makeFaceDown();
            cartas.push(c);
        }
    }

    public void devolverCartasFuerza(Pila<CartaInglesa> bloque) {
        while (!bloque.isEmpty()) {
            CartaInglesa c = bloque.pop();
            c.makeFaceDown();
            cartas.push(c);
        }
    }

    @Override
    public String toString() {
        if (cartas.isEmpty()) {
            return "-E-";
        }
        return "@";
    }
}