package ConexionDB;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeConexion {
    @Test
    public void testParaConexionExitosa() {
        // Intentamos obtener la conexión usando la clase de conexion y su respectivo metodo
        Connection conexion = new Conexion().hacerConexion();

        // Se plantea el primer escenario, en donde si falla la conexion esta devolvera null, pero no debe de ser asi.
        assertNotNull(conexion, "La conexión falló y devolvió null");

        try {
            // Como segundo escenario de prueba se plantea que si la conexion se cierra inesperadamente se va a cerrar la conexion.
            assertFalse(conexion.isClosed(), "La conexión se cerró inesperadamente");
            conexion.close();

            // Si succede alguno de estos errores lo anunciara
        } catch (SQLException e) {
            fail("Ocurrió un error durante la prueba: " + e.getMessage());
        }
    }
}
