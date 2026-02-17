package ConexionDB;

import java.sql.Connection;

// Esta clase se crea con el objetivo de cumplir y aplicar estos principios:


// Interfaz para estandarizar la conexion a la base de datos
// La idea es que otras clases dependan de esta interfaz y no de una conexion estatica o concreta
public interface InterfazConexion {

    // Metodo el cual se encarga de realizar/abrir la conexion a la BD
    Connection hacerConexion();

    // Metodo el cual se encarga de cerrar la conexion recibida
    void cerrarConexion(Connection conexion);
}