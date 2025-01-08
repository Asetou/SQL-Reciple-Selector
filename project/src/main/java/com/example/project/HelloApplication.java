package com.example.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Recipe App");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();

    }


//    public static void main(String[] args) {
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            connect = DriverManager.getConnection(
//                    "jdbc:mysql://ambari-node5.csc.calpoly.edu/eumorale?user=eumorale&password=027830952");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        launch();
//    }
}
