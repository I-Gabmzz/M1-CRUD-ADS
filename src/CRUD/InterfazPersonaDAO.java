package CRUD;

import Elementos.Persona;
import java.util.List;

// Esta interfaz se va a encargar de definir las operaciones del CRUD.

// Esta clase se crea con el objetivo de cumplir y aplicar estos principios:
// 1. ISP (Segregación de Interfaces): Define solo lo necesario.
// 2. OCP (Abierto/Cerrado): Permite crear nuevas implementaciones sin romper el código.


// Se declara la interfaz del DAO para Persona
// Define únicamente las operaciones básicas del CRUD, evitando depender de una clase concreta
public interface InterfazPersonaDAO {

    // C (CREATE)
    // Metodo el cual busca insertar una persona
    boolean insertarPersona(Persona persona);

    // R (READ)
    // Metodo el cual busca leer una persona por su ID
    Persona leerPersonaID(int id);

    // U (UPDATE)
    // Metodo el cual busca actualizar una persona
    boolean actualizarPersona(Persona persona);

    // D (DELETE)
    // Metodo el cual busca eliminar una persona por su ID
    boolean eliminarPersona(int id);;
}