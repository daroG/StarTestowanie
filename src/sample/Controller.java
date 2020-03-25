package sample;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TableView table;
    @FXML
    private TextField name;
    @FXML
    private TextField constellation;
    @FXML
    private TextField declination;
    @FXML
    private TextField rightAscension;
    @FXML
    private TextField distance;
    @FXML
    private TextField observedMagnitude;
    @FXML
    private TextField temperature;
    @FXML
    private TextField mass;
    @FXML
    private Label label;
    @FXML
    private Label nameSub;
    @FXML
    private Label constellationSub;
    @FXML
    private Label declinationSub;
    @FXML
    private Label rightAscensionSub;
    @FXML
    private Label distanceSub;
    @FXML
    private Label observedMagnitudeSub;
    @FXML
    private Label temperatureSub;
    @FXML
    private Label massSub;

    private Star selectedStar = null;

    @FXML
    public void removeStar(ActionEvent actionEvent) {
        if (selectedStar == null) return;

        if((new DatabaseManager()).deleteStarWithId(selectedStar.getId()) > 0){
            System.out.println("Usunięto gwiazdę");
            refreshTable();
        }

    }


    @FXML
    public void addStar(ActionEvent actionEvent) {

        Star star = new Star();

        boolean isDone = true;

        hideHints();

        try {
            star.setName(name.getText());
        } catch (Exception e) {
            nameSub.setVisible(true);
            nameSub.setText(e.getMessage());
            isDone = false;
        }

        try {
            star.setDeclination(declination.getText());
        } catch (Exception e) {
            declinationSub.setVisible(true);
            declinationSub.setText(e.getMessage());
            isDone = false;
        }

        try {
            star.setRightAscension(rightAscension.getText());
        } catch (Exception e) {
            rightAscensionSub.setVisible(true);
            rightAscensionSub.setText(e.getMessage());
            isDone = false;
        }

        try {
            star.setObservedMagnitude(Double.parseDouble(observedMagnitude.getText()));
        } catch (Exception e) {
            observedMagnitudeSub.setVisible(true);
            observedMagnitudeSub.setText(e.getMessage());
            isDone = false;
        }

        try {
            star.setDistance(Double.parseDouble(distance.getText()));
        } catch (Exception e) {
            distanceSub.setVisible(true);
            distanceSub.setText(e.getMessage());
            isDone = false;
        }

        try {
            star.setTemperature(Double.parseDouble(temperature.getText()));
        } catch (Exception e) {
            temperatureSub.setVisible(true);
            temperatureSub.setText(e.getMessage());
            isDone = false;
        }

        try {
            star.setMass(Double.parseDouble(mass.getText()));
        } catch (NumberFormatException e) {

        } catch (Exception e) {
            massSub.setVisible(true);
            massSub.setText(e.getMessage());
            isDone = false;
        }

        try {
            star.setConstellation(constellation.getText());
        } catch (Exception e) {
            constellationSub.setVisible(true);
            constellationSub.setText(e.getMessage());
            isDone = false;
        }

        if (isDone) {
            System.out.println("Gwiazda może być zapisana");
            if (1 == (new DatabaseManager()).insertStar(star)) {
                refreshTable();
                hideHints();
                clearInputs();
            } else {
                System.out.println("Błąd, nie udało się dodać gwiazdy");
            }
        } else {
            System.out.println("Gwiazda nie może być zapisana");
        }

//        System.out.println("Name: " + name.getText());


    }


    public TableColumn[] createTable() {
        TableView tableView = new TableView();

        TableColumn<String, Star> columns[] = new TableColumn[12];
        columns[0] = new TableColumn<>("Id");
        columns[0].setCellValueFactory(new PropertyValueFactory<>("id"));

        columns[1] = new TableColumn<>("Nazwa");
        columns[1].setCellValueFactory(new PropertyValueFactory<>("name"));

        columns[2] = new TableColumn<>("Nazwa katalogowa");
        columns[2].setCellValueFactory(new PropertyValueFactory<>("catalogName"));

        columns[3] = new TableColumn<>("Deklinacja");
        columns[3].setCellValueFactory(new PropertyValueFactory<>("declination"));

        columns[4] = new TableColumn<>("Rektascensja");
        columns[4].setCellValueFactory(new PropertyValueFactory<>("rightAscension"));

        columns[5] = new TableColumn<>("Obserwowana wielkość gwiazdowa");
        columns[5].setCellValueFactory(new PropertyValueFactory<>("observedMagnitude"));

        columns[6] = new TableColumn<>("Odległość");
        columns[6].setCellValueFactory(new PropertyValueFactory<>("distance"));

        columns[7] = new TableColumn<>("Gwiazdozbiór");
        columns[7].setCellValueFactory(new PropertyValueFactory<>("constellation"));

        columns[8] = new TableColumn<>("Absolutna wielkość gwiazdowa");
        columns[8].setCellValueFactory(new PropertyValueFactory<>("absoluteMagnitude"));

        columns[9] = new TableColumn<>("Półkula");
        columns[9].setCellValueFactory(new PropertyValueFactory<>("hemisphere"));

        columns[10] = new TableColumn<>("Temperatura");
        columns[10].setCellValueFactory(new PropertyValueFactory<>("temperature"));

        columns[11] = new TableColumn<>("Masa");
        columns[11].setCellValueFactory(new PropertyValueFactory<>("mass"));

        return columns;
    }

    private void refreshTable() {
        table.getColumns().clear();
        table.getItems().clear();

        table.getColumns().addAll(createTable());
        table.getItems().addAll((new DatabaseManager()).getAllStars());
    }

    private void hideHints(){
        nameSub.setVisible(false);
        declinationSub.setVisible(false);
        observedMagnitudeSub.setVisible(false);
        rightAscensionSub.setVisible(false);
        distanceSub.setVisible(false);
        temperatureSub.setVisible(false);
        massSub.setVisible(false);
        constellationSub.setVisible(false);
    }

    private void clearInputs(){
        name.setText("");
        declination.setText("");
        observedMagnitude.setText("");
        rightAscension.setText("");
        distance.setText("");
        temperature.setText("");
        mass.setText("");
        constellation.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        refreshTable();
        table.setPlaceholder(new Label("Brak gwiazd do wyświetlenia"));

        table.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<Star>() {
            @Override
            public void onChanged(Change<? extends Star> change) {
                selectedStar = change.getList().get(0);
            }
        });
    }

}
