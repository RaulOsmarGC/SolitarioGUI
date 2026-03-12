package solitaire;

import DeckOfCards.CartaInglesa;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SolitaireBoard extends BorderPane {

    private final SolitaireGame game;

    //contenedores visuales para las distintas zonas
    private StackPane drawPileView;
    private StackPane wastePileView;
    private List<StackPane> foundationViews;
    private List<VBox> tableauViews;

    //constantes de tamaño para las cartas y márgenes
    private static final double CARD_WIDTH = 80;
    private static final double CARD_HEIGHT = 120;
    private static final double MARGIN = 10;

    //variables para controlar el estado del arrastre
    private CardView draggedCardView = null;
    //origen "WASTE", "TABLEAU", "FOUNDATION"
    //descarte, tablero, base
    private String dragSourceType = "";
    private int dragSourceIndex = -1;

    public SolitaireBoard(SolitaireGame game) {
        this.game = game;

        //configuración del fondo verde
        this.setStyle("-fx-background-color: #006400;"); //mesa verde oscuro
        this.setPadding(new Insets(MARGIN));

        //construcción de las zonas visuales
        setupTopZone();
        setupBottomZone();

        //dibuja el estado inicial de las cartas
        refreshGame();
    }

    //configura la parte superior mazo, descarte, bases y boton UNDO
    private void setupTopZone() {
        HBox topBox = new HBox(MARGIN);
        topBox.setPadding(new Insets(MARGIN));
        topBox.setAlignment(Pos.CENTER_LEFT);

        //deshacer (solo el botón)
        Button btnUndo = new Button("Deshacer");
        btnUndo.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #f4a261; -fx-text-fill: white;");
        btnUndo.setOnAction(e -> {
            game.undo();
            refreshGame();
        });

        //zona del mazo
        drawPileView = new StackPane();
        configureSlot(drawPileView);
        drawPileView.setOnMouseClicked(e -> handleDrawPileClick());

        //zona del descarte
        wastePileView = new StackPane();
        configureSlot(wastePileView);

        //espaciador invisible para empujar las bases a la derecha
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        //zonas de las 4 bases
        foundationViews = new ArrayList<>();
        HBox foundationBox = new HBox(MARGIN);
        for (int i = 0; i < 4; i++) {
            StackPane fView = new StackPane();
            configureSlot(fView);
            int finalI = i;

            //permite que las bases reciban cartas
            setupDragTarget(fView, "FOUNDATION", finalI);

            foundationViews.add(fView);
            foundationBox.getChildren().add(fView);
        }

        topBox.getChildren().addAll(btnUndo, drawPileView, wastePileView, spacer, foundationBox);
        this.setTop(topBox);
    }

    //configura la parte inferior las 7 columnas
    private void setupBottomZone() {
        HBox bottomBox = new HBox(MARGIN);
        bottomBox.setPadding(new Insets(MARGIN));
        bottomBox.setAlignment(Pos.CENTER);

        tableauViews = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            //vbox con espaciado negativo para efecto cascada
            VBox tView = new VBox(-90);
            tView.setMinWidth(CARD_WIDTH);
            tView.setMinHeight(CARD_HEIGHT);

            //placeholder transparente para detectar drops en columnas vacías
            Rectangle placeholder = new Rectangle(CARD_WIDTH, CARD_HEIGHT, Color.TRANSPARENT);
            placeholder.setStroke(Color.LIGHTGRAY);
            placeholder.getStrokeDashArray().addAll(10d, 10d);
            tView.getChildren().add(placeholder);

            int finalI = i;
            setupDragTarget(tView, "TABLEAU", finalI);

            tableauViews.add(tView);
            bottomBox.getChildren().add(tView);
        }

        this.setCenter(bottomBox);
    }

    //método auxiliar para dibujar el borde verde de los espacios vacíos
    private void configureSlot(StackPane slot) {
        slot.setMinWidth(CARD_WIDTH);
        slot.setMinHeight(CARD_HEIGHT);
        slot.setMaxWidth(CARD_WIDTH);
        slot.setMaxHeight(CARD_HEIGHT);
        Rectangle border = new Rectangle(CARD_WIDTH, CARD_HEIGHT, Color.TRANSPARENT);
        border.setStroke(Color.DARKGREEN.brighter());
        border.setStrokeWidth(2);
        border.setArcWidth(10);
        border.setArcHeight(10);
        slot.getChildren().add(border);
    }

    //actualiza toda la vista basándose en los datos del juego
    public void refreshGame() {
        //actualizar mazo
        drawPileView.getChildren().clear();
        configureSlot(drawPileView);
        if (game.getDrawPile().hayCartas()) {
            //muestra carta boca abajo si hay cartas
            drawPileView.getChildren().add(new CardView(null));
        } else {
            //icono de recargar si está vacío
            Text resetIcon = new Text("↺");
            resetIcon.setFill(Color.WHITE);
            resetIcon.setFont(Font.font(30));
            drawPileView.getChildren().add(resetIcon);
        }

        //actualizar descarte
        wastePileView.getChildren().clear();
        configureSlot(wastePileView);
        if (game.getWastePile().hayCartas()) {
            CartaInglesa carta = game.getWastePile().verCarta();
            CardView cv = new CardView(carta);
            setupDragSource(cv, "WASTE", -1);

            //doble clic rápido para enviar a base
            cv.setOnMouseClicked(e -> {
                if(e.getClickCount() == 2) {
                    if(game.moveWasteToFoundation()) refreshGame();
                }
            });

            wastePileView.getChildren().add(cv);
        }

        //actualizar bases
        for (int i = 0; i < 4; i++) {
            StackPane fView = foundationViews.get(i);
            fView.getChildren().clear();
            configureSlot(fView);
        }

        //obtenemos el arreglo lógico (ya no es ArrayList)
        FoundationDeck[] foundations = game.getFoundations();

        for (int i = 0; i < 4; i++) {
            FoundationDeck deck = foundations[i];
            StackPane fView = foundationViews.get(i);

            //si hay cartas, dibujamos la última
            if (!deck.estaVacio()) {
                CartaInglesa c = deck.getUltimaCarta();
                fView.getChildren().add(new CardView(c));
            }
        }

        //actualizar columnas/tablero
        TableauDeck[] tableaux = game.getTableau();
        for (int i = 0; i < 7; i++) {
            VBox tView = tableauViews.get(i);
            var placeholder = tView.getChildren().get(0);
            tView.getChildren().clear();
            //mantener el placeholder al fondo
            tView.getChildren().add(placeholder);

            TableauDeck deck = tableaux[i];

            //reemplazamos el for-each por el while con la pila volteada
            Pila<CartaInglesa> temporal = deck.getCards().voltear();
            while (!temporal.isEmpty()) {
                CartaInglesa carta = temporal.pop();
                CardView cv = new CardView(carta);

                //solo las cartas boca arriba se pueden arrastrar
                if (carta.isFaceup()) {
                    setupDragSource(cv, "TABLEAU", i + 1);
                }

                //doble clic en la última carta para mover a base
                if (carta.isFaceup() && carta == deck.getUltimaCarta()) {
                    int finalI = i + 1;
                    cv.setOnMouseClicked(e -> {
                        if(e.getClickCount() == 2) {
                            if(game.moveTableauToFoundation(finalI)) refreshGame();
                        }
                    });
                }
                tView.getChildren().add(cv);
            }
        }

        if (game.isGameOver()) {
            new Alert(Alert.AlertType.INFORMATION, "Juego Terminado").show();
        }
    }

    //maneja el clic en el mazo para sacar cartas o recargar
    private void handleDrawPileClick() {
        if (!game.getDrawPile().hayCartas()) {
            game.reloadDrawPile();
        } else {
            game.drawCards();
        }
        refreshGame();
    }

    //método para configurar el inicio del arrastre
    private void setupDragSource(CardView node, String type, int index) {
        node.setOnDragDetected(event -> {
            //guardar qué se está arrastrando
            draggedCardView = node;
            dragSourceType = type;
            dragSourceIndex = index;

            //iniciar el modo de arrastre
            Dragboard db = node.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            //enviamos datos básicos como texto
            content.putString(type + ":" + index);
            db.setContent(content);

            //establecer la imagen que sigue al ratón
            db.setDragView(node.snapshot(null, null));

            event.consume();
        });
    }

    //método para configurar el final del arrastre
    private void setupDragTarget(Pane targetNode, String targetType, int targetIndex) {
        //validar cuando el ratón pasa por encima
        targetNode.setOnDragOver(event -> {
            if (event.getGestureSource() != targetNode && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        //ejecutar la lógica cuando se suelta la carta
        targetNode.setOnDragDropped(event -> {
            boolean success = false;

            //lógica si el destino es la base
            if ("FOUNDATION".equals(targetType)) {
                if ("WASTE".equals(dragSourceType)) {
                    success = game.moveWasteToFoundation();
                } else if ("TABLEAU".equals(dragSourceType)) {
                    success = game.moveTableauToFoundation(dragSourceIndex);
                }
            }
            //lógica si el destino es tableau
            else if ("TABLEAU".equals(targetType)) {
                int destIdx = targetIndex + 1;

                if ("WASTE".equals(dragSourceType)) {
                    success = game.moveWasteToTableau(destIdx);
                } else if ("TABLEAU".equals(dragSourceType)) {
                    //evitar mover sobre la misma columna
                    if (dragSourceIndex != destIdx) {
                        success = game.moveTableauToTableau(dragSourceIndex, destIdx);
                    }
                }
            }
            event.setDropCompleted(success);
            //si el movimiento fue válido
            if (success) {
                refreshGame();
            }
            event.consume();
        });
    }
}