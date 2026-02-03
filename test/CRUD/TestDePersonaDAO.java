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

    @Test
    public void testActualizar() {
        // Nuevamente en este test tambien se crea y se inserta una persona inicial
        PersonaDAO testDAO = new PersonaDAO();
        Persona personaParaEditar = new Persona("Marina Del Pilar", "Palacio Municipal");
        personaParaEditar.agregarTelefono("123-456-7890"); // Telefono a actualizar

        testDAO.insertarPersona(personaParaEditar);
        int idObjetivo = personaParaEditar.getId();

        System.out.println("Se inserto a la persona: " + personaParaEditar.getNombre());

        // Ahora en este apartado se cambian los datos de la persona
        personaParaEditar.setNombre("Gobernadora");
        personaParaEditar.setDireccion("Baja California");

        // Se limpia la lista de telefonos asociados que es vieja y se pone una nueva
        personaParaEditar.getTelefonos().clear();
        personaParaEditar.agregarTelefono("098-765-4321"); // Telefono nuevo

        // Se crea una variable para saber si al llamar al metodo actualizar del DAO si funciona
        boolean seActualizo = testDAO.actualizarPersona(personaParaEditar);

        // Se verifica el estado
        assertTrue(seActualizo, "Error: El metodo actualizar devolvio false");

        // Se lee para ver si se efectuaron los cambios y se comprueba cada uno de los cambios.
        Persona personaEnBD = testDAO.leerPersonaID(idObjetivo);
        assertEquals("Gobernadora", personaEnBD.getNombre(), "Error: El nombre no se actualizo en BD");
        assertEquals("Baja California", personaEnBD.getDireccion(), "Error: La direccion no se actualizo");
        // Se verifica que el telefono viejo ya no este y este el nuevo
        assertEquals("098-765-4321", personaEnBD.getTelefonos().get(0), "Error: Los telefonos no se actualizaron correctamente");

        System.out.println("Prueba de actualizacion es correcta, el nuevo nombre es: " + personaEnBD.getNombre());
    }

    @Test
    public void testEliminar() {
        // Ahora para finalizar las pruebas de unidad nuevamente se crea una persona temporal
        PersonaDAO testDAO = new PersonaDAO();
        Persona personaParaBorrar = new Persona("EliminarTest", "Calle borrada");
        testDAO.insertarPersona(personaParaBorrar);
        int idObjetivo = personaParaBorrar.getId();

        // Se llama el metodo para borrar y se verifica que sucedio
        boolean seElimino = testDAO.eliminarPersona(idObjetivo);
        assertTrue(seElimino, "Error: El metodo eliminar devolvio false");

        // Como segunda prueba se intenta buscar a la persona en la BD
        Persona personaBuscada = testDAO.leerPersonaID(idObjetivo);

        // Para finalizar la personaBuscada deberia ser NULL porque ya no existe
        assertNull(personaBuscada, "Error: La persona sigue existiendo en la DB despues de borrarla");

        System.out.println("La prueba de eliminacion fue exitosa, la persona ha sido silenciada...");
    }

}