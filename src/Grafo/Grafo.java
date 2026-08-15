package Grafo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Grafo ponderado y no dirigido que representa el mapa de la zona de entregas.
 */
public class Grafo {
    // Lista de adyacencia: Cada vértice tiene una lista de aristas (conexiones)
    private Map<String, List<Arista>> adyacencia;

    public Grafo() {
        this.adyacencia = new HashMap<>();
    }

    /**
     * Agrega un nuevo vértice al grafo.
     * @param vertice Nombre de la ubicación (ej. "Tienda", "Casa Cliente")
     */
    public void insertarVertice(String vertice) {
        if (!adyacencia.containsKey(vertice)) {
            adyacencia.put(vertice, new ArrayList<>());
        }
    }

    /**
     * Agrega una conexión bidireccional entre dos vértices con una distancia.
     * Al ser un grafo NO DIRIGIDO, la conexión va en ambos sentidos.
     */
    public void insertarArista(String origen, String destino, double peso) {
        // Nos aseguramos de que los vértices existan
        insertarVertice(origen);
        insertarVertice(destino);

        // Agregamos la conexión de ida
        adyacencia.get(origen).add(new Arista(destino, peso));
        // Agregamos la conexión de vuelta (grafo no dirigido)
        adyacencia.get(destino).add(new Arista(origen, peso));
    }

    public Map<String, List<Arista>> getAdyacencia() {
        return adyacencia;
    }
}