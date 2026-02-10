package Elementos;

// Se importan las librerias necesarias para la clase.
import java.util.ArrayList;

public class Persona {
    private int id; // Atributo para identificar la persona
    private String nombre; // Nombre de la persona
    private ArrayList<String> direcciones; // Direcciones de la persona
    private ArrayList<String> telefonos; // Lista de telefonos que tiene esa persona

    // Se crea el constructor de Persona que no asigna una ID a la persona.
    public Persona(String nombre) {
        this.nombre = nombre;
        this.direcciones = new ArrayList<>();
        this.telefonos = new ArrayList<>();
    }

    // Segundo metodo constructor el cual posee todos los datos de la persona.
    public Persona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.direcciones = new ArrayList<>();
        this.telefonos = new ArrayList<>();
    }

    // Metodo que sirve para agregar un telefono a la lista de telefonos.
    public void agregarTelefono(String telefono) {
        this.telefonos.add(telefono);
    }

    // Metodo que sirve para agregar una direccion a la lista de DIRECCIONES.
    public void agregarDireccion(String direccion) {
        if (!this.direcciones.contains(direccion)) {
            this.direcciones.add(direccion);
        }
    }

    // Getter y Setters de cada atributo
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public ArrayList<String> getDirecciones() { return direcciones; }
    public void setDirecciones(ArrayList<String> direcciones) { this.direcciones = direcciones; }

    public ArrayList<String> getTelefonos() { return telefonos; }
    public void setTelefonos(ArrayList<String> telefonos) { this.telefonos = telefonos; }

}
