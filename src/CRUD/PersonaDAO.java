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
        ResultSet rsPersona = null; // Resultado de guardar una persona
        boolean resultado = false; // Resultado del metodo

        try {
            // Busca realizar la conexion con la DB
            conexion = Conexion.hacerConexion();

            // Se agrega una persona a la DB
            String dbPersona = "INSERT INTO Personas (nombre, direccion) VALUES (?, ?)";

            // Con ayuda del RGK es posible saber que ID va a recibir la persona que vamos a añadir
            psPersona = conexion.prepareStatement(dbPersona, Statement.RETURN_GENERATED_KEYS);
            psPersona.setString(1, persona.getNombre());
            psPersona.setString(2, persona.getDireccion());

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
            if (idGenerado > 0 && !persona.getTelefonos().isEmpty()) {
                String dbTelefono = "INSERT INTO Telefonos (personaId, telefono) VALUES (?, ?)";
                psTelefono = conexion.prepareStatement(dbTelefono);

                for (String telefono : persona.getTelefonos()) {
                    psTelefono.setInt(1, idGenerado);
                    psTelefono.setString(2, telefono);
                    psTelefono.executeUpdate();
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
        ResultSet rsPersona = null;
        ResultSet rsTelefono = null;
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
                String direccionDb = rsPersona.getString("direccion");

                // Se construye a la persona con los datos recuperados
                // En este punto se crea exactamente la persona que se consulto pero faltan sus telefonos asignados
                personaEncontrada = new Persona(idDb, nombreDb, direccionDb);
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
            }

        } catch (Exception e) {
            System.out.println("Error al leer persona: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Nuevamente se cierran los recursos para liberar memoria
            try {
                if (rsPersona != null) rsPersona.close();
                if (rsTelefono != null) rsTelefono.close();
                if (psPersona != null) psPersona.close();
                if (psTelefono != null) psTelefono.close();
                if (conexion != null) conexion.close();
            } catch (Exception e) {}
        }
        return personaEncontrada;
    }

}
