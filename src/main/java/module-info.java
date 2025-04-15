module com.example.sudokuproyecto2 {
    requires javafx.controls;
    requires javafx.fxml;

    // Abre el paquete 'com.example.sudokuproyecto2' para javafx.fxml, permitiendo la reflexión
    opens com.example.sudokuproyecto2 to javafx.fxml;

    // Exporta el paquete 'com.example.sudokuproyecto2' para uso en otros módulos
    exports com.example.sudokuproyecto2;

    // Exporta el paquete 'controller' para permitir que el FXMLLoader acceda a 'SudokuController'
    exports com.example.sudokuproyecto2.controller;

    // Abre el paquete 'controller' a javafx.fxml para permitir la reflexión desde FXML
    opens com.example.sudokuproyecto2.controller to javafx.fxml;

    // Exporta el paquete 'model' y 'view' si lo necesitas para otros módulos
    exports com.example.sudokuproyecto2.model;
    exports com.example.sudokuproyecto2.view;
}
