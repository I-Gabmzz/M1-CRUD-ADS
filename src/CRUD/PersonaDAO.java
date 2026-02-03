package CRUD;

// Se importan las librerias necesarias
import java.sql.*;
import java.util.*;
import ConexionDB.Conexion;
import Elementos.Persona;

// Se declara la creacion de la clase Persona Data Access Object
public class PersonaDAO {

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

}
