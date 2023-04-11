package org.openjfx.clientfx;

import org.openjfx.server.models.*;
import org.openjfx.client.Client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.ArrayList;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.control.cell.*;

public class ClientFXLauncher extends Application {
    Client client;
    int PORT = 1500;
    ArrayList<Course> courseList;

    public void begin() {
        launch();
    }

    @Override
    public void start(Stage stage) {
        client = new Client();
        HBox root = new HBox();
        Background bgRoot = new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY));
        root.setBackground(bgRoot);

        VBox left = new VBox();
        Background bgLeft = new Background (new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY));
        left.setBackground(bgLeft);
        left.prefWidthProperty().bind(root.widthProperty().multiply(0.50));
        left.setPadding(new Insets(5, 0, 5, 5));
        left.setSpacing(5);
        root.getChildren().add(left);

        VBox topLeft = new VBox();
        Background bgTopLeft = new Background (new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY));
        topLeft.setBackground(bgTopLeft);
        topLeft.prefHeightProperty().bind(left.heightProperty().multiply(0.80));
        topLeft.setAlignment(Pos.CENTER);
        left.getChildren().add(topLeft);

        HBox bottomLeft = new HBox();
        Background bgBottomLeft = new Background (new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY));
        bottomLeft.setBackground(bgBottomLeft);
        bottomLeft.prefHeightProperty().bind(left.heightProperty().multiply(0.20));
        bottomLeft.setSpacing(50);
        bottomLeft.setAlignment(Pos.CENTER);
        left.getChildren().add(bottomLeft);

        VBox right = new VBox();
        Background bgRight = new Background(new BackgroundFill(Color.CORNSILK, CornerRadii.EMPTY, Insets.EMPTY));
        right.setBackground(bgRight);
        right.prefWidthProperty().bind(root.widthProperty().multiply(0.50));
        HBox.setMargin(right, new Insets(5,5,5,5));
        right.setAlignment(Pos.CENTER);
        right.setSpacing(15);
        root.getChildren().add(right);

        Scene scene = new Scene(root, 800, 600, Color.SILVER);

        Text listeCoursTitre = new Text("Liste des cours");
        listeCoursTitre.setFont(Font.font("Lucida Sans Unicode", 33));
        VBox.setMargin(listeCoursTitre, new Insets(10,0,0,0));
        topLeft.getChildren().add(listeCoursTitre);

        TableView<Course> coursTable = new TableView<>();

        TableColumn<Course, String> codeColumn = new TableColumn<>("Code");
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Course, String> coursColumn = new TableColumn<>("Cours");
        coursColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        coursTable.getColumns().addAll(codeColumn, coursColumn);
        topLeft.getChildren().add(coursTable);

        codeColumn.prefWidthProperty().bind(coursTable.widthProperty().multiply(0.30));
        coursColumn.prefWidthProperty().bind(coursTable.widthProperty().multiply(0.70));
        VBox.setMargin(coursTable, new Insets(15, 15, 15, 15));


        ObservableList<String> sessions = FXCollections.observableArrayList (
                "Automne", "Hiver", "Ete");
        ComboBox<String> listeSessions = new ComboBox<>(sessions);
        listeSessions.getSelectionModel().selectFirst();
        bottomLeft.getChildren().add(listeSessions);
        listeSessions.setPrefSize(150, 35);

        Button chargerBtn = new Button("Charger");
        bottomLeft.getChildren().add(chargerBtn);
        chargerBtn.setPrefSize(75, 35);

        chargerBtn.setOnAction((event) -> {
            String session = listeSessions.getValue();
            client.connect("127.0.0.1", PORT);
            courseList = client.getCourses(session);
            if (courseList != null) {
                ObservableList<Course> listeCours = FXCollections.observableArrayList(courseList);
                coursTable.setItems(listeCours);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur avec le server au moment de l'obtention des " +
                        "cours. Veuillez réessayer plus tard. ", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Text fourmulaireTitre = new Text("Formulaire d'inscription");
        fourmulaireTitre.setFont(Font.font("Lucida Sans Unicode", 25));
        right.getChildren().add(fourmulaireTitre);

        GridPane formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setPadding(new Insets(20, 20, 20, 20));
        formGrid.setHgap(25);
        formGrid.setVgap(10);

        Label prenomLabel = new Label("Prénom:");
        prenomLabel.setFont(Font.font("Lucida Sans Unicode", 15));
        TextField prenomField = new TextField();
        prenomField.setPrefWidth(150);
        formGrid.add(prenomLabel, 0, 0);
        formGrid.add(prenomField, 1, 0);

        Label nomLabel = new Label("Nom:");
        nomLabel.setFont(Font.font("Lucida Sans Unicode", 15));
        TextField nomField = new TextField();
        nomField.setPrefWidth(150);
        formGrid.add(nomLabel, 0, 1);
        formGrid.add(nomField, 1, 1);

        Label emailLabel = new Label("Email:");
        emailLabel.setFont(Font.font("Lucida Sans Unicode", 15));
        TextField emailField = new TextField();
        emailField.setPrefWidth(150);
        formGrid.add(emailLabel, 0, 2);
        formGrid.add(emailField, 1, 2);

        Label matriculeLabel = new Label("Matricule:");
        matriculeLabel.setFont(Font.font("Lucida Sans Unicode", 15));
        TextField matriculeField = new TextField();
        matriculeField.setPrefWidth(150);
        formGrid.add(matriculeLabel, 0, 3);
        formGrid.add(matriculeField, 1, 3);

        right.getChildren().add(formGrid);

        Button envoyerBtn = new Button("Envoyer");
        right.getChildren().add(envoyerBtn);
        envoyerBtn.setPrefSize(75, 35);

        envoyerBtn.setOnAction((event) -> {
            String prenom = prenomField.getText();
            String nom = nomField.getText();
            String email = emailField.getText();
            String matricule = matriculeField.getText();
            String errorMessage = "";
            if (prenom.equals("")) {
                errorMessage += "\nLe champ 'Prénom' est invalide!";
            }
            if (nom.equals("")) {
                errorMessage += "\nLe champ 'Nom' est invalide!";
            }
            if (!email.matches("\\S+@\\S+[.]\\S+")) {
                errorMessage += "\nLe champ 'Email' est invalide!";
            }
            if (!(matricule.length() == 8) || !(matricule.matches("[0-9]+"))) {
                errorMessage += "\nLe champ 'Matricule' est invalide!";
            }
            if (!(errorMessage.equals(""))) {
                errorMessage = "Le formulaire est invalide." + errorMessage;
                Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.OK);
                alert.showAndWait();
            }
            else {
                Course course = coursTable.getSelectionModel().getSelectedItem();
                if (course == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Le formulaire est invalide.\nVous devez " +
                            "sélectionner un cours!", ButtonType.OK);
                    alert.showAndWait();
                }
                else {
                    client.connect("127.0.0.1", PORT);
                    String response = client.registerToCourse(new RegistrationForm(prenom, nom, email, matricule, course));
                    if (response != null) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Félicitations! " + nom + " " + prenom + " est"+
                                " inscrit(e) avec succès pour le cours " + course.getCode() + "!", ButtonType.OK);
                        prenomField.clear();
                        nomField.clear();
                        emailField.clear();
                        matriculeField.clear();
                        alert.showAndWait();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur avec le serveur au moment de " +
                                "l'inscription. Veuillez réessayer plus tard." , ButtonType.OK);
                        alert.showAndWait();
                    }

                }
            }
        });

        stage.setTitle("Inscription UdeM");
        stage.setScene(scene);
        stage.show();
    }
}