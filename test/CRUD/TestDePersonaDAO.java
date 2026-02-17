package CRUD;

import ConexionDB.Conexion;
import Elementos.Persona;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestDePersonaDAO {

    @Test
    public void testInsertar() {
        // Primera prueba: Se crea la respectiva persona falsa
        PersonaDAO pruebaDePersona = new PersonaDAO();
        Persona personaFake = new Persona("Guillermo Del Toro");
        personaFake.agregarDireccion("Calle UABC");
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
        Persona personaOriginal = new Persona("Read Test");
        personaOriginal.agregarDireccion("Calle wikipedia");
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
        assertEquals("Calle wikipedia", personaRecuperada.getDirecciones().get(0), "Error: La direccion no coincide");
        assertFalse(personaRecuperada.getTelefonos().isEmpty(), "Error: No se recuperaron los telefonos");

        System.out.println("Prueba de lectura exitosa, ya se encontro a: " + personaRecuperada.getNombre());
    }

    @Test
    public void testActualizar() {
        // Nuevamente en este test tambien se crea y se inserta una persona inicial
        PersonaDAO testDAO = new PersonaDAO();
        Persona personaParaEditar = new Persona("Marina Del Pilar");
        personaParaEditar.agregarDireccion("Palacio Municipal");
        personaParaEditar.agregarTelefono("123-456-7890"); // Telefono a actualizar

        testDAO.insertarPersona(personaParaEditar);
        int idObjetivo = personaParaEditar.getId();

        System.out.println("Se inserto a la persona: " + personaParaEditar.getNombre());

        // Ahora en este apartado se cambian los datos de la persona
        personaParaEditar.setNombre("Gobernadora");
        personaParaEditar.getDirecciones().clear();
        personaParaEditar.agregarDireccion("Baja California");

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
        assertEquals("Baja California", personaEnBD.getDirecciones().get(0), "Error: La direccion no se actualizo");
        // Se verifica que el telefono viejo ya no este y este el nuevo
        assertEquals("098-765-4321", personaEnBD.getTelefonos().get(0), "Error: Los telefonos no se actualizaron correctamente");

        System.out.println("Prueba de actualizacion es correcta, el nuevo nombre es: " + personaEnBD.getNombre());
    }

    @Test
    public void testEliminar() {
        // Ahora para finalizar las pruebas de unidad nuevamente se crea una persona temporal
        PersonaDAO testDAO = new PersonaDAO();
        Persona personaParaBorrar = new Persona("EliminarTest");
        personaParaBorrar.agregarDireccion("Calle borrada");
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

    @Test
    public void testIntegracionCicloCompleto() {
        System.out.println("Test de integracion");

        // Primero se crea la persona y el respectivo usuario
        PersonaDAO pDao = new PersonaDAO();
        Persona usuario = new Persona("John Cena");
        usuario.agregarDireccion("Calle WWE");

        // Posteriormente se inserta al usuario
        pDao.insertarPersona(usuario);
        int idGenerado = usuario.getId();

        assertTrue(idGenerado > 0, "Fallo en la integracion");
        System.out.println("Se ha insertado correctamente con el ID: " + idGenerado);

        // Ahora es momento de modificar el usuario por lo tanto, cambiamos el nombre y agregamos telefonos
        usuario.setNombre("Triple H");
        usuario.agregarTelefono("686-777-1234");
        usuario.agregarTelefono("686-888-1234");

        boolean actualizado = pDao.actualizarPersona(usuario);

        assertTrue(actualizado, "Fallo en la integracion");
        System.out.println("La persona ha sido actualizado y enviado a BD");

        // Luego se consulta a la BD para ver si los cambios han ocurrido
        // COMO SE CREA UN NUEVO USUARIOBD ESTE NO SE ELIMINA PARA DEJAR RASTRO DE LOS CAMBIOS HECHOS
        Persona usuarioBD = pDao.leerPersonaID(idGenerado);
        assertEquals("Triple H", usuarioBD.getNombre(), "Fallo en la integracion");

        assertEquals("686-777-1234", usuarioBD.getTelefonos().get(0), "Fallo en la integracion");
        assertEquals("686-888-1234", usuarioBD.getTelefonos().get(1), "Fallo en la integracion");

        System.out.println("Se ha consultado la persona y los datos en la BD coinciden");

        // Por ultimo en el ciclo del programa se elimina la persona y se verifica que este ha sido silenciado
        boolean eliminado = pDao.eliminarPersona(idGenerado);
        assertTrue(eliminado, "Fallo en la integracion");
        assertNull(pDao.leerPersonaID(idGenerado), "Fallo en la integracion");
        System.out.println("El ciclo y la integracion ha finalizado correctamente");
    }

    @Test
    public void testDireccionesCompartidas() {
        PersonaDAO dao = new PersonaDAO();
        // Para comprobar que las direcciones si se comparten correctamente se crean dos personas
        Persona p1 = new Persona("Naruto");
        Persona p2 = new Persona("Goku");

        // Se les asigna la misma direccion a las dos personas
        String direccionComun = "Japon";
        p1.agregarDireccion(direccionComun);
        p2.agregarDireccion(direccionComun);

        // Se insertan las respectivas personas a la db
        dao.insertarPersona(p1);
        dao.insertarPersona(p2);

        // Ahora se cuenta cuantas veces existe la direccion en la tabla Direcciones
        int totalDireccionesReales = 0;
        try {
            java.sql.Connection conn = new Conexion().hacerConexion();
            java.sql.PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM Direcciones WHERE texto_direccion = ?");
            ps.setString(1, direccionComun);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalDireccionesReales = rs.getInt(1);
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }

        // Debería ser 1 solo registro de direccion el cual esta compartido por 2 personas
        assertEquals(1, totalDireccionesReales, "Error: Se duplicó la dirección en lugar de compartirse.");

        System.out.println("Éxito: La dirección se guardó una sola vez y se compartió.");
        dao.eliminarPersona(p1.getId());
        dao.eliminarPersona(p2.getId());
    }

}