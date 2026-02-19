package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CardView extends StackPane {

    //definimos el tamaño
    private static final double CARD_WIDTH = 80;
    private static final double CARD_HEIGHT = 120;

    public CardView(CartaInglesa carta) {
        setPrefSize(CARD_WIDTH, CARD_HEIGHT);

        String nombreArchivo;

        if (carta == null || !carta.isFaceup()) {
            nombreArchivo = "back.gif";
        } else {
            String valorTexto = obtenerNombreValor(carta.getValor());
            String paloTexto = obtenerNombrePalo(carta.getPalo());
            nombreArchivo = valorTexto + "-of-" + paloTexto + ".gif";
        }

        try {
            //carga desde resources/images/
            String ruta = "/images/" + nombreArchivo;
            var stream = getClass().getResourceAsStream(ruta);

            if (stream != null) {
                Image img = new Image(stream);
                ImageView imgView = new ImageView(img);
                imgView.setFitWidth(CARD_WIDTH);
                imgView.setFitHeight(CARD_HEIGHT);
                getChildren().add(imgView);
            } else {
                //si falla la imagen, dibuja un cuadro rojo con texto
                dibujarPlaceholderError(nombreArchivo);
            }
        } catch (Exception e) {
            dibujarPlaceholderError("Error");
        }
    }

    private String obtenerNombreValor(int valor) {
        switch (valor) {
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                return "five";
            case 6:
                return "six";
            case 7:
                return "seven";
            case 8:
                return "eight";
            case 9:
                return "nine";
            case 10:
                return "ten";
            case 11:
                return "jack";
            case 12:
                return "queen";
            case 13:
                return "king";
            case 14:
                return "ace";
            default:
                return "ace";
        }
    }

    //aquí dependiendo de lo necesario traducimos el palo
    private String obtenerNombrePalo(Palo palo) {
        switch (palo) {
            case TREBOL:
                return "clubs";
            case DIAMANTE:
                return "diamonds";
            case CORAZON:
                return "hearts";
            case PICA:
                return "spades";
            default:
                return "spades";
        }
    }

    //placeholder en caso de no contar con la carta o nombre: 2
    private void dibujarPlaceholderError(String texto) {
        Rectangle bg = new Rectangle(CARD_WIDTH, CARD_HEIGHT, Color.RED);
        bg.setStroke(Color.BLACK);
        Text t = new Text(texto);
        t.setWrappingWidth(CARD_WIDTH - 5);
        t.setStyle("-fx-font-size: 10px;");
        getChildren().addAll(bg, t);
    }
}