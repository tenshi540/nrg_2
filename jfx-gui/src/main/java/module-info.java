module com.energy.jfxgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.energy.jfxgui to javafx.fxml;
    exports com.energy.jfxgui;
}