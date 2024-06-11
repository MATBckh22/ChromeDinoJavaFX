module com.example.chromedino {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.example.chromedino to javafx.fxml;
    exports com.example.chromedino;
}