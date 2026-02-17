package ConexionDB;

// Libreria necesaria para el uso de la DB y la conexion
import Elementos.Persona;

import java.sql.*;
import java.util.*;

public class Conexion implements InterfazConexion {

    // Datos de la conexión a la base de datos
    private static final String URL = "jdbc:mariadb://localhost:3306/agenda";
    private static final String USER = "usuario1";
    private static final String PASSWORD = "superpassword";

    @Override
    // Metodo por el cual se logra la conexion.
    public Connection hacerConexion() {
        Connection conexion = null;
        try {
            // Se registra el driver para que se asegure la libreria.
            Class.forName("org.mariadb.jdbc.Driver");
            // Se establece la conexion mediante ayuda del driver y los datos de la DB
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        // Si existe error con el driver
        } catch (ClassNotFoundException e) {
            System.out.println("Error al conectar con el driver");
            e.printStackTrace();
        // Si existe error con la conexion
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos");
            e.printStackTrace();        }
        return conexion;
    }

    @Override
    public void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metodo que recupera todos los registros de personas al iniciar la aplicacion
    public static List<Persona> cargarBaseDeDatos() {
        // Lista donde se guardaran las personas recuperadas
        List<Persona> lista = new ArrayList<>();
        Connection conexion = null;
        Statement declaracion = null;
        ResultSet resultado = null;

        try {
            // Se inicia la conexion
            conexion = new Conexion().hacerConexion();

            // Solicitud de la DB para seleccionar todas las filas de la tabla Personas
            String solicitudDB = "SELECT * FROM Personas";
            declaracion = conexion.createStatement();
            resultado = declaracion.executeQuery(solicitudDB);

            // Se recorren los resultados obtenidos uno por uno
            while (resultado.next()) {
                // Se crea un objeto Persona con los datos de la fila actual
                Persona persona = new Persona(
                        resultado.getInt("id"),
                        resultado.getString("nombre")
                );
                // Se agrega la persona a la lista final
                lista.add(persona);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Se cierran los recursos para liberar memoria
            try {
                if (resultado != null) resultado.close();
                if (declaracion != null) declaracion.close();
                if (conexion != null) conexion.close();
            } catch (Exception e) {}
        }
        // Se devuelve la lista completa
        return lista;
    }
}
