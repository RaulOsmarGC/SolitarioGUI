package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

import java.util.ArrayList;
/**
 * Juego de solitario.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class SolitaireGame {
    TableauDeck[] tableau = new TableauDeck[7];
    FoundationDeck[] foundation = new FoundationDeck[4];
    FoundationDeck lastFoundationUpdated;
    DrawPile drawPile;
    WastePile wastePile;
    Pila<Movimiento> historialMovimientos = new Pila<>();


    public SolitaireGame() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        createTableaux();
        createFoundations();
        wastePile.addCartas(drawPile.retirarCartas());
    }

    public void drawCards() {
        Pila<CartaInglesa> cards = drawPile.retirarCartas();
        //guardamos el tamaño antes de enviar la pila
        int cantidadMovida = cards.size();
        wastePile.addCartas(cards);
        //usamos la variable guardada
        historialMovimientos.push(new Movimiento("DRAW", -1, -1, cantidadMovida, false));
    }

    public void reloadDrawPile() {
        Pila<CartaInglesa> cards = wastePile.emptyPile();
        //guardamos el tamaño antes de enviar la pila
        int cantidadMovida = cards.size();
        drawPile.recargar(cards);
        //usamos la variable guardada
        historialMovimientos.push(new Movimiento("RELOAD", -1, -1, cantidadMovida, false));
    }

    //tomar la carta de la pila de descarte y ponerla en el tablero
    public boolean moveWasteToTableau(int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck destino = tableau[tableauDestino - 1];

        //vemos la carta del descarte
        CartaInglesa carta = wastePile.verCarta();

        //intentamos agregarla directamente al tablero
        if (carta != null && destino.agregarCarta(carta)) {
            //si podemos, la sacamos definitivamente del descarte
            wastePile.getCarta();
            movimientoRealizado = true;

            //registro descarte a tablero
            historialMovimientos.push(new Movimiento("WASTE_TO_TABLEAU", -1, tableauDestino - 1, 1, false));
        }

        return movimientoRealizado;
    }

    /**
     * Tomar varias cartas del Tableau fuente y colocarlas en el
     * Tableau destino.
     *
     * @param tableauFuente  de donde se toma la carta (1-7)
     * @param tableauDestino donde se coloca la carta (1-7)
     * @return true si se pudo hacer el movimiento, false si no
     */
    public boolean moveTableauToTableau(int tableauFuente, int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau[tableauFuente - 1];

        if (!fuente.isEmpty()) {
            TableauDeck destino = tableau[tableauDestino - 1];
            int valorQueDebeTenerLaCartaInicialDeLaFuente;
            CartaInglesa cartaUltimaDelDestino;

            if (!destino.isEmpty()) {
                cartaUltimaDelDestino = destino.getUltimaCarta();
                valorQueDebeTenerLaCartaInicialDeLaFuente = cartaUltimaDelDestino.getValor() - 1;
            } else {
                valorQueDebeTenerLaCartaInicialDeLaFuente = 13;
            }

            CartaInglesa cartaInicialDePrueba = fuente.viewCardStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);
            boolean destaparaCarta = false;

            if (cartaInicialDePrueba != null && destino.sePuedeAgregarCarta(cartaInicialDePrueba)) {
                Pila<CartaInglesa> cartas = fuente.removeStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);

                //guardamos el tamaño antes de que la pila se vacie
                int cantidadMovida = cartas.size();

                if (destino.agregarBloqueDeCartas(cartas)) {
                    if (!fuente.isEmpty() && !fuente.getUltimaCarta().isFaceup()) {
                        fuente.getUltimaCarta().makeFaceUp();
                        destaparaCarta = true;
                    }
                    movimientoRealizado = true;
                    //usamos la variable cantidadMovida en lugar de cartas.size()
                    historialMovimientos.push(new Movimiento("TABLEAU_TO_TABLEAU", tableauFuente - 1, tableauDestino - 1, cantidadMovida, destaparaCarta));
                } else {
                    //por si algo fallara, devolvemos las cartas a su origen
                    fuente.devolverBloque(cartas);
                }
            }
        }
        return movimientoRealizado;
    }

    public boolean moveTableauToFoundation(int numero) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau[numero - 1];

        //si la columna esta vacia, no hacemos nada
        if (fuente.isEmpty()) return false;

        //solo vemos la carta, no la sacamos todavia
        CartaInglesa carta = fuente.getUltimaCarta();

        //intentamos enviarla a la base
        if (carta != null && moveCartaToFoundation(carta)) {
            //si entra, ahora si la eliminamos del tablero
            fuente.getCards().pop();

            //verificamos si la nueva carta en la cima necesita ser volteada
            boolean destaparaCarta = false;
            if (!fuente.isEmpty() && !fuente.getUltimaCarta().isFaceup()) {
                fuente.getUltimaCarta().makeFaceUp();
                //guardamos este dato para el undo
                destaparaCarta = true;
            }

            movimientoRealizado = true;
            //registramos el movimiento
            historialMovimientos.push(new Movimiento("TABLEAU_TO_FOUNDATION", numero - 1, carta.getPalo().ordinal(), 1, destaparaCarta));
        }
        //si la base no la acepto, no hacemos nada
        return movimientoRealizado;
    }

    public boolean moveWasteToFoundation() {
        boolean movimientoRealizado = false;
        CartaInglesa carta = wastePile.verCarta();
        if (carta != null && moveCartaToFoundation(carta)) {
            carta = wastePile.getCarta();
            movimientoRealizado = true;
            //registro descarte a base
            historialMovimientos.push(new Movimiento("WASTE_TO_FOUNDATION", -1, carta.getPalo().ordinal(), 1, false));
        }
        return movimientoRealizado;
    }

    /**
     * Coloca la carta recibida en el Tableau recibido.
     *
     * @param carta   a colocar
     * @param destino Tableau que recibe la carta.
     * @return true si se pudo hacer el movimiento, false si no
     */
    private boolean moveCartaToTableau(CartaInglesa carta, TableauDeck destino) {
        return destino.agregarCarta(carta);
    }

    /**
     * Coloca la carta recibida en el Foundation correspondiente.
     *
     * @param carta a colocar
     * @return true si se pudo hacer el movimiento, false si no.
     */
    private boolean moveCartaToFoundation(CartaInglesa carta) {
        int cualFoundation = carta.getPalo().ordinal();
        FoundationDeck destino = foundation[cualFoundation];
        lastFoundationUpdated = destino;
        return destino.agregarCarta(carta);
    }

    /**
     * Determina si se terminó el juego. El juego se
     * termina cuando todas las cartas están en Foundation
     *
     * @return true si se terminó el juego
     */
    public boolean isGameOver() {
        boolean gameOver = true;
        for (FoundationDeck foundation : foundation) {
            if (foundation.estaVacio()) {
                gameOver = false;
            } else {
                CartaInglesa ultimaCarta = foundation.getUltimaCarta();
                //si la ultima carta no es rey, no se ha terminado
                if (ultimaCarta.getValor() != 13) {
                    gameOver = false;
                }
            }
        }
        return gameOver;
    }

    private void createFoundations() {
        int i = 0;
        for (Palo palo : Palo.values()) {
            foundation[i] = new FoundationDeck(palo);
            i++;
        }
    }

    private void createTableaux() {
        for (int i = 0; i < 7; i++) {
            tableau[i] = new TableauDeck();
            tableau[i].inicializar(drawPile.getCartas(i + 1));
        }
    }

    public DrawPile getDrawPile() {
        return drawPile;
    }

    //arreglos
    public TableauDeck[] getTableau() {
        return tableau;
    }

    public WastePile getWastePile() {
        return wastePile;
    }

    public FoundationDeck getLastFoundationUpdated() {
        return lastFoundationUpdated;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        //bases
        str.append("Foundation\n");
        for (FoundationDeck foundationDeck : foundation) {
            str.append(foundationDeck);
            str.append("\n");
        }

        //tablero
        str.append("\nTableaux\n");
        int tableauNumber = 1;
        for (TableauDeck tableauDeck : tableau) {
            str.append(tableauNumber + " ");
            str.append(tableauDeck);
            str.append("\n");
            tableauNumber++;
        }
        str.append("Waste\n");
        str.append(wastePile);
        str.append("\nDraw\n");
        str.append(drawPile);
        return str.toString();
    }

    public FoundationDeck[] getFoundations() {
        return foundation;
    }

     //deshace el último movimiento registrado en el historial
    public void undo() {
        if (historialMovimientos.isEmpty()) {
            return;
        }

        Movimiento ultimo = historialMovimientos.pop();

        switch (ultimo.tipoAccion) {
            case "DRAW":
                //regresar cartas del descarte a la pila de reserva
                Pila<CartaInglesa> cartasRegresar = new Pila<>();
                for (int i = 0; i < ultimo.cantidadCartas; i++) {
                    CartaInglesa c = wastePile.getCarta();
                    if (c != null) {
                        cartasRegresar.push(c);
                    }
                }
                //mantener el orden original exacto
                drawPile.devolverCartasFuerza(cartasRegresar.voltear());
                break;

            case "RELOAD":
                //extrae las cartas de la pila de reserva
                Pila<CartaInglesa> recuperadas = new Pila<>();
                while (drawPile.hayCartas()) {
                    Pila<CartaInglesa> cartaUnica = drawPile.getCartas(1);
                    if (!cartaUnica.isEmpty()) {
                        CartaInglesa c = cartaUnica.pop();
                        c.makeFaceUp();
                        recuperadas.push(c);
                    }
                }
                //las devolvemos a la pila de descaarte para que el tablero regrese a la normalidad
                wastePile.addCartas(recuperadas.voltear());
                break;
            case "WASTE_TO_TABLEAU":
                //quitar del tablero y regresar al descarte
                CartaInglesa c1 = tableau[ultimo.destino].quitarCartaFuerza();
                Pila<CartaInglesa> temp1 = new Pila<>();
                temp1.push(c1);
                wastePile.addCartas(temp1);
                break;

            case "TABLEAU_TO_TABLEAU":
                //ocultar carta en el origen si se habia destapado
                if (ultimo.seDestapoCarta && !tableau[ultimo.origen].isEmpty()) {
                    tableau[ultimo.origen].getUltimaCarta().makeFaceDown();
                }
                //extraer bloque del destino de forma segura
                Pila<CartaInglesa> temp2 = new Pila<>();
                for(int i = 0; i < ultimo.cantidadCartas; i++) {
                    temp2.push(tableau[ultimo.destino].quitarCartaFuerza());
                }
                //devolverlas al origen usando el forzado
                tableau[ultimo.origen].devolverBloque(temp2);
                break;

            case "TABLEAU_TO_FOUNDATION":
                //ocultar carta en origen si se habia destapado
                if (ultimo.seDestapoCarta && !tableau[ultimo.origen].isEmpty()) {
                    tableau[ultimo.origen].verUltimaCarta().makeFaceDown();
                }
                //regresar de la base al tablero forzando la entrada
                CartaInglesa c2 = foundation[ultimo.destino].removerUltimaCarta();
                tableau[ultimo.origen].devolverCarta(c2);
                break;

            case "WASTE_TO_FOUNDATION":
                //regresar de la base al descarte
                CartaInglesa c3 = foundation[ultimo.destino].removerUltimaCarta();
                Pila<CartaInglesa> temp3 = new Pila<>();
                temp3.push(c3);
                wastePile.addCartas(temp3);
                break;
        }
    }

    public void reiniciarJuego() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        //vaciamos el historial del undo
        historialMovimientos.clear();

        createTableaux();
        createFoundations();
        wastePile.addCartas(drawPile.retirarCartas());
    }



}
