package CRUD;

// Se importan las librerias necesarias
import java.sql.*;

import ConexionDB.Conexion;
import Elementos.Persona;

// Se declara la creacion de la clase Persona Data Access Object
public class PersonaDAO {

    // C (CREATE)
    // Metodo el cual busca instertar una persona a la DB
    public boolean insertarPersona(Persona persona) {
        // Se declaran las variables necesarias para este metodo
        Connection conexion = null;
        PreparedStatement psPersona = null; // PS para rellenar a una persona
        PreparedStatement psTelefono = null; // PS para rellenar un telefono
        PreparedStatement psDireccion = null; // PS para rellenar una direccion
        ResultSet rsPersona = null; // Resultado de guardar una persona
        boolean resultado = false; // Resultado del metodo

        try {
            // Busca realizar la conexion con la DB
            conexion = Conexion.hacerConexion();

            // Se agrega una persona a la DB
            String dbPersona = "INSERT INTO Personas (nombre) VALUES (?)";

            // Con ayuda del RGK es posible saber que ID va a recibir la persona que vamos a añadir
            psPersona = conexion.prepareStatement(dbPersona, Statement.RETURN_GENERATED_KEYS);
            psPersona.setString(1, persona.getNombre());

            // Se confirma la insercion de la persona
            psPersona.executeUpdate();

            // Ahora se recupera y se vuelve a setear el ID que debe recibir la persona que acabamos de guardar
            rsPersona = psPersona.getGeneratedKeys();
            int idGenerado = 0;
            if (rsPersona.next()) {
                idGenerado = rsPersona.getInt(1);
                persona.setId(idGenerado);
            }

            // Se asignan los telefonos de acorde a la persona recien agregada
            if (idGenerado > 0) {
                if (!persona.getTelefonos().isEmpty()) {
                    String dbTelefono = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
                    psTelefono = conexion.prepareStatement(dbTelefono);

                    for (String telefono : persona.getTelefonos()) {
                        psTelefono.setInt(1, idGenerado);
                        psTelefono.setString(2, telefono);
                        psTelefono.executeUpdate();
                    }
                }

                if (!persona.getDirecciones().isEmpty()) {
                    String dbDireccion = "INSERT INTO Personas_Direcciones (personaId, direccionId) VALUES (?, ?)";
                    psDireccion = conexion.prepareStatement(dbDireccion);

                    for (String direccion : persona.getDirecciones()) {
                        int idDir = obtenerIdDireccion(conexion, direccion);
                        psDireccion.setInt(1, idGenerado);
                        psDireccion.setInt(2, idDir);
                        psDireccion.executeUpdate();
                    }
                }
            }

            resultado = true;

            // si no se logra hacer la conexion.
        } catch (Exception e) {
            System.out.println("Error de conexion: " + e.getMessage());
            throw new RuntimeException(e);
            // Se cierran los recursos que toma la DB para no generar una mala optimizacion.
        } finally {
            try {
                if (rsPersona != null) rsPersona.close();
                if (psPersona != null) psPersona.close();
                if (psTelefono != null) psTelefono.close();
                if (psDireccion != null) psDireccion.close();
                if (conexion != null) conexion.close();
            } catch (Exception e) {
            }
        }
        return resultado;
    }

    // R (READ)
    // Metodo para buscar una persona por su ID y recuperar sus telefonos
    public Persona leerPersonaID(int id) {
        // Se declaran las variables necesarias
        Connection conexion = null;
        PreparedStatement psPersona = null;
        PreparedStatement psTelefono = null;
        PreparedStatement psDireccion = null;
        ResultSet rsPersona = null;
        ResultSet rsTelefono = null;
        ResultSet rsDireccion = null;
        Persona personaEncontrada = null; // Aqui se guarda el resultado final

        try {
            // Busca realizar la conexion con la DB
            conexion = Conexion.hacerConexion();

            // En este apartado se buscan los datos principales en la tabla Personas
            String dbBuscar = "SELECT * FROM Personas WHERE id = ?";
            psPersona = conexion.prepareStatement(dbBuscar);
            psPersona.setInt(1, id); // Se asigna el ID objetivo al ?

            rsPersona = psPersona.executeQuery();

            // Si existe una fila con ese ID, se crea el objeto
            if (rsPersona.next()) {
                // Se recuperan los datos de las columnas de la DB
                int idDb = rsPersona.getInt("id");
                String nombreDb = rsPersona.getString("nombre");

                // Se construye a la persona con los datos recuperados
                // En este punto se crea exactamente la persona que se consulto pero faltan sus telefonos asignados
                personaEncontrada = new Persona(idDb, nombreDb);
            }

            // Proceso para encontrar los telefonos asignados a la persona creada
            if (personaEncontrada != null) {
                String dbTel = "SELECT telefono FROM Telefonos WHERE personaId = ?";
                psTelefono = conexion.prepareStatement(dbTel);
                psTelefono.setInt(1, id);

                rsTelefono = psTelefono.executeQuery();

                // Recorremos todos los telefonos que encuentre (porque pueden ser varios)
                while (rsTelefono.next()) {
                    String numero = rsTelefono.getString("telefono");
                    // Se agrega el numero a la lista de la persona encontrada
                    personaEncontrada.agregarTelefono(numero);
                }

                String dbDir = "SELECT d.texto_direccion FROM Direcciones d JOIN Personas_Direcciones pd ON d.id = pd.direccionId WHERE pd.personaId = ?";
                psDireccion = conexion.prepareStatement(dbDir);
                psDireccion.setInt(1, id);

                rsDireccion = psDireccion.executeQuery();

                while (rsDireccion.next()) {
                    String dirTexto = rsDireccion.getString("texto_direccion");
                    personaEncontrada.agregarDireccion(dirTexto);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al leer persona: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Nuevamente se cierran los recursos para liberar memoria
            try {
                if (rsPersona != null) rsPersona.close();
                if (rsTelefono != null) rsTelefono.close();
                if (rsDireccion != null) rsDireccion.close();
                if (psPersona != null) psPersona.close();
                if (psTelefono != null) psTelefono.close();
                if (psDireccion != null) psDireccion.close();
                if (conexion != null) conexion.close();
            } catch (Exception e) {}
        }
        return personaEncontrada;
    }

    // U (UPDATE)
    // Metodo para actualizar los datos de una persona y su lista de telefonos
    public boolean actualizarPersona(Persona persona) {
        Connection conexion = null;
        PreparedStatement psActualizar = null;
        PreparedStatement psBorrarTel = null;
        PreparedStatement psInsertarTel = null;
        PreparedStatement psBorrarDir = null;
        PreparedStatement psInsertarDir = null;
        boolean resultado = false;

        try {
            conexion = Conexion.hacerConexion();

            // Primero se actualizan los datos clave tanto nombre como direccion
            String dbActualizar = "UPDATE Personas SET nombre = ? WHERE id = ?";
            psActualizar = conexion.prepareStatement(dbActualizar);
            psActualizar.setString(1, persona.getNombre());
            psActualizar.setInt(2, persona.getId());

            // Posteriormente se ejecuta la actualizacion, en este caso si se devuelve un 0, significa que el ID no existe.
            int filasAfectadas = psActualizar.executeUpdate();

            // Si se encontro la persona y se actualizo, se procede con los telefonos
            if (filasAfectadas > 0) {

                // Se borran todos los numeros asociados anteriormente para evitar errores
                String dbBorrarTel = "DELETE FROM Telefonos WHERE personaId = ?";
                psBorrarTel = conexion.prepareStatement(dbBorrarTel);
                psBorrarTel.setInt(1, persona.getId());
                psBorrarTel.executeUpdate();

                // Nuevamente se vuelven a meter los telefonos
                if (!persona.getTelefonos().isEmpty()) {
                    String dbInsertarTel = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
                    psInsertarTel = conexion.prepareStatement(dbInsertarTel);

                    for (String telefono : persona.getTelefonos()) {
                        psInsertarTel.setInt(1, persona.getId());
                        psInsertarTel.setString(2, telefono);
                        psInsertarTel.executeUpdate();
                    }
                }

                String dbBorrarDir = "DELETE FROM Personas_Direcciones WHERE personaId = ?";
                psBorrarDir = conexion.prepareStatement(dbBorrarDir);
                psBorrarDir.setInt(1, persona.getId());
                psBorrarDir.executeUpdate();

                if (!persona.getDirecciones().isEmpty()) {
                    String dbInsertarDir = "INSERT INTO Personas_Direcciones (personaId, direccionId) VALUES (?, ?)";
                    psInsertarDir = conexion.prepareStatement(dbInsertarDir);

                    for (String direccion : persona.getDirecciones()) {
                        int idDir = obtenerIdDireccion(conexion, direccion);
                        psInsertarDir.setInt(1, persona.getId());
                        psInsertarDir.setInt(2, idDir);
                        psInsertarDir.executeUpdate();
                    }
                }

                resultado = true;
            }

        } catch (Exception e) {
            System.out.println("Error al actualizar persona: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Como se ha hecho anteriormente despues de cada proceso, se liberan los recursos utilizados
            try {
                if (psActualizar != null) psActualizar.close();
                if (psBorrarTel != null) psBorrarTel.close();
                if (psInsertarTel != null) psInsertarTel.close();
                if (psBorrarDir != null) psBorrarDir.close();
                if (psInsertarDir != null) psInsertarDir.close();
                if (conexion != null) conexion.close();
            } catch (Exception e) {}
        }
        return resultado;
    }

    // D (DELETE)
    // Metodo para eliminar una persona de la base de datos utilizando mediante su ID
    public boolean eliminarPersona(int id) {
        Connection conexion = null;
        PreparedStatement psEliminar = null;
        boolean resultado = false;

        try {
            conexion = Conexion.hacerConexion();

            // Se prepara la accion de la DB para borrar pero aqui sucede algo particular
            // al borrar la persona, sus telefonos se eliminan automaticamente.
            // esto succede porque se utiliza ON DELETE CASCADE que borra la tabla padre
            String dbEliminar = "DELETE FROM Personas WHERE id = ?";
            psEliminar = conexion.prepareStatement(dbEliminar);
            psEliminar.setInt(1, id);

            // Se ejecuta la instruccion de eliminar
            int filasAfectadas = psEliminar.executeUpdate();

            if (filasAfectadas > 0) {
                resultado = true;
            }

        } catch (Exception e) {
            System.out.println("Error al eliminar persona: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Se cierran los recursos
            try {
                if (psEliminar != null) psEliminar.close();
                if (conexion != null) conexion.close();
            } catch (Exception e) {}
        }
        return resultado;
    }

    // Metodo auxiliar para manejar la logica de direcciones compartidas
    // Devuelve el ID de la direccion (ya sea nueva o existente)
    private int obtenerIdDireccion(Connection conn, String textoDireccion) throws SQLException {
        int idDireccion = -1;

        // Lo primero que se realiza es buscar si ya existe esa direccion
        String sqlBuscar = "SELECT id FROM Direcciones WHERE texto_direccion = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlBuscar)) {
            ps.setString(1, textoDireccion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }

        // En caso de que la direccion no exista, esta se va a crear
        String sqlInsertar = "INSERT INTO Direcciones (texto_direccion) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, textoDireccion);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idDireccion = rs.getInt(1);
            }
        }
        return idDireccion;
    }

}