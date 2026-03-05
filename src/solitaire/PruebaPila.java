package solitaire;

public class PruebaPila {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS DE LA PILA ===");

        // 1. Crear una pila de prueba (usaremos Strings para que sea fácil de leer)
        Pila<String> miPila = new Pila<>();

        // 2. Probar si está vacía al inicio
        System.out.println("1. ¿Está vacía al inicio? " + miPila.isEmpty() + " (Debería ser true)");

        // 3. Probar el push (agregar elementos)
        miPila.push("Carta A");
        miPila.push("Carta B");
        miPila.push("Carta C");
        System.out.println("2. Tamaño después de 3 push: " + miPila.size() + " (Debería ser 3)");

        // 4. Probar el peek (ver la cima sin sacarla)
        System.out.println("3. Ver la cima: " + miPila.peek() + " (Debería ser Carta C)");
        System.out.println("   Tamaño después de peek: " + miPila.size() + " (Debería seguir siendo 3)");

        // 5. Probar el voltear (Invertir la pila)
        Pila<String> pilaInvertida = miPila.voltear();
        System.out.println("4. Ver la cima de la pila invertida: " + pilaInvertida.peek() + " (Debería ser Carta A)");

        // 6. Probar el pop (sacar elementos)
        System.out.println("5. Sacando el elemento de la cima: " + miPila.pop() + " (Debería ser Carta C)");
        System.out.println("   Nueva cima después del pop: " + miPila.peek() + " (Debería ser Carta B)");
        System.out.println("   Tamaño después del pop: " + miPila.size() + " (Debería ser 2)");

        // 7. Probar vaciarla (clear)
        miPila.clear();
        System.out.println("6. ¿Está vacía después de clear()? " + miPila.isEmpty() + " (Debería ser true)");
        System.out.println("=== PRUEBAS TERMINADAS ===");
    }
}