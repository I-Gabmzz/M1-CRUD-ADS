package Main;

// Se importan las librerias necesarias para la ejecucion del programa
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Clase principal para iniciar todo el programa
public class Main extends Application {

    // Este es el metodo principal que configura y muestra la ventana de la aplicacion
    @Override
    public void start(Stage stage) throws IOException {
        // Se carga el archivo que contiene la interfaz y se configura la respectiva ventana
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Interfaz/Interfaz.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Meta 1.2 | AyDS - Sistema CRUD de una Agenda");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}