package com.example.project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class HelloController {
    @FXML
    private TextField usernameTXT;
    @FXML
    private Label signinERR;
    @FXML
    private TextField passwordTXT;

    @FXML
    private Button signinBTN;

    @FXML
    private Button signupBTN;

    public static String currentlyLoggedIn;
    @FXML
    protected void handleMouse(MouseEvent event) throws IOException {
        if (event.getSource() == signinBTN){
            if (login().equals("Successful")) {
                try {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Successful.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(),1330,663);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }

            }
        }
        else if (event.getSource() == signupBTN){
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Signup.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600,400);
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    public HelloController() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu/eumorale?user=eumorale&password=027830952");
            con.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private String login(){
        String username = usernameTXT.getText();
        currentlyLoggedIn = username;
        String password = passwordTXT.getText();
        String sql = "SELECT * FROM User where userID = ? and password = ?";

        if(username.isEmpty() || password.isEmpty()) {
            signinERR.setText("Empty Username/Password");
            return "Error";
        }try{
            ps = con.prepareStatement(sql);
            ps.setString(1,username );
            ps.setString(2,password );
            rs = ps.executeQuery();
           if(!rs.next()){
               signinERR.setText("Wrong email/password");
               return "Error";
           }
        }catch (SQLException e) {
            System.err.println(e.getMessage());
           return "Exception";
        }
        return "Successful";
    }

}