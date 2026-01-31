package ConexionDB;

// Libreria necesaria para el uso de la DB y la conexion
import java.sql.*;

public class Conexion {

    // Datos de la conexión a la base de datos
    private static final String URL = "jdbc:mariadb://localhost:3306/agenda";
    private static final String USER = "usuario1";
    private static final String PASSWORD = "superpassword";

    // Metodo por el cual se logra la conexion.
    public static Connection hacerConexion() {
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
}
