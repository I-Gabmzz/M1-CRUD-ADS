package CRUD;

import Elementos.Persona;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestDePersonaDAO {

    @Test
    public void testInsertar() {
        // Primera prueba: Se crea la respectiva persona falsa
        PersonaDAO pruebaDePersona = new PersonaDAO();
        Persona personaFake = new Persona("Guillermo Del Toro", "Calle UABC");
        personaFake.agregarTelefono("123-456-7890");

        // Segunda prueba: Se intenta guardar la personaFake
        boolean seGuardo = pruebaDePersona.insertarPersona(personaFake);

        // Tercera Prueba: Se verifica el resultado de la operacion seGuardo, si recibe true pasa la prueba
        assertTrue(seGuardo, "Error: El método insertar devolvió false");

        // Cuatra Prueba: Se comprueba si el ID cambió (si es mayor a 0, significa que la BD lo registro correctamente)
        if (personaFake.getId() > 0) {
            System.out.println("Todo ha salido correctamente, ya se guardó con el ID: " + personaFake.getId());
        } else {
            fail("Error: La persona no recibió un ID de la base de datos");
        }
    }

    @Test
    public void testLeer() {
        // Primera prueba: Se inserta a alguien para asegurar que exista algo que buscar
        PersonaDAO testDAO = new PersonaDAO();
        Persona personaOriginal = new Persona("Read Test", "Calle wikipedia");
        personaOriginal.agregarTelefono("123-456-7890");

        testDAO.insertarPersona(personaOriginal);
        int idBuscado = personaOriginal.getId(); // Se guarda el ID que se genero

        System.out.println("Intentando leer el ID: " + idBuscado);

        // Segunda Prueba: Se recupera la persona usando el metodo de lectura
        Persona personaRecuperada = testDAO.leerPersonaID(idBuscado);

        // Tercera prueba: Se verifica que si la haya encontrado
        assertNotNull(personaRecuperada, "Error: No se encontro la persona en la DB");

        // Cuarta Prueba: Se comprueba que los datos sean los mismos que se guardaron
        assertEquals("Read Test", personaRecuperada.getNombre(), "Error: El nombre no coincide");
        assertEquals("Calle wikipedia", personaRecuperada.getDireccion(), "Error: La direccion no coincide");
        assertFalse(personaRecuperada.getTelefonos().isEmpty(), "Error: No se recuperaron los telefonos");

        System.out.println("Prueba de lectura exitosa, ya se encontro a: " + personaRecuperada.getNombre());
    }

}