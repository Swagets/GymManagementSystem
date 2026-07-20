module org.example.crudproyecto {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.crudproyecto to javafx.fxml;
    exports org.example.crudproyecto;
}