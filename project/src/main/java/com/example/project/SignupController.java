package com.example.project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class SignupController {
    public Button backBTN;
    PreparedStatement ps;
    Connection connection;
    public SignupController(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu/eumorale?user=eumorale&password=027830952");
            connection.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    @FXML
    private TextField usernameTXT;
    @FXML
    private TextField calorieTXT;
    @FXML
    private TextField nameTXT;
    @FXML
    private Label signupERR;
    @FXML
    private TextField passwordTXT;
    @FXML
    private void HandleEvents(MouseEvent event) {
        if(event.getSource() == backBTN){
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                stage.setScene(scene);
                stage.show();
                connection.commit();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            String typeCheck = calorieTXT.getText();
            if (usernameTXT.getText().isEmpty() || nameTXT.getText().isEmpty() || calorieTXT.getText().isEmpty() || passwordTXT.getText().isEmpty()) {
                signupERR.setTextFill(Color.TOMATO);
                signupERR.setText("Enter all details");
            } else {
                try {
                    int i = Integer.parseInt(typeCheck);
                    if(i<1200){
                        signupERR.setText("Calorie limit must be above 1200");
                        return;
                    }
                } catch (NumberFormatException nfe) {
                    signupERR.setText("Calorie not an integer");
                    return;
                }
                if (saveSignup().equals("Successful")){
                    try {
                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        stage.close();
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Successful.fxml"));
                        Scene scene = new Scene(fxmlLoader.load(), 1330,663);
                        stage.setScene(scene);
                        stage.show();
                        connection.commit();
                    } catch (IOException ex) {
                        System.err.println(ex.getMessage());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }

    }
    private String saveSignup() {
        String username = usernameTXT.getText();
        String password = passwordTXT.getText();
        String name = nameTXT.getText();
        String calorie = calorieTXT.getText();
        String sql = "INSERT INTO User VALUES(?,?,?,?)";
        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1,username );
            ps.setString(2,password );
            ps.setString(3,name);
            ps.setString(4,calorie);
            ps.executeUpdate();
            usernameTXT.clear();
            passwordTXT.clear();
            nameTXT.clear();
            return "Successful";
        }catch (SQLException e) {
            System.err.println(e.getMessage());
            return "Exception";
        }
    }

}


