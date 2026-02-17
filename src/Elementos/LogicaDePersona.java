package Elementos;

import CRUD.InterfazPersonaDAO;
import CRUD.PersonaDAO;
import Elementos.Persona;
import java.util.List;

// Esta clase se encarga de cumplir con el principio SRP
// Su única responsabilidad es aplicar las reglas de la logica y las validaciones
// Su funcion es separar la interfaz (ControladorDeInterfaz) de los datos (PersonaDAO)
public class LogicaDePersona {
    private InterfazPersonaDAO personaDAO;

    public LogicaDePersona() {
        // Se aplica la implementación del DAO
        this.personaDAO = new PersonaDAO();
    }

    // Metodo para registrar una persona con la implementacion de validaciones lógicas
    public void registrarPersona(String nombre) throws Exception {
        // Validacion 1: Nombre no nulo ni vacio
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }
        // Validacion 2: Longitud minima
        if (nombre.length() < 3) {
            throw new Exception("El nombre es muy corto (mínimo 3 letras).");
        }

        // Si pasa las validaciones, creamos el objeto y llamamos a la capa de datos
        Persona nuevaPersona = new Persona(nombre);

        if (!personaDAO.insertarPersona(nuevaPersona)) {
            throw new Exception("Error al intentar guardar en la base de datos.");
        }
    }

    // Metodo para buscar con validaciones
    public Persona buscarPersona(int id) throws Exception {
        if (id <= 0) throw new Exception("El ID debe ser mayor a 0.");

        Persona p = personaDAO.leerPersonaID(id);
        if (p == null) throw new Exception("Persona no encontrada con el ID: " + id);

        return p;
    }

    // Metodo para actualizar nombre con validaciones
    public void modificarNombrePersona(Persona persona, String nuevoNombre) throws Exception {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new Exception("El nuevo nombre no es válido.");
        }
        persona.setNombre(nuevoNombre);

        if (!personaDAO.actualizarPersona(persona)) {
            throw new Exception("No se pudo actualizar el registro.");
        }
    }

    // Metodo para eliminar
    public void eliminarPersona(int id) throws Exception {
        if (!personaDAO.eliminarPersona(id)) {
            throw new Exception("No se pudo eliminar (posiblemente ID no existe).");
        }
    }

    // Metodo para agregar dirección en donde se valida la direccion
    public void agregarDireccion(Persona persona, String direccion) throws Exception {
        if (direccion == null || direccion.length() < 5) {
            throw new Exception("La dirección es muy corta, sé más específico.");
        }
        persona.agregarDireccion(direccion);
        // Guardamos el cambio
        personaDAO.actualizarPersona(persona);
    }

    // Metodo para agregar teléfono con validacion
    public void agregarTelefono(Persona persona, String telefono) throws Exception {
        if (telefono == null || telefono.length() < 7) {
            throw new Exception("El teléfono parece incompleto.");
        }
        persona.agregarTelefono(telefono);
        personaDAO.actualizarPersona(persona);
    }

    // Metodo para eliminar teléfono
    public void eliminarTelefono(Persona persona, String telefono) throws Exception {
        if (persona.getTelefonos().contains(telefono)) {
            persona.getTelefonos().remove(telefono);
            // Reutilizamos el actualizar del DAO
            if (!personaDAO.actualizarPersona(persona)) {
                throw new Exception("Error al guardar la eliminación del teléfono.");
            }
        } else {
            throw new Exception("El teléfono no pertenece a esta persona.");
        }
    }

    // Metodo para eliminar dirección
    public void eliminarDireccion(Persona persona, String direccion) throws Exception {
        if (persona.getDirecciones().contains(direccion)) {
            persona.getDirecciones().remove(direccion);
            // Reutilizamos el actualizar del DAO
            if (!personaDAO.actualizarPersona(persona)) {
                throw new Exception("Error al guardar la eliminación de la dirección.");
            }
        } else {
            throw new Exception("La dirección no pertenece a esta persona.");
        }
    }

}