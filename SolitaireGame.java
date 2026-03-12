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
    /**
     * Move cards from Waste pile to Draw Pile.
     */
    public void reloadDrawPile() {
        Pila<CartaInglesa> cards = wastePile.emptyPile();
        drawPile.recargar(cards);
    }

    /**
     * Move cards from Draw pile to Waste Pile.
     */
    public void drawCards() {
        Pila<CartaInglesa> cards = drawPile.retirarCartas();
        wastePile.addCartas(cards);
    }

    /**
     * Tomar la carta del Waste pile y ponerla en el tableau
     *
     * @param tableauDestino donde se coloca la carta
     * @return true si se pudo hacer el movimiento, false si no
     */
    public boolean moveWasteToTableau(int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck destino = tableau[tableauDestino - 1];
        if (moveWasteToTableau(destino)) {
            movimientoRealizado = true;
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
                cartaUltimaDelDestino = destino.verUltimaCarta();
                valorQueDebeTenerLaCartaInicialDeLaFuente = cartaUltimaDelDestino.getValor() - 1;
            } else {
                valorQueDebeTenerLaCartaInicialDeLaFuente = 13;
            }
            CartaInglesa cartaInicialDePrueba = fuente.viewCardStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);
            if (cartaInicialDePrueba != null && destino.sePuedeAgregarCarta(cartaInicialDePrueba)) {
                Pila<CartaInglesa> cartas = fuente.removeStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);
                if (destino.agregarBloqueDeCartas(cartas)) {
                    if (!fuente.isEmpty()) {
                        fuente.verUltimaCarta().makeFaceUp();
                    }
                    movimientoRealizado = true;
                }
            }

        }
        return movimientoRealizado;
    }

    /**
     * Tomar la carta de Tableau y colocarla en el Foundation.
     *
     * @param numero de tableau donde se moverá la carta (1-7)
     * @return true si se pudo move la carta, false si no
     */
    public boolean moveTableauToFoundation(int numero) {
        boolean movimientoRealizado = false;

        TableauDeck fuente = tableau[numero - 1];
        CartaInglesa carta = fuente.removerUltimaCarta();

        if (moveCartaToFoundation(carta)) {
            movimientoRealizado = true;
        } else {
            fuente.agregarCarta(carta);
        }
        return movimientoRealizado;
    }

    /**
     * Tomar la carta de Waste y colocarla en el Tableau.
     *
     * @param tableau donde se moverá la carta
     * @return true si se pudo move la carta, false si no
     */
    public boolean moveWasteToTableau(TableauDeck tableau) {
        boolean movimientoRealizado = false;

        CartaInglesa carta = wastePile.verCarta();
        if (moveCartaToTableau(carta, tableau)) {
            //si es movimiento válido, elimina la carta de la pila
            carta = wastePile.getCarta();
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    /**
     * Tomar una carta de Waste y ponerla en una de las Foundations.
     *
     * @return true si se pudo hacer el movimiento.
     */

    public boolean moveWasteToFoundation() {
        boolean movimientoRealizado = false;

        CartaInglesa carta = wastePile.verCarta();
        if (moveCartaToFoundation(carta)) {
            //si es movimiento válido, elimina la carta de la pila
            carta = wastePile.getCarta();
            movimientoRealizado = true;
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

    //arreglo normal
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
        //add foundations
        str.append("Foundation\n");
        for (FoundationDeck foundationDeck : foundation) {
            str.append(foundationDeck);
            str.append("\n");
        }

        //add tableaux
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

    /**
     * Deshace el último movimiento registrado en el historial.
     */
    public void undo() {
        if (historialMovimientos.isEmpty()) {
            System.out.println("No hay movimientos para deshacer.");
            return;
        }

        Movimiento ultimo = historialMovimientos.pop();

        switch (ultimo.tipoAccion) {
            case "WASTE_TO_TABLEAU":
                //quitar carta del tablero y regresarla al descarte
                CartaInglesa carta = tableau[ultimo.destino].removerUltimaCarta();
                Pila<CartaInglesa> temporal = new Pila<>();
                temporal.push(carta);
                wastePile.addCartas(temporal);
                break;

            case "TABLEAU_TO_TABLEAU":
                //restaurar la carta boca abajo en el origen si se había destapado
                if (ultimo.seDestapoCarta && !tableau[ultimo.origen].isEmpty()) {
                    tableau[ultimo.origen].verUltimaCarta().makeFaceDown();
                }
                //extraer el bloque del destino
                Pila<CartaInglesa> cartasRegresar = new Pila<>();
                for(int i = 0; i < ultimo.cantidadCartas; i++) {
                    cartasRegresar.push(tableau[ultimo.destino].removerUltimaCarta());
                }
                //devolverlas al origen
                tableau[ultimo.origen].agregarBloqueDeCartas(cartasRegresar.voltear());
                break;

            case "TABLEAU_TO_FOUNDATION":
                //restaurar la carta boca abajo en el origen si se destapó
                if (ultimo.seDestapoCarta && !tableau[ultimo.origen].isEmpty()) {
                    tableau[ultimo.origen].verUltimaCarta().makeFaceDown();
                }
                //quitar del Foundation y regresar al tablero
                CartaInglesa c2 = foundation[ultimo.destino].removerUltimaCarta();
                tableau[ultimo.origen].agregarCarta(c2);
                break;

            case "WASTE_TO_FOUNDATION":
                //quitar de las bases y regresar al descarte
                CartaInglesa c3 = foundation[ultimo.destino].removerUltimaCarta();
                Pila<CartaInglesa> temp3 = new Pila<>();
                temp3.push(c3);
                wastePile.addCartas(temp3);
                break;
        }
    }

}
