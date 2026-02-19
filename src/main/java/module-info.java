module com.clase.solitario { // El nombre puede variar
    requires javafx.controls;
    requires javafx.fxml;

    // Permite que JavaFX use tus paquetes
    exports solitaire;
    exports DeckOfCards;
}