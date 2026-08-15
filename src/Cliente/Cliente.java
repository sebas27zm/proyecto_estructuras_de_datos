package Cliente;

import ListaProductos.ListaProductos;
import Producto.Producto;

public class Cliente {

    // Atributos del cliente
    private String id;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String correo;
    private String telefono;
    private String ubicacion; // NUEVO: Ubicación en el mapa
    private int prioridad; // 1: Básico, 2: Afiliado, 3: Premium
    private final ListaProductos carrito; // Lista enlazada que almacena los productos

    public Cliente(String id, String nombre, String primerApellido, String segundoApellido,
                   String correo, String telefono, int prioridad, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.correo = correo;
        this.telefono = telefono;
        this.ubicacion = ubicacion; // Asignación del nuevo atributo

        if (prioridad < 1 || prioridad > 3) {
            this.prioridad = 1;
        } else {
            this.prioridad = prioridad;
        }

        // Inicialización de la lista enlazada para el carrito
        this.carrito = new ListaProductos();
    }

    // --- MÉTODOS PARA LA GESTIÓN DEL CARRITO ---
    public void agregarProductoAlCarrito(Producto producto) {
        if (producto != null) {
            this.carrito.insertarAlFinal(producto);
        }
    }

    public ListaProductos getCarrito() {
        return carrito;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getUbicacion() { return ubicacion; } // NUEVO
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; } // NUEVO

    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) {
        if (prioridad >= 1 && prioridad <= 3) {
            this.prioridad = prioridad;
        }
    }

    public String getTipoPrioridadString() {
        return switch (this.prioridad) {
            case 3 -> "Premium";
            case 2 -> "Afiliado";
            default -> "Básico";
        };
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "ID='" + id + '\'' +
                ", Nombre Completo='" + nombre + " " + primerApellido + " " + segundoApellido + '\'' +
                ", Correo='" + correo + '\'' +
                ", Teléfono='" + telefono + '\'' +
                ", Ubicación='" + ubicacion + '\'' +
                ", Prioridad=" + prioridad + " (" + getTipoPrioridadString() + ")" +
                '}';
    }
}