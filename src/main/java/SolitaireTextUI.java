package solitaire; // Asegúrate de que tenga el paquete si las demás clases lo tienen

import java.util.Scanner;

public class SolitaireTextUI {
    SolitaireGame sg;

    public SolitaireTextUI() {
        sg = new SolitaireGame();
    }

    public void playGame() {
        int tableauFuente;
        int tableauDestino;
        Scanner sc = new Scanner(System.in);

        // mostrar estado actual del juego
        System.out.println(sg);
        menu();
        System.out.println("¿Qué operación quieres hacer?");

        String op = sc.nextLine();
        op = op.toUpperCase();
        while (!op.equals("Q") && !sg.isGameOver()) {
            switch (op) {
                case "A":
                    sg.moveWasteToFoundation();
                    break;
                case "B":
                    sg.drawCards();
                    break;
                case "C":
                    sg.reloadDrawPile();
                    break;
                case "D": 
                    System.out.println("Escribe el número del Tableau (1-7) de donde se toma la carta:");
                    tableauFuente = getTableauNumber(sc);
                    if (tableauFuente > 0 && tableauFuente <= 7) {
                        sg.moveTableauToFoundation(tableauFuente);
                    }
                    break;
                case "E": 
                    System.out.println("Escribe el número del Tableau (1-7) de donde se toma la carta: ");
                    tableauFuente = getTableauNumber(sc);
                    if (tableauFuente > 0 && tableauFuente <= 7) {
                        System.out.println("Escribe el número del Tableau (1-7) donde se colocará: ");
                        tableauDestino = getTableauNumber(sc);
                        if (tableauDestino > 0 && tableauDestino <= 7) {
                            sg.moveTableauToTableau(tableauFuente, tableauDestino);
                        }
                    }
                    break;
                case "F": 
                    System.out.println("Escribe el número del Tableau (1-7) donde se colocará: ");
                    tableauDestino = getTableauNumber(sc);
                    if (tableauDestino > 0 && tableauDestino <= 7) {
                        sg.moveWasteToTableau(tableauDestino);
                    }
                    break;
                case "G": 
                    System.out.println("Deshaciendo el último movimiento...");
                    // sg.undoLastMove(); // Lo implementaremos pronto
                    break;
                default:
                    System.out.println("Operación desconocida");
            }
            System.out.println(sg);

            menu();
            System.out.println("¿Qué operación quieres hacer?");
            op = sc.nextLine();
            op = op.toUpperCase();
        }
        if (sg.isGameOver()) {
            System.out.println("GAME OVER");
        }
    }

    private void menu() {
        System.out.println("A) Waste Pile to Foundation");
        System.out.println("B) Draw Cards.");
        System.out.println("C) Reload Draw Pile");
        System.out.println("D) Move Tableau to Foundation");
        System.out.println("E) Move Tableau to Tableau");
        System.out.println("F) Move Waste Pile to Tableau");
        System.out.println("G) Deshacer movimiento (Undo)");
        System.out.println("Q) Salir.");
    }

    private int getTableauNumber(Scanner sc) {
        int tableauNumber = 0;
        if (sc.hasNextInt()) {
            tableauNumber = sc.nextInt();
            sc.nextLine(); 
        } else {
            sc.nextLine(); 
        }
        return tableauNumber;
    }

    // --- AQUÍ ESTÁ EL MÉTODO MAIN QUE FALTABA ---
    public static void main(String[] args) {
        SolitaireTextUI ui = new SolitaireTextUI();
        ui.playGame();
    }
}