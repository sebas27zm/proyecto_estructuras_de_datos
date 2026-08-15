package Grafo;

import Helpers.NodoDijkstra;
import Helpers.ResultadoDijkstra;

import java.util.*;

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

    /**
     * Precarga inicial del mapa con ubicaciones principales de la GAM
     * como referencia para nuestros clientes
     */
    public void precargarMapaInicial() {
        // 1. Nodos Principales y Centros de Distribución
        insertarVertice("Tienda Central (Tibás)");
        insertarVertice("Distribuidor Lindora");
        insertarVertice("Distribuidor Curridabat");
        insertarVertice("San José Centro");
        insertarVertice("Heredia Centro");
        insertarVertice("Alajuela Centro");
        insertarVertice("Cartago Centro");

        // 2. Cantones y Distritos para Clientes
        insertarVertice("Escazú");
        insertarVertice("Santa Ana");
        insertarVertice("Desamparados");
        insertarVertice("San Pedro");
        insertarVertice("Moravia");
        insertarVertice("Guadalupe");
        insertarVertice("Pavas");
        insertarVertice("Santo Domingo");
        insertarVertice("Barva");
        insertarVertice("San Joaquín");
        insertarVertice("Belén");
        insertarVertice("Tres Ríos");
        insertarVertice("El Coyol");

        // 3. Red Troncal Principal (Conexiones entre Sedes en km)
        insertarArista("Tienda Central (Tibás)", "San José Centro", 3.5);
        insertarArista("Tienda Central (Tibás)", "Heredia Centro", 8.0);
        insertarArista("Tienda Central (Tibás)", "Distribuidor Curridabat", 8.5);
        insertarArista("San José Centro", "Distribuidor Curridabat", 6.5);
        insertarArista("San José Centro", "Distribuidor Lindora", 14.0);
        insertarArista("Heredia Centro", "Distribuidor Lindora", 10.0);
        insertarArista("Heredia Centro", "Alajuela Centro", 12.0);
        insertarArista("Distribuidor Lindora", "Alajuela Centro", 11.0);
        insertarArista("Distribuidor Curridabat", "Cartago Centro", 12.0);

        // 4. Conexiones a Cantones y Distritos (Distancias reales aproximadas)
        // Zona Tibás / Moravia / Guadalupe
        insertarArista("Tienda Central (Tibás)", "Moravia", 3.0);
        insertarArista("Tienda Central (Tibás)", "Guadalupe", 3.5);

        // Zona San José Centro
        insertarArista("San José Centro", "Desamparados", 5.0);
        insertarArista("San José Centro", "Pavas", 6.0);
        insertarArista("San José Centro", "Escazú", 7.0);

        // Zona Lindora / Santa Ana / Belén
        insertarArista("Distribuidor Lindora", "Santa Ana", 3.0);
        insertarArista("Distribuidor Lindora", "Belén", 6.0);
        insertarArista("Escazú", "Santa Ana", 5.0); // Conexión entre cantones vecinos

        // Zona Curridabat / Cartago
        insertarArista("Distribuidor Curridabat", "San Pedro", 2.5);
        insertarArista("Distribuidor Curridabat", "Tres Ríos", 4.5);

        // Zona Heredia
        insertarArista("Heredia Centro", "Santo Domingo", 4.0);
        insertarArista("Heredia Centro", "Barva", 3.5);
        insertarArista("Heredia Centro", "San Joaquín", 5.0);

        // Zona Alajuela
        insertarArista("Alajuela Centro", "El Coyol", 7.5);
        insertarArista("Alajuela Centro", "Belén", 9.0);

        System.out.println("[*] Mapa ampliado de rutas precargado exitosamente!");
    }

    /**
     * Algoritmo de Dijkstra para encontrar el camino más corto entre dos vértices.
     */
    public ResultadoDijkstra calcularCaminoMasCorto(String origen, String destino) {
        if (!adyacencia.containsKey(origen) || !adyacencia.containsKey(destino)) {
            return new ResultadoDijkstra(Double.POSITIVE_INFINITY, new java.util.ArrayList<>(), false);
        }

        Map<String, Double> distancias = new HashMap<>();
        Map<String, String> predecesores = new HashMap<>();
        PriorityQueue<NodoDijkstra> cola = new PriorityQueue<>();
        Set<String> visitados = new HashSet<>();

        for (String vertice : adyacencia.keySet()) {
            distancias.put(vertice, Double.POSITIVE_INFINITY);
        }
        distancias.put(origen, 0.0);
        cola.add(new NodoDijkstra(origen, 0.0));

        while (!cola.isEmpty()) {
            NodoDijkstra actual = cola.poll();
            String verticeActual = actual.getVertice();

            if (visitados.contains(verticeActual)) continue;
            visitados.add(verticeActual);

            if (verticeActual.equals(destino)) break;

            for (Arista arista : adyacencia.get(verticeActual)) {
                String verticeVecino = arista.getDestino();
                if (!visitados.contains(verticeVecino)) {
                    double nuevaDist = distancias.get(verticeActual) + arista.getPeso();
                    if (nuevaDist < distancias.get(verticeVecino)) {
                        distancias.put(verticeVecino, nuevaDist);
                        predecesores.put(verticeVecino, verticeActual);
                        cola.add(new NodoDijkstra(verticeVecino, nuevaDist));
                    }
                }
            }
        }

        // Validación de nodo aislado: si la distancia se mantiene infinita no hay conexión
        if (distancias.get(destino) == Double.POSITIVE_INFINITY) {
            return new ResultadoDijkstra(Double.POSITIVE_INFINITY, new java.util.ArrayList<>(), false);
        }

        // Reconstrucción del camino trazado
        java.util.LinkedList<String> camino = new java.util.LinkedList<>();
        String paso = destino;
        while (paso != null) {
            camino.addFirst(paso);
            paso = predecesores.get(paso);
        }

        return new ResultadoDijkstra(distancias.get(destino), camino, true);
    }

    /**
     * Retorna true si existe al menos una ruta conectada entre dos ubicaciones.
     */
    public boolean estaConectado(String origen, String destino) {
        return calcularCaminoMasCorto(origen, destino).isAlcanzable();
    }
}