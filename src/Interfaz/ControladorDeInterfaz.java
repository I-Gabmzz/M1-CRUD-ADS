package Interfaz;

import ConexionDB.Conexion;
import CRUD.PersonaDAO;
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
    @FXML private TableColumn<Persona, String> columnaDireccion;
    // Tabla de telefonos y su columna
    @FXML private TableView<String> tablaTelefonos;
    @FXML private TableColumn<String, String> columnaNumero;

    private PersonaDAO gestorDatos;
    private ObservableList<Persona> listaPersonasInterfaz;
    private ObservableList<String> listaTelefonosInterfaz;

    // Metodo de inicializacion que se ejecuta automaticamente al abrir la ventana
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Se inicializan el gestor de datos y las listas vacias
        gestorDatos = new PersonaDAO();
        listaPersonasInterfaz = FXCollections.observableArrayList();
        listaTelefonosInterfaz = FXCollections.observableArrayList();
        configurarTablas();

        // Se obtienen todos los registros de la base de datos y se agregan a la lista
        listaPersonasInterfaz.addAll(Conexion.cargarBaseDeDatos());

        // Se agrega un listener para detectar cuando el usuario selecciona una fila en la tabla de personas
        tablaPersonas.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccion) -> {
            if (seleccion != null) {
                // Si se selecciona a alguien, se cargan sus telefonos
                cargarTelefonosDe(seleccion);
            } else {
                // Si se deselecciona, se limpia la tabla de telefonos
                listaTelefonosInterfaz.clear();
            }
        });
    }

    // Metodo para vincular los datos de las personas con las columnas
    private void configurarTablas() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Configuracion para la columna de telefonos
        columnaNumero.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue()));

        // Se asignan las listas a los componentes de la interfaz
        tablaPersonas.setItems(listaPersonasInterfaz);
        tablaTelefonos.setItems(listaTelefonosInterfaz);
    }

    // Metodo para obtener y mostrar los telefonos de una persona seleccionada
    private void cargarTelefonosDe(Persona persona) {
        listaTelefonosInterfaz.clear();
        // Se consulta a la base de datos para asegurar que tenemos los respectivos datos
        Persona personaCompleta = gestorDatos.leerPersonaID(persona.getId());

        if (personaCompleta != null && personaCompleta.getTelefonos() != null) {
            // Se actualiza la lista y el objeto
            listaTelefonosInterfaz.addAll(personaCompleta.getTelefonos());
            persona.setTelefonos(personaCompleta.getTelefonos());
        }
    }

    // Accion para crear una nueva persona
    @FXML
    void accionBotonAñadirPersona(ActionEvent event) {
        // Se crea una ventana emergente
        Dialog<Persona> dialogo = new Dialog<>();
        dialogo.setTitle("Nueva Persona");
        dialogo.setHeaderText("Ingresa los datos:");
        ButtonType tipoBotonGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(tipoBotonGuardar, ButtonType.CANCEL);
        GridPane panel = new GridPane();
        panel.setHgap(10); panel.setVgap(10);

        TextField campoNombre = new TextField();
        TextField campoDireccion = new TextField();
        panel.add(new Label("Nombre:"), 0, 0);
        panel.add(campoNombre, 1, 0);
        panel.add(new Label("Direccion:"), 0, 1);
        panel.add(campoDireccion, 1, 1);

        dialogo.getDialogPane().setContent(panel);

        // Se convierten los datos insertados en la ventana en una Persona
        dialogo.setResultConverter(botonPresionado -> {
            if (botonPresionado == tipoBotonGuardar) {
                return new Persona(campoNombre.getText(), campoDireccion.getText());
            }
            return null;
        });

        Optional<Persona> resultado = dialogo.showAndWait();
        resultado.ifPresent(nuevaPersona -> {
            // Si el usuario se guardo, se inserta en la base de datos
            if (gestorDatos.insertarPersona(nuevaPersona)) {
                listaPersonasInterfaz.add(nuevaPersona);
                tablaPersonas.getSelectionModel().select(nuevaPersona);
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
        Dialog<Persona> dialogo = new Dialog<>();
        dialogo.setTitle("Editar");
        dialogo.setHeaderText("Modificar a: " + personaSeleccionada.getNombre());
        ButtonType botonActualizar = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(botonActualizar, ButtonType.CANCEL);
        GridPane panel = new GridPane();
        panel.setHgap(10); panel.setVgap(10);

        // Se cargan los campos con la informacion actual
        TextField campoNombre = new TextField(personaSeleccionada.getNombre());
        TextField campoDireccion = new TextField(personaSeleccionada.getDireccion());
        panel.add(new Label("Nombre:"), 0, 0); panel.add(campoNombre, 1, 0);
        panel.add(new Label("Direccion:"), 0, 1); panel.add(campoDireccion, 1, 1);
        dialogo.getDialogPane().setContent(panel);

        dialogo.setResultConverter(boton -> {
            if (boton == botonActualizar) {
                // Se actualizan los datos de la persona en la interfaz
                personaSeleccionada.setNombre(campoNombre.getText());
                personaSeleccionada.setDireccion(campoDireccion.getText());
                return personaSeleccionada;
            }
            return null;
        });

        Optional<Persona> resultado = dialogo.showAndWait();
        resultado.ifPresent(persona -> {
            // Se envian los cambios a la base de datos
            if (gestorDatos.actualizarPersona(persona)) {
                tablaPersonas.refresh(); // Se refresca la tabla de la interfaz
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
                // Se busca en la base de datos
                Persona encontrada = gestorDatos.leerPersonaID(id);

                if (encontrada != null) {
                    // Esta logica es para seleccionar la persona en la tabla una vez encontrada
                    boolean estabaEnLista = false;
                    for (Persona p : listaPersonasInterfaz) {
                        if (p.getId() == id) {
                            tablaPersonas.getSelectionModel().select(p);
                            tablaPersonas.scrollTo(p);
                            estabaEnLista = true;
                            break;
                        }
                    }
                    // Si no estaba en la lista, se agrega
                    if (!estabaEnLista) {
                        listaPersonasInterfaz.add(encontrada);
                        tablaPersonas.getSelectionModel().select(encontrada);
                    }
                } else {
                    mostrarMensaje("No encontrado", "ID no existe");
                }
            } catch (Exception e) {
                mostrarMensaje("Error", "ID invalido");
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
        alerta.setContentText("Se borraran tambien sus telefonos.");

        if (alerta.showAndWait().get() == ButtonType.OK) {
            // Se elimina de la base de datos y de las listas de la interfaz
            if (gestorDatos.eliminarPersona(persona.getId())) {
                listaPersonasInterfaz.remove(persona);
                listaTelefonosInterfaz.clear();
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
            if (!numero.isEmpty()) {
                // Se agrega el numero a la lista del objeto
                persona.agregarTelefono(numero);
                // Se usa actualizarPersona para guardar los cambios en la BD
                if (gestorDatos.actualizarPersona(persona)) {
                    cargarTelefonosDe(persona);
                }
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

        // Se elimina el numero de la lista que esta en memoria
        persona.getTelefonos().remove(numero);
        // Se actualiza la base de datos con la nueva lista
        if (gestorDatos.actualizarPersona(persona)) {
            cargarTelefonosDe(persona);
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
}