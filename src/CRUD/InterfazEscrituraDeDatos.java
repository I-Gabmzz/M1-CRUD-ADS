package CRUD;

import Elementos.Persona;

// ISP (Segregación de Interfaces): esta interfaz se enfoca solo en operaciones de escritura,
// evitando que otras clases dependan de metodos que no necesitan (como lectura).
public interface InterfazEscrituraDeDatos {

    // Metodo el cual busca insertar una persona
    boolean insertarPersona(Persona persona);

    // Metodo el cual busca actualizar una persona
    boolean actualizarPersona(Persona persona);

    // Metodo el cual busca eliminar una persona por su ID
    boolean eliminarPersona(int id);
}
