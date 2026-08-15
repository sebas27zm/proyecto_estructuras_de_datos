package Helpers;

import java.util.List;

public class ResultadoDijkstra {
    private double distanciaTotal;
    private List<String> ruta;
    private boolean alcanzable;

    public ResultadoDijkstra(double distanciaTotal, List<String> ruta, boolean alcanzable) {
        this.distanciaTotal = distanciaTotal;
        this.ruta = ruta;
        this.alcanzable = alcanzable;
    }

    public double getDistanciaTotal() { return distanciaTotal; }
    public List<String> getRuta() { return ruta; }
    public boolean isAlcanzable() { return alcanzable; }
}
