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
            System.out.println("Todo salio bien. Se guardó con el ID: " + personaFake.getId());
        } else {
            fail("Error: La persona no recibió un ID de la base de datos");
        }
    }
}