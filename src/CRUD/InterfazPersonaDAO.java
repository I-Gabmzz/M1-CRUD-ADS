package CRUD;

import Elementos.Persona;
import java.util.List;

// Esta interfaz se va a encargar de definir las operaciones del CRUD.

// Esta clase se crea con el objetivo de cumplir y aplicar estos principios:
// 1. ISP (Segregación de Interfaces): Define solo lo necesario.
// 2. OCP (Abierto/Cerrado): Permite crear nuevas implementaciones sin romper el código.


// Se declara la interfaz del DAO para Persona
// Define únicamente las operaciones básicas del CRUD, evitando depender de una clase concreta
public interface InterfazPersonaDAO extends InterfazLecturaDeDatos, InterfazEscrituraDeDatos {
    // Esta interfaz ahora agrupa dos interfaces.
    // Esto permite que si alguien solo quiere leer, use InterfazLecturaDeDatos directamente
    // Sin depender de InterfazEscrituraDeDatos.
    // Y tambien ya no necesita definir los métodos aquí porque los hereda.
}