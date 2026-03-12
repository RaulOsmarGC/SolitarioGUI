package solitaire;

import DeckOfCards.CartaInglesa;

public class TableauDeck {
    Pila<CartaInglesa> cartas = new Pila<>();

    public void inicializar(Pila<CartaInglesa> cartasIniciales) {
        this.cartas = cartasIniciales;
        if (!this.cartas.isEmpty()) {
            this.cartas.peek().makeFaceUp();
        }
    }

    public Pila<CartaInglesa> removeStartingAt(int value) {
        Pila<CartaInglesa> tempStack = new Pila<>();
        boolean encontrada = false;

        while (!cartas.isEmpty()) {
            CartaInglesa top = cartas.pop();
            tempStack.push(top);

            if (top.isFaceup() && top.getValor() == value) {
                encontrada = true;
                break;
            }
        }

        if (encontrada) {
            //tempStack ya tiene la secuencia completa en el orden correcto
            return tempStack;
        } else {
            while (!tempStack.isEmpty()) {
                cartas.push(tempStack.pop());
            }
            return new Pila<>();
        }
    }
    public CartaInglesa viewCardStartingAt(int value) {
        Pila<CartaInglesa> tempStack = new Pila<>();
        CartaInglesa cartaConElValorDeseado = null;

        while (!cartas.isEmpty()) {
            CartaInglesa top = cartas.pop();
            tempStack.push(top);

            if (top.isFaceup() && top.getValor() == value) {
                cartaConElValorDeseado = top;
                break;
            }
        }

        while (!tempStack.isEmpty()) {
            cartas.push(tempStack.pop());
        }

        return cartaConElValorDeseado;
    }

    public boolean agregarCarta(CartaInglesa carta) {
        if (sePuedeAgregarCarta(carta)) {
            carta.makeFaceUp();
            cartas.push(carta);
            return true;
        }
        return false;
    }

    CartaInglesa verUltimaCarta() {
        return cartas.peek();
    }

    CartaInglesa removerUltimaCarta() {
        CartaInglesa ultimaCarta = cartas.pop();
        if (ultimaCarta != null && !cartas.isEmpty() && !cartas.peek().isFaceup()) {
            cartas.peek().makeFaceUp();
        }
        return ultimaCarta;
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

    public boolean agregarBloqueDeCartas(Pila<CartaInglesa> cartasRecibidas) {
        if (!cartasRecibidas.isEmpty()) {
            CartaInglesa primera = cartasRecibidas.peek();
            if (sePuedeAgregarCarta(primera)) {
                //aquí damos pop y push
                while (!cartasRecibidas.isEmpty()) {
                    cartas.push(cartasRecibidas.pop());
                }
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return cartas.isEmpty();
    }

    public boolean sePuedeAgregarCarta(CartaInglesa cartaInicialDePrueba) {
        if (!cartas.isEmpty()) {
            CartaInglesa ultima = cartas.peek();
            if (!ultima.getColor().equals(cartaInicialDePrueba.getColor())) {
                if (ultima.getValor() == cartaInicialDePrueba.getValor() + 1) {
                    return true;
                }
            }
        } else {
            if (cartaInicialDePrueba.getValor() == 13) {
                return true;
            }
        }
        return false;
    }

    public CartaInglesa getUltimaCarta() {
        return cartas.peek();
    }

    public Pila<CartaInglesa> getCards() {
        return cartas;
    }

    //fuerza el ingreso de una carta sin validar las reglas del solitario
    public void devolverCarta(CartaInglesa carta) {
        cartas.push(carta);
    }

     //fuerza el ingreso de un bloque de cartas sin validar reglas
    public void devolverBloque(Pila<CartaInglesa> bloque) {
        while (!bloque.isEmpty()) {
            cartas.push(bloque.pop());
        }
    }

    public CartaInglesa quitarCartaFuerza() {
        return cartas.pop();
    }
}