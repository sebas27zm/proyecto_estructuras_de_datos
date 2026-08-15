package Tienda;

import Cliente.Cliente;
import Cliente.ColaClientes;
import Arbol.ArbolProductos;
import Grafo.Grafo; // NUEVA IMPORTACIÓN
import Helpers.ResultadoDijkstra;
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
        this.ubicacion = "Tienda Central (Tibás)"; // Vértice de origen predeterminado
        this.mapa.precargarMapaInicial(); // Llama a la precarga de datos solicitada
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
        // 1. Consultar el frente de la cola sin desencolar
        Cliente clienteActual = colaAtencion.verFrente();

        if (clienteActual == null) {
            System.out.println("\n[INFO] La cola de atención está vacía.");
            return;
        }

        // 2. Validación de conectividad (nodo aislado)
        if (!validarConectividadCliente(clienteActual)) {
            return; // Cancela el procesamiento y el cliente PERMANECE en la cola
        }

        // 3. Si la validación pasa, se desencola y se procesa la compra
        colaAtencion.desencolar();

        // 4. Cálculo del camino más corto con Dijkstra
        ResultadoDijkstra resultadoRuta = mapa.calcularCaminoMasCorto(this.ubicacion, clienteActual.getUbicacion());

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

    /**
     * Verifica si la ubicación del cliente se encuentra conectada con la tienda.
     * Si está aislada, muestra la alerta en consola y retorna false.
     */
    private boolean validarConectividadCliente(Cliente cliente) {
        if (!mapa.estaConectado(this.ubicacion, cliente.getUbicacion())) {
            System.out.println("\n=======================================================");
            System.out.println("  [ALERTA DE ENVÍO] OPERACIÓN CANCELADA");
            System.out.println("=======================================================");
            System.out.println("Cliente: " + cliente.getNombre() + " " + cliente.getPrimerApellido());
            System.out.println("Ubicación de entrega: " + cliente.getUbicacion());
            System.out.println("Motivo: La ubicación se encuentra DESCONECTADA de la tienda.");
            System.out.println("Sugerencia: Conecte el nodo agregando aristas desde el menú.");
            System.out.println("=======================================================\n");
            return false;
        }

        return true;
    }
}