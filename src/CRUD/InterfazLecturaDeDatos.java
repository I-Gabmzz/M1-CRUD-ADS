package CRUD;

import Elementos.Persona;

// ISP (Segregación de Interfaces): esta interfaz se enfoca solo en lectura de datos,
// evitando que una clase que solo consulta dependa tambien de metodos de escritura.
public interface InterfazLecturaDeDatos {

    // Metodo el cual busca leer una persona por su ID
    Persona leerPersonaID(int id);
}
