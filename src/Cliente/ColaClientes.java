package Cliente;

import Grafo.Grafo;

import java.util.ArrayList;

/**
 * Clase que gestiona la cola de prioridad para la atención de clientes.
 * La atención se rige por:
 * 1. Mayor prioridad primero (3: Premium > 2: Afiliado > 1: Básico).
 * 2. En caso de empate en prioridad, gana el cliente con mayor tiempo en la cola (FIFO).
 */
public class ColaClientes {

    private ArrayList<Cliente> clientes;

    public ColaClientes() {
        this.clientes = new ArrayList<>();
    }

    public boolean estaVacia() {
        return clientes.isEmpty();
    }

    public void encolar(Cliente cliente, Grafo grafo) {
        if (cliente == null) {
            return;
        }

        // Registro automático de la ubicación en el grafo (Fase 2)
        if (grafo != null && cliente.getUbicacion() != null) {
            grafo.insertarVertice(cliente.getUbicacion());
        }

        // Si la cola está vacía, simplemente se agrega al inicio
        if (estaVacia()) {
            clientes.add(cliente);
            return;
        }

        // Si no está vacía, buscamos la posición correcta de inserción
        int posicion = 0;
        while (posicion < clientes.size()) {
            // Si la prioridad del cliente nuevo es MAYOR que la del cliente en la posición actual,
            // encontramos su lugar (debe ir antes que él).
            if (cliente.getPrioridad() > clientes.get(posicion).getPrioridad()) {
                break;
            }
            // Si la prioridad es menor o igual, avanzamos (mantiene el desempate por orden de llegada)
            posicion++;
        }

        // Insertamos el cliente en la posición calculada
        clientes.add(posicion, cliente);
    }

    //Atiende al primer cliente en prioridad y luego lo elimina de la cola
    public Cliente desencolar() {
        if (estaVacia()) {
            System.out.println("No hay clientes en la cola de atención.");
            return null;
        }
        // El cliente con mayor prioridad siempre estará en la primera posición (índice 0)
        return clientes.removeFirst();
    }

    //Obtiene el primer cliente en prioridad
    public Cliente verFrente() {
        if (estaVacia()) {
            System.out.println("La cola de clientes está vacía.");
            return null;
        }
        return clientes.getFirst();
    }

    public int getTamanio() {
        return clientes.size();
    }

    public void mostrarCola() {
        if (estaVacia()) {
            System.out.println("La cola está vacía.");
            return;
        }

        System.out.println("\n--- ESTADO DE LA COLA DE ATENCIÓN ---");
        for (int i = 0; i < clientes.size(); i++) {
            Cliente c = clientes.get(i);
            System.out.println((i + 1) + ". " + c.getNombre() + " " + c.getPrimerApellido() +
                    " | Prioridad: " + c.getPrioridad() + " (" + c.getTipoPrioridadString() + ")");
        }
        System.out.println("-------------------------------------\n");
    }
}