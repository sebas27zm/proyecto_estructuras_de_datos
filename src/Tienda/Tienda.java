package Tienda;

import Cliente.Cliente;
import Cliente.ColaClientes;
import Arbol.ArbolProductos;
import Grafo.Grafo; // NUEVA IMPORTACIÓN
import Producto.Producto;
import Nodo.Nodo;

/**
 * Clase que gestiona la integración entre el inventario (BST), la cola de prioridad
 * y el mapa de ubicaciones (Grafo).
 */
public class Tienda {
    private ArbolProductos inventario;
    private ColaClientes colaAtencion;
    private Grafo mapa; // NUEVO
    private String ubicacion; // NUEVO: Ubicación física de la Tienda

    public Tienda() {
        this.inventario = new ArbolProductos();
        this.colaAtencion = new ColaClientes();
        this.mapa = new Grafo(); // Inicialización
        this.ubicacion = "Sede Central"; // Vértice de origen predeterminado
        precargarMapa(); // Llama a la precarga de datos solicitada
    }

    /**
     * Genera un conjunto inicial de vértices y aristas para tener un mapa funcional
     * desde que arranca la aplicación.
     */
    private void precargarMapa() {
        mapa.insertarVertice(this.ubicacion);

        // Insertamos rutas predeterminadas (Aristas ponderadas)
        mapa.insertarArista(this.ubicacion, "Punto A", 5.5);
        mapa.insertarArista(this.ubicacion, "Punto B", 12.0);
        mapa.insertarArista("Punto A", "Punto C", 3.2);
        mapa.insertarArista("Punto B", "Punto C", 7.4);
        mapa.insertarArista("Punto C", "Punto D", 2.1);

        System.out.println("[*] Mapa básico de rutas precargado exitosamente en la Tienda.");
    }

    public ArbolProductos getInventario() {
        return inventario;
    }

    public ColaClientes getColaAtencion() {
        return colaAtencion;
    }

    public Grafo getMapa() {
        return mapa;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Atiende al cliente con mayor prioridad en la cola, calcula el costo total
     * de su ListaProductos (carrito) e imprime la factura.
     */
    public void atenderSiguienteCliente() {
        Cliente clienteActual = colaAtencion.desencolar();

        if (clienteActual == null) {
            return; // El método desencolar() avisa si está vacía
        }

        System.out.println("\n=======================================");
        System.out.println("          FACTURA DE COMPRA            ");
        System.out.println("=======================================");
        System.out.println("Cliente: " + clienteActual.getNombre() + " " + clienteActual.getPrimerApellido());
        System.out.println("Ubicación de entrega: " + clienteActual.getUbicacion());
        System.out.println("Prioridad: " + clienteActual.getPrioridad() + " (" + clienteActual.getTipoPrioridadString() + ")");
        System.out.println("---------------------------------------");
        System.out.println("Detalle de productos:");

        double totalAcumulado = 0.0;

        // Recorrido de la ListaProductos (carrito) del cliente para calcular el costo
        Nodo actual = clienteActual.getCarrito().getCabeza();

        if (actual == null) {
            System.out.println("  (El carrito está vacío)");
        } else {
            while (actual != null) {
                Producto p = actual.getProducto();
                // Se asume cantidad 1 por producto agregado para el cálculo básico
                System.out.printf(" - %-20s : $%.2f\n", p.getNombre(), p.getPrecio());
                totalAcumulado += p.getPrecio();
                actual = actual.getSig();
            }
        }

        System.out.println("---------------------------------------");
        System.out.printf("COSTO TOTAL ACUMULADO : $%.2f\n", totalAcumulado);
        System.out.println("=======================================\n");
    }
}