package solitaire;

public class Movimiento {
    String tipoAccion;
    int origen;
    int destino;
    int cantidadCartas;
    boolean seDestapoCarta;

    public Movimiento(String tipoAccion, int origen, int destino, int cantidadCartas, boolean seDestapoCarta) {
        this.tipoAccion = tipoAccion;
        this.origen = origen;
        this.destino = destino;
        this.cantidadCartas = cantidadCartas;
        this.seDestapoCarta = seDestapoCarta;
    }
}