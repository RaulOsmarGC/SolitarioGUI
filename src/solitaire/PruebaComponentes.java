package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

public class PruebaComponentes {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS DE COMPONENTES ===\n");

        // 1. PROBAR FOUNDATION DECK
        System.out.println("--- Probando Foundation (Pila LIFO) ---");
        FoundationDeck foundation = new FoundationDeck(Palo.CORAZON);
        CartaInglesa asCorazones = new CartaInglesa(14, Palo.CORAZON, "Rojo"); // Valor 14 = As
        CartaInglesa dosCorazones = new CartaInglesa(2, Palo.CORAZON, "Rojo");
        CartaInglesa tresCorazones = new CartaInglesa(3, Palo.CORAZON, "Rojo");

        System.out.println("Intentando agregar 2 sin As: " + foundation.agregarCarta(dosCorazones) + " (Debe ser false)");
        System.out.println("Agregando As: " + foundation.agregarCarta(asCorazones) + " (Debe ser true)");
        System.out.println("Agregando 2: " + foundation.agregarCarta(dosCorazones) + " (Debe ser true)");
        System.out.println("Foundation actual: " + foundation.toString() + "\n");

        // 2. PROBAR WASTE PILE
        System.out.println("--- Probando Waste Pile (Pila LIFO) ---");
        WastePile waste = new WastePile();
        Pila<CartaInglesa> cartasParaWaste = new Pila<>();
        cartasParaWaste.push(new CartaInglesa(5, Palo.TREBOL, "Negro"));
        cartasParaWaste.push(tresCorazones);

        waste.addCartas(cartasParaWaste);
        System.out.println("Waste actual (cima debe ser 3 de Corazones): " + waste.toString());
        CartaInglesa sacada = waste.getCarta();
        System.out.println("Carta sacada del Waste: " + sacada.toString());
        System.out.println("Waste después de sacar: " + waste.toString() + "\n");

        // 3. PROBAR TABLEAU DECK
        System.out.println("--- Probando Tableau (Extracción por bloques) ---");
        TableauDeck tableau = new TableauDeck();
        Pila<CartaInglesa> cartasIniciales = new Pila<>();
        cartasIniciales.push(new CartaInglesa(10, Palo.PICA, "Negro"));
        cartasIniciales.push(new CartaInglesa(9, Palo.CORAZON, "Rojo"));

        tableau.inicializar(cartasIniciales);
        System.out.println("Tableau Inicial: " + tableau.toString());

        // Intentar agregar un 8 Negro (debe permitirlo porque intercala colores y desciende)
        CartaInglesa ochoTrebol = new CartaInglesa(8, Palo.TREBOL, "Negro");
        System.out.println("¿Se puede agregar 8 de Trébol?: " + tableau.agregarCarta(ochoTrebol) + " (Debe ser true)");
        System.out.println("Tableau tras agregar 8: " + tableau.toString());

        // Probar extracción de bloque (desde el 9)
        Pila<CartaInglesa> bloque = tableau.removeStartingAt(9);
        System.out.println("Bloque extraído. El tableau ahora es: " + tableau.toString());

        System.out.println("\n=== PRUEBAS COMPLETADAS ===");
    }
}