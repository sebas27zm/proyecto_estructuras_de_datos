package Helpers;

public class NodoDijkstra implements Comparable<NodoDijkstra> {
    private String vertice;
    private double distancia;

    public NodoDijkstra(String vertice, double distancia) {
        this.vertice = vertice;
        this.distancia = distancia;
    }

    public String getVertice() {
        return vertice;
    }

    public double getDistancia() {
        return distancia;
    }

    @Override
    public int compareTo(NodoDijkstra otro) {
        return Double.compare(this.distancia, otro.distancia);
    }
}