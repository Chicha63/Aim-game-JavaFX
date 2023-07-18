module com.example.guiproj2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.guiproj2 to javafx.fxml;
    exports com.example.guiproj2;
}