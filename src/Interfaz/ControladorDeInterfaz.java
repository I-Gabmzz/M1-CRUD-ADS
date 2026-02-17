package Interfaz;

import ConexionDB.Conexion;
import CRUD.PersonaDAO;
import CRUD.InterfazPersonaDAO;
import Elementos.LogicaDePersona;
import Elementos.Persona;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.net.URL;
import java.util.*;

// Clase que maneja la logica de la interfaz grafica
public class ControladorDeInterfaz implements Initializable {

    // Tabla de personas y sus respectivas columnas
    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, Integer> columnaId;
    @FXML private TableColumn<Persona, String> columnaNombre;

    // Tabla de telefonos y su columna
    @FXML private TableView<String> tablaTelefonos;
    @FXML private TableColumn<String, String> columnaNumero;

    // Tabla de direcciones y su columna
    @FXML private TableView<String> tablaDirecciones;
    @FXML private TableColumn<String, String> columnaDireccionDetalle;

    private LogicaDePersona logicaDePersona;
    private ObservableList<Persona> listaPersonasInterfaz;
    private ObservableList<String> listaTelefonosInterfaz;
    private ObservableList<String> listaDireccionesInterfaz;

    // Metodo de inicializacion que se ejecuta automaticamente al abrir la ventana
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Se inicializan el gestor de datos y las listas vacias
        logicaDePersona = new LogicaDePersona(); // Para aplicar el principio SRP se conecta con el DAO
        listaPersonasInterfaz = FXCollections.observableArrayList();
        listaTelefonosInterfaz = FXCollections.observableArrayList();
        listaDireccionesInterfaz = FXCollections.observableArrayList();
        configurarTablas();

        // Se obtienen todos los registros de la base de datos y se agregan a la lista
        listaPersonasInterfaz.addAll(Conexion.cargarBaseDeDatos());

        // Se agrega un listener para detectar cuando el usuario selecciona una fila en la tabla de personas
        tablaPersonas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccion) -> {
            if (seleccion != null) {
                // Si se selecciona a alguien, se cargan sus telefonos y direcciones
                cargarDatosDetalladosDe(seleccion);
            } else {
                // Si se deselecciona, se limpian las tablas de datos
                listaTelefonosInterfaz.clear();
                listaDireccionesInterfaz.clear();
            }
        });
    }

    // Metodo para vincular los datos de las personas con las columnas
    private void configurarTablas() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Configuracion para la columna de telefonos y direcciones
        columnaNumero.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue()));
        columnaDireccionDetalle.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue()));

        // Se asignan las listas a los componentes de la interfaz
        tablaPersonas.setItems(listaPersonasInterfaz);
        tablaTelefonos.setItems(listaTelefonosInterfaz);
        tablaDirecciones.setItems(listaDireccionesInterfaz);
    }

    // Metodo para obtener y mostrar los telefonos y direcciones de una persona seleccionada
    private void cargarDatosDetalladosDe(Persona persona) {
        listaTelefonosInterfaz.clear();
        listaDireccionesInterfaz.clear();
        try {
            // Ahora se usa buscarPersona de la lógica
            Persona personaCompleta = logicaDePersona.buscarPersona(persona.getId());

            if (personaCompleta != null) {
                if (personaCompleta.getTelefonos() != null) {
                    listaTelefonosInterfaz.addAll(personaCompleta.getTelefonos());
                    persona.setTelefonos(personaCompleta.getTelefonos());
                }
                if (personaCompleta.getDirecciones() != null) {
                    listaDireccionesInterfaz.addAll(personaCompleta.getDirecciones());
                    persona.setDirecciones(personaCompleta.getDirecciones());
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudieron cargar detalles: " + e.getMessage());
        }
    }

    // Accion para crear una nueva persona
    @FXML
    void accionBotonAñadirPersona(ActionEvent event) {
        // Se crea una ventana emergente
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Nueva Persona");
        dialogo.setHeaderText("Crear nuevo contacto");
        dialogo.setContentText("Nombre completo:");

        Optional<String> resultado = dialogo.showAndWait();
        resultado.ifPresent(nombre -> {
            try {
                // Se aplica el principio de SRP
                logicaDePersona.registrarPersona(nombre);

                // Si la línea de arriba no falla, se actualiza la tabla
                mostrarMensaje("Éxito", "Persona guardada correctamente.");
                actualizarTabla();

            } catch (Exception e) {
                mostrarMensaje("Error de Validación", e.getMessage());
            }
        });
    }

    // Accion para modificar los datos de una persona existente
    @FXML
    void accionBotonModificarPersona(ActionEvent event) {
        // Se verifica que haya una persona seleccionada
        Persona personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (personaSeleccionada == null) { mostrarMensaje("Error", "Selecciona a alguien primero"); return; }

        // Se configura la ventana de modificacion
        TextInputDialog dialogo = new TextInputDialog(personaSeleccionada.getNombre());
        dialogo.setTitle("Editar");
        dialogo.setHeaderText("Modificar nombre de ID: " + personaSeleccionada.getId());
        dialogo.setContentText("Nuevo nombre:");

        Optional<String> resultado = dialogo.showAndWait();
        resultado.ifPresent(nuevoNombre -> {
            try {
                // Ahora se usa la logica y atrapamos errores
                logicaDePersona.modificarNombrePersona(personaSeleccionada, nuevoNombre);

                // Si no fallo, refrescamos la tabla visualmente
                tablaPersonas.refresh();
                mostrarMensaje("Éxito", "Nombre actualizado correctamente.");

            } catch (Exception e) {
                mostrarMensaje("Error al modificar", e.getMessage());
            }
        });
    }

    // Accion para buscar una persona por su ID
    @FXML
    void accionBotonBuscarPersona(ActionEvent event) {
        // Se solicita el ID mediante una ventana emergente
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Buscar");
        dialogo.setHeaderText("Buscar por ID");
        dialogo.setContentText("ID:");
        Optional<String> resultado = dialogo.showAndWait();

        resultado.ifPresent(textoId -> {
            try {
                int id = Integer.parseInt(textoId);

                // La logica busca o lanza excepción si no encuentra
                Persona encontrada = logicaDePersona.buscarPersona(id);

                // Seleccionar en tabla (Lógica visual)
                boolean estabaEnLista = false;
                for (Persona p : listaPersonasInterfaz) {
                    if (p.getId() == id) {
                        tablaPersonas.getSelectionModel().select(p);
                        tablaPersonas.scrollTo(p);
                        estabaEnLista = true;
                        break;
                    }
                }
                if (!estabaEnLista) {
                    listaPersonasInterfaz.add(encontrada);
                    tablaPersonas.getSelectionModel().select(encontrada);
                }

            } catch (NumberFormatException e) {
                mostrarMensaje("Error", "El ID debe ser un número.");
            } catch (Exception e) {
                mostrarMensaje("No encontrado", e.getMessage());
            }
        });
    }

    // Accion para eliminar una persona
    @FXML
    void accionBotonEliminarPersona(ActionEvent event) {
        Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();
        if (persona == null) return;

        // Se pide confirmacion al usuario
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar");
        alerta.setHeaderText("¿Borrar a " + persona.getNombre() + "?");
        alerta.setContentText("Se borraran tambien sus telefonos y direcciones.");

        if (alerta.showAndWait().get() == ButtonType.OK) {
            try {
                // se delega la logica
                logicaDePersona.eliminarPersona(persona.getId());

                // se actualiza la UI
                listaPersonasInterfaz.remove(persona);
                listaTelefonosInterfaz.clear();
                listaDireccionesInterfaz.clear();
                mostrarMensaje("Éxito", "Persona eliminada.");

            } catch (Exception e) {
                mostrarMensaje("Error al eliminar", e.getMessage());
            }
        }
    }
    // Accion para agregar un telefono a la persona seleccionada
    @FXML
    void accionBotonAñadirTelefono(ActionEvent event) {
        Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();
        if (persona == null) { mostrarMensaje("Error", "Selecciona una persona"); return; }

        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Nuevo Telefono");
        dialogo.setHeaderText("Agregar a: " + persona.getNombre());
        dialogo.setContentText("Numero:");

        Optional<String> resultado = dialogo.showAndWait();
        resultado.ifPresent(numero -> {
            try {
                // Ahora la logica valida (longitud, etc) y guarda
                logicaDePersona.agregarTelefono(persona, numero);

                // Recargamos detalles visuales
                cargarDatosDetalladosDe(persona);

            } catch (Exception e) {
                mostrarMensaje("Error de validación", e.getMessage());
            }
        });
    }

    // Accion para eliminar un telefono especifico
    @FXML
    void accionBotonEliminarTelefono(ActionEvent event) {
        Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();
        String numero = tablaTelefonos.getSelectionModel().getSelectedItem();

        if (persona == null || numero == null) {
            mostrarMensaje("Error", "Selecciona un numero");
            return;
        }

        try {
            // Se elimina el telefono
            logicaDePersona.eliminarTelefono(persona, numero);
            cargarDatosDetalladosDe(persona);

        } catch (Exception e) {
            mostrarMensaje("Error", e.getMessage());
        }
    }

    // Accion para agregar una direccion
    @FXML
    void accionBotonAñadirDireccion(ActionEvent event) {
        Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();
        if (persona == null) { mostrarMensaje("Error", "Selecciona una persona"); return; }

        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Nueva Direccion");
        dialogo.setHeaderText("Agregar direccion a: " + persona.getNombre());
        dialogo.setContentText("Direccion:");

        Optional<String> resultado = dialogo.showAndWait();
        resultado.ifPresent(direccion -> {
            try {
                // Validacion y guardado por logica
                logicaDePersona.agregarDireccion(persona, direccion);
                cargarDatosDetalladosDe(persona);

            } catch (Exception e) {
                mostrarMensaje("Error de validación", e.getMessage());
            }
        });
    }

    // Accion para eliminar una direccion
    @FXML
    void accionBotonEliminarDireccion(ActionEvent event) {
        Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();
        String direccion = tablaDirecciones.getSelectionModel().getSelectedItem();

        if (persona == null || direccion == null) { mostrarMensaje("Error", "Selecciona una direccion"); return; }

        try {
            // Se elimina la direccion usando la logica de persona
            logicaDePersona.eliminarDireccion(persona, direccion);
            cargarDatosDetalladosDe(persona);
        } catch (Exception e) {
            mostrarMensaje("Error", e.getMessage());
        }
    }

    // Metodo para mostrar alertas
    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Metodo auxiliar para refrescar las tablas
    private void actualizarTabla() {
        listaPersonasInterfaz.clear();
        listaPersonasInterfaz.addAll(ConexionDB.Conexion.cargarBaseDeDatos());
    }


}