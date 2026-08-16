import Cliente.Cliente;
import Producto.Producto;
import Tienda.Tienda;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que aloja la rutina main() y el menu() de consola.
 * Permite la interacción intuitiva con la aplicación de gestión de inventarios.
 */
public class Main {

    public static void main(String[] args) {
        Tienda miTienda = new Tienda();
        menu(miTienda);
    }

    /**
     * Muestra el menú principal en consola y despacha cada opción del usuario
     * hacia el método correspondiente hasta que se seleccione "Salir".
     */
    public static void menu(Tienda tienda) {
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        System.out.println("==================================================");
        System.out.println("  SISTEMA DE GESTIÓN DE INVENTARIOS EN LÍNEA      ");
        System.out.println("==================================================");

        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Insertar Producto al Inventario (Árbol BST)");
            System.out.println("2. Registrar Cliente y llenar Carrito");
            System.out.println("3. Atender siguiente Cliente (Generar Factura)");
            System.out.println("4. Insertar Vértice al Mapa (Grafo)");
            System.out.println("5. Insertar Arista (Conexión) al Mapa (Grafo)");
            System.out.println("6. Salir");

            opcion = leerEnteroSeguro(scanner, "Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    insertarProducto(scanner, tienda);
                    break;
                case 2:
                    registrarCliente(scanner, tienda);
                    break;
                case 3:
                    tienda.atenderSiguienteCliente();
                    break;
                case 4:
                    insertarVerticeManual(scanner, tienda);
                    break;
                case 5:
                    insertarAristaManual(scanner, tienda);
                    break;
                case 6:
                    System.out.println("\nCerrando el sistema... ¡Ejecución finalizada con éxito!");
                    break;
                default:
                    System.out.println("\n[!] Opción inválida. Seleccione un número del 1 al 6.");
            }
        } while (opcion != 6);

        scanner.close();
    }

    private static void insertarProducto(Scanner scanner, Tienda tienda) {
        System.out.println("\n-- INSERTAR PRODUCTO AL INVENTARIO --");
        System.out.print("Ingrese el nombre del producto (Llave de búsqueda): ");
        String nombre = scanner.nextLine();
        double precio = leerDoubleSeguro(scanner, "Ingrese el precio del producto: $");

        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(nombre);
        nuevoProducto.setPrecio(precio);
        nuevoProducto.setCantidad(1);

        tienda.getInventario().insertar(nuevoProducto);
        System.out.println("[+] Producto insertado correctamente en el árbol.");
    }

    /**
     * Registra un nuevo cliente (datos personales, ubicación y carrito de compras)
     * y lo encola en la Cola de Prioridad de atención.
     */
    private static void registrarCliente(Scanner scanner, Tienda tienda) {
        System.out.println("\n-- REGISTRAR CLIENTE EN LA COLA --");
        System.out.print("ID del cliente: "); String id = scanner.nextLine();
        System.out.print("Nombre: "); String nombre = scanner.nextLine();
        System.out.print("Primer Apellido: "); String ap1 = scanner.nextLine();
        System.out.print("Segundo Apellido: "); String ap2 = scanner.nextLine();
        System.out.print("Correo: "); String correo = scanner.nextLine();
        System.out.print("Teléfono: "); String telefono = scanner.nextLine();

        // SELECCIÓN DE UBICACIÓN DESDE MAPA PRECARGADO
        String ubicacion = seleccionarUbicacion(scanner, tienda);

        int prioridad = 0;
        while (prioridad < 1 || prioridad > 3) {
            prioridad = leerEnteroSeguro(scanner, "Prioridad asignada (1-Básico, 2-Afiliado, 3-Premium): ");
            if (prioridad < 1 || prioridad > 3) System.out.println("[!] La prioridad debe ser un entero entre 1 y 3.");
        }

        Cliente nuevoCliente = new Cliente(id, nombre, ap1, ap2, correo, telefono, prioridad, ubicacion);

        System.out.println("\n-- LLENADO DEL CARRITO DESDE EL INVENTARIO --");
        tienda.getInventario().listarProductos();

        boolean comprando = true;
        while (comprando) {
            System.out.print("Ingrese el nombre exacto del producto a comprar (o 'fin' para terminar): ");
            String busqueda = scanner.nextLine();

            if (busqueda.equalsIgnoreCase("fin")) {
                comprando = false;
            } else {
                Producto prodEncontrado = tienda.getInventario().buscar(busqueda);
                if (prodEncontrado != null) {
                    nuevoCliente.agregarProductoAlCarrito(prodEncontrado);
                    System.out.println("[+] Producto '" + prodEncontrado.getNombre() + "' agregado a la ListaProductos personal.");
                } else {
                    System.out.println("[-] Producto no encontrado en el inventario de la Tienda.");
                }
            }
        }

        // Se encola registrando la ubicación en el mapa
        tienda.getColaAtencion().encolar(nuevoCliente, tienda.getMapa());
        System.out.println("[+] Cliente ingresado a la Cola de Prioridad exitosamente.");
    }

    /**
     * Permite al usuario insertar manualmente un nuevo vértice (ubicación) al Grafo.
     */
    private static void insertarVerticeManual(Scanner scanner, Tienda tienda) {
        System.out.println("\n-- INSERTAR VÉRTICE AL MAPA --");
        System.out.print("Nombre de la nueva ubicación: ");
        String nombre = scanner.nextLine();

        if (nombre == null || nombre.isBlank()) {
            System.out.println("[!] El nombre no puede estar vacío.");
            return;
        }

        boolean yaExistia = tienda.getMapa().getAdyacencia().containsKey(nombre);
        tienda.getMapa().insertarVertice(nombre);

        if (yaExistia) {
            System.out.println("[i] La ubicación '" + nombre + "' ya existía en el mapa.");
        } else {
            System.out.println("[+] Vértice '" + nombre + "' agregado correctamente al mapa.");
        }
    }

    /**
     * Permite al usuario insertar manualmente una nueva arista (conexión) entre
     * dos ubicaciones del Grafo, indicando la distancia entre ellas.
     */
    private static void insertarAristaManual(Scanner scanner, Tienda tienda) {
        System.out.println("\n-- INSERTAR ARISTA (CONEXIÓN) AL MAPA --");

        List<String> ubicaciones = new ArrayList<>(tienda.getMapa().getAdyacencia().keySet());
        if (!ubicaciones.isEmpty()) {
            System.out.println("Ubicaciones actuales en el mapa:");
            for (String u : ubicaciones) {
                System.out.println("  - " + u);
            }
        } else {
            System.out.println("[i] El mapa aún no tiene ubicaciones registradas.");
        }

        System.out.print("\nUbicación de origen: ");
        String origen = scanner.nextLine();
        System.out.print("Ubicación de destino: ");
        String destino = scanner.nextLine();

        if (origen.isBlank() || destino.isBlank()) {
            System.out.println("[!] Origen y destino no pueden estar vacíos.");
            return;
        }

        double peso = leerDoubleSeguro(scanner, "Distancia entre ambas ubicaciones (km): ");

        tienda.getMapa().insertarArista(origen, destino, peso);
        System.out.println("[+] Conexión creada: " + origen + " <-> " + destino + " (" + peso + " km).");
    }

    /**
     * Muestra las ubicaciones disponibles en el Grafo y fuerza al usuario
     * a seleccionar una mediante su índice numérico.
     */
    private static String seleccionarUbicacion(Scanner scanner, Tienda tienda) {
        List<String> ubicaciones = new ArrayList<>(tienda.getMapa().getAdyacencia().keySet());

        if (ubicaciones.isEmpty()) {
            System.out.println("[!] No hay ubicaciones registradas en el mapa.");
            return "Desconocido";
        }

        System.out.println("\n--- SELECCIÓN DE UBICACIÓN DE ENTREGA ---");
        for (int i = 0; i < ubicaciones.size(); i++) {
            System.out.printf(" %2d. %s\n", (i + 1), ubicaciones.get(i));
        }

        int seleccion = 0;
        while (seleccion < 1 || seleccion > ubicaciones.size()) {
            seleccion = leerEnteroSeguro(scanner, "Seleccione el número de su ubicación (1-" + ubicaciones.size() + "): ");
            if (seleccion < 1 || seleccion > ubicaciones.size()) {
                System.out.println("[!] Opción fuera de rango. Por favor elija un número de la lista.");
            }
        }

        String ubicacionSeleccionada = ubicaciones.get(seleccion - 1);
        System.out.println("[✓] Ubicación seleccionada: " + ubicacionSeleccionada);
        return ubicacionSeleccionada;
    }

    // --- MÉTODOS DE VALIDACIÓN DE ENTRADA ---

    /**
     * Solicita un entero por consola, repitiendo la petición si la entrada no es válida.
     */
    private static int leerEnteroSeguro(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                int numero = scanner.nextInt();
                scanner.nextLine();
                return numero;
            } catch (InputMismatchException e) {
                System.out.println("[!] Error: Entrada no válida. Debe ingresar un número entero.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Solicita un número decimal no negativo por consola, repitiendo la petición
     * si la entrada no es válida.
     */
    private static double leerDoubleSeguro(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double numero = scanner.nextDouble();
                scanner.nextLine();
                if (numero >= 0) return numero;
                System.out.println("[!] Error: El precio no puede ser negativo.");
            } catch (InputMismatchException e) {
                System.out.println("[!] Error: Entrada no válida. Ingrese un número (ej. 1500 o 1500.50).");
                scanner.nextLine();
            }
        }
    }
}