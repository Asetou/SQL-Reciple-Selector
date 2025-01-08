package com.example.project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.*;

public class SuccessfulController {
    public String user = HelloController.currentlyLoggedIn;
    @FXML
    public Button likedFoodBTN;
    @FXML
    public Button mealPlanBTN;
    @FXML
    public TextField nameFoodTXT;
    @FXML
    public TextField foodPlanTXT;
    @FXML
    public TextField mealDate;
    @FXML
    private MenuButton QueryMenu;
    public MenuItem Easy;
    @FXML
    public MenuItem Breakfast;
    @FXML
    public MenuItem Lunch;
    @FXML
    public MenuItem Dinner;
    @FXML
    public MenuItem Snack;
    @FXML
    public MenuItem Dessert;
    @FXML
    public MenuItem Medium;
    @FXML public MenuItem Vegan;
    @FXML public MenuItem Vegeterian;
    public Button btnSbmitRecipe;
    @FXML MenuItem None;
    @FXML Label recipeSaveERR;
    @FXML Label likedSaveERR;
    @FXML Label foodPlanERR;
    @FXML
    public MenuItem Hard;
    @FXML
    private TextArea queryResults;
    @FXML
    private TextField UserQueryInput;
    @FXML
    private TextField foodName;
    @FXML
    private TextArea ingredients;
    @FXML
    private TextArea recipeSteps;
    @FXML
    private MenuButton difficulty;
    @FXML
    private TextField calorieCount;
    @FXML
    private MenuButton category;
    @FXML
    private MenuButton mealType;

    Boolean QueryFoodByIngredientTF = false;

    Boolean UsersFavoriteFoodTF = false;
    Boolean SearchCategory = false;
    PreparedStatement ps;
    Connection connection;
    public SuccessfulController() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu/eumorale?user=eumorale&password=027830952");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSubmit(MouseEvent event){
        if (event.getSource() == btnSbmitRecipe){
            if (calorieCount.getText().isEmpty() || foodName.getText().isEmpty() || ingredients.getText().isEmpty() || recipeSteps.getText().isEmpty()) {
                recipeSaveERR.setTextFill(Color.TOMATO);
                recipeSaveERR.setText("Enter all details");
            }else{
                saveRecipe();
            }
        } else if (event.getSource() == likedFoodBTN){
            if( nameFoodTXT.getText().isEmpty()){
                likedSaveERR.setTextFill(Color.TOMATO);
                likedSaveERR.setText("Enter all details");
            }else{
                saveLiked();
            }
        }else if(event.getSource()==mealPlanBTN){
            if ( mealDate.getText().isEmpty() || foodPlanTXT.getText().isEmpty()) {
                foodPlanERR.setTextFill(Color.TOMATO);
                foodPlanERR.setText("Enter all details");
            }else{
                saveMealPlan();
            }
        }
    }

    private void saveLiked() {
        String fd = nameFoodTXT.getText();

        String sql = "INSERT INTO FavoriteFoods VALUES(?,?)";

        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1,user );
            ps.setString(2,fd );
            ps.executeUpdate();
            nameFoodTXT.clear();
        }catch (SQLException e) {
            System.err.println(e.getMessage());
            likedSaveERR.setTextFill(Color.RED);
            likedSaveERR.setText("Liked Failed");
            return;
        }
        likedSaveERR.setTextFill(Color.GREEN);
        likedSaveERR.setText("Liked Saved Successfully");


    }
    @FXML //for the drop down menu breakfast, lunch, etc
    private void HandleTypeMeal(ActionEvent event) {
        String [] diff = event.getTarget().toString().split(",");
        diff = diff[0].split("=");
        mealType.setText(diff[1]);
    }

    @FXML
    private void HandleDifficulty(ActionEvent event) {
        String [] diff = event.getTarget().toString().split(",");
        diff = diff[0].split("=");
        difficulty.setText(diff[1]);
}
    @FXML
    private void HandleCategory(ActionEvent event) {
        String [] diff = event.getTarget().toString().split(",");
        diff = diff[0].split("=");
        category.setText(diff[1]);
    }

    private void saveMealPlan() {
        String typeMealText = mealType.getText();
        String usernameText = user;
        String foodNameText = foodPlanTXT.getText();
        String mealDateText = mealDate.getText();

        String sql = "INSERT INTO MealPlanner (foodName, mealType,userID,mealDate) VALUES(?,?,?,?)";

        try{
            ps = connection.prepareStatement(sql);
            ps.setString(1,foodNameText );
            ps.setString(2, typeMealText);
            ps.setString(3,usernameText);
            ps.setString(4,mealDateText);
            ps.executeUpdate();
            mealDate.clear();
            foodPlanTXT.clear();
            mealType.setText("Type of Meal");
        }catch (SQLException e) {
            System.err.println(e.getMessage());
            foodPlanERR.setTextFill(Color.RED);
            foodPlanERR.setText("Food Plan Save Failed");
            return;
        }
        foodPlanERR.setTextFill(Color.GREEN);
        foodPlanERR.setText("Food Plan Saved Successfully");
    }
    private void saveRecipe() {

            String difficultyText = difficulty.getText();
            String fname = foodName.getText();
            String ing = ingredients.getText();
            String recip = recipeSteps.getText();
            int calorieCnt;
            try{
                calorieCnt = Integer.parseInt(calorieCount.getText());
            } catch (NumberFormatException nfe) {
                recipeSaveERR.setText("Calorie not an integer");
                return;
            }

            String sql = "INSERT INTO Food VALUES(?,?,?,?,?,?)";

            try{
                ps = connection.prepareStatement(sql);
                ps.setString(1,fname );
                ps.setString(2,ing );
                ps.setString(3,recip);
                ps.setString(4,difficultyText);
                ps.setString(5, String.valueOf(calorieCnt));
                ps.setString(6, category.getText());
                ps.executeUpdate();
                foodName.clear();
                calorieCount.clear();
                ingredients.clear();
                recipeSteps.clear();
                difficulty.setText("Difficulty");
            }catch (SQLException e) {
                System.err.println(e.getMessage());
                return;
            }
            recipeSaveERR.setTextFill(Color.GREEN);
            recipeSaveERR.setText("Recipe Saved Successfully");

    }

    @FXML
    private void testQuery(ActionEvent event){
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = false;
        SearchCategory = false;

        QueryMenu.setText("Test");
        StringBuilder hi= new StringBuilder();
        int i = 0;
        while (i < 10){
            i++;
            String s = String.valueOf(i);
            hi.append(s).append("\n\n");
        }
        queryResults.setText(String.valueOf(hi));
    }

    @FXML
    private void setQuery2True(){
        QueryFoodByIngredientTF = true;
        UsersFavoriteFoodTF = false;
        SearchCategory = false;

        QueryMenu.setText("Search by Ingredient Name. (Insert Ingredient Name)");
    }

    @FXML
    private void setQueryFavoriteFood(){
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = true;
        SearchCategory = false;
        QueryMenu.setText("Search a user’s favorite food by Name");
    }

    @FXML
    private void SearchCategoryType(){
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = false;
        SearchCategory = true;
        QueryMenu.setText(        "Search By Food by Category Type. (Insert Food Type)");
    }

        @FXML
    private void QueryResult2() throws SQLException {
        try{
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = false;

        SearchCategory = false;
        QueryMenu.setText("What are the usernames and passwords for all users?");
        ResultSet rs;
        Statement statement = connection.createStatement();
        rs = statement.executeQuery("SELECT UserID, password FROM User");
        StringBuilder ans = new StringBuilder();

        while (rs.next()){
            String Username = rs.getString(1);
            String Password = rs.getString(2);
            ans.append(Username).append(", ").append(Password).append("\n\n");
        }
        queryResults.setText(String.valueOf(ans));
        }
        catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    }

    @FXML
    private void QueryByInput() throws SQLException{
        try{
        if (QueryFoodByIngredientTF){
            if (!UserQueryInput.getText().isEmpty()){
                ResultSet rs;
                Statement statement = connection.createStatement();
                rs = statement.executeQuery("select foodName" +
                        " from Food F" +
                        " where F.ingredients " +
                        " LIKE '%" + UserQueryInput.getText() + "%'");
                StringBuilder ans = new StringBuilder();
                while (rs.next()){
                    String FoodName = rs.getString(1);
                    ans.append(FoodName).append("\n\n");
                }
                queryResults.setText(String.valueOf(ans));
            }
            else {
                queryResults.setText("Enter an ingredient.");
            }
        }
        else if (UsersFavoriteFoodTF){
            if (!UserQueryInput.getText().isEmpty()){
                ResultSet rs;
                Statement statement = connection.createStatement();
                rs = statement.executeQuery("select F.likedFood " +
                        "from FavoriteFoods F, User U " +
                        "where F.userID = U.userID and U.name = '" + UserQueryInput.getText() + "'");
                StringBuilder ans = new StringBuilder();
                while (rs.next()){
                    String FoodName = rs.getString(1);
                    ans.append(FoodName).append("\n\n");
                }
                queryResults.setText(String.valueOf(ans));
            }
            else {
                queryResults.setText("Enter a user's Name.");
            }
        }
        else if (SearchCategory){
            if (!UserQueryInput.getText().isEmpty()){
                ResultSet rs;
                Statement statement = connection.createStatement();
                rs = statement.executeQuery("Select foodName " +
                        "From Food F " +
                        "Where F.category = '" + UserQueryInput.getText() + "'");
                StringBuilder ans = new StringBuilder();
                while (rs.next()){
                    String FoodName = rs.getString(1);
                    ans.append(FoodName).append("\n\n");
                }
                queryResults.setText(String.valueOf(ans));
            }
            else {
                queryResults.setText("Enter a Food Category Type");
            }
        }

    }
        catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    }

    @FXML
    private void UsersOnApp() throws SQLException {
        try{
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = false;
        SearchCategory = false;

        QueryMenu.setText("How many users are using this App?");
        ResultSet rs;
        Statement statement = connection.createStatement();
        rs = statement.executeQuery("select count(*) as totUsers from User U");
        StringBuilder ans = new StringBuilder();

        while (rs.next()){
            String NumberOfUsers = rs.getString(1);
            ans.append(NumberOfUsers).append("\n\n");
        }
        queryResults.setText(String.valueOf(ans));
    }
        catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    }

    @FXML
    private void MostLikedFoods() throws SQLException {

        try{
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = false;
        SearchCategory = false;

        QueryMenu.setText("What are the top 10 most liked Food items?");
        ResultSet rs;
        Statement statement = connection.createStatement();
        rs = statement.executeQuery("select likedFood, count(*) as totLikes " +
                "from FavoriteFoods FF " +
                "group by FF.likedFood " +
                "order by totLikes desc " +
                "limit 10");
        StringBuilder ans = new StringBuilder();

        while (rs.next()){
            String Food = rs.getString(1);
            ans.append(Food).append("\n\n");
        }
        queryResults.setText(String.valueOf(ans));
    }
        catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    }

    @FXML
    private void GetUsersNames() throws SQLException{
        try {
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = false;
        SearchCategory = false;

        QueryMenu.setText("What are the names of all the users?");
        ResultSet rs;
        Statement statement = connection.createStatement();
        rs = statement.executeQuery("select name " +
                "from User");
        StringBuilder ans = new StringBuilder();

        while (rs.next()){
            String Name = rs.getString(1);
            ans.append(Name).append("\n\n");
        }
        queryResults.setText(String.valueOf(ans));
    }
        catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    }
    @FXML
    private void FavoriteMealPlan() throws SQLException{
        try {
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = false;
        SearchCategory = false;

        QueryMenu.setText("List of favorite foods eaten in MealPlan");
        ResultSet rs;
        Statement statement = connection.createStatement();
        rs = statement.executeQuery("select F.foodName, M.mealDate, F.ingredients, U.name " +
                "from MealPlanner M, Food F, FavoriteFoods FF, User U " +
                "where FF.userID = U.userID and FF.likedFood = M.foodName");
        StringBuilder ans = new StringBuilder();

        while (rs.next()){
            String foodName = rs.getString(1);
            String mealDate = rs.getString(2);
            String ingredients = rs.getString(3);
            String name = rs.getString(4);

            ans.append(foodName).append(", ").append(mealDate).append(", ").append(ingredients).append(", ").append(name).append(", ").append("\n\n");
        }
        queryResults.setText(String.valueOf(ans));
    }
        catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    }

    @FXML
    private void ManyFoodsUsersLiked() throws SQLException{
        try {
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = false;
        SearchCategory = false;

        QueryMenu.setText("How many foods all users have liked?");
        ResultSet rs;
        Statement statement = connection.createStatement();
        rs = statement.executeQuery("select U.userID, coalesce(count(likedFood), 0) as totLikes " +
                "from User U left join FavoriteFoods FF on (U.userID = FF.userID) " +
                "group by U.userID");
        StringBuilder ans = new StringBuilder();

        while (rs.next()){
            String UserID = rs.getString(1);
            String TotalLikes = rs.getString(2);

            ans.append(UserID).append(", ").append(TotalLikes).append("\n\n");
        }
        queryResults.setText(String.valueOf(ans));
    }
        catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    }

    @FXML
    private void SumCaloriesOnDate() throws SQLException{
        try {
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = false;
        SearchCategory = false;

        QueryMenu.setText("Sum of Calories for a User based on Exact Year + Month + Day");
        ResultSet rs;
        Statement statement = connection.createStatement();
        rs = statement.executeQuery("select M.userID, year(M.mealDate) as year, month(M.mealDate) as month, day(M.mealDate) as day, sum(F.calorieCount) as dailyCalories, U.calorieLimit " +
                "from Food F, MealPlanner M, User U " +
                "where F.foodName = M.foodName and U.userID = M.userID " +
                "group by M.userID, month(M.mealDate), year(M.mealDate), day(M.mealDate), U.calorieLimit;");
        StringBuilder ans = new StringBuilder();

        while (rs.next()){
            String UserID = rs.getString(1);
            String Year = rs.getString(2);
            String Month = rs.getString(3);
            String day = rs.getString(4);
            String dailyCalories = rs.getString(5);
            String calorieLimit = rs.getString(6);
            ans.append(UserID).append(", ").append(Year).append(", ").append(Month).append(", ").append(day).append(", ").append(dailyCalories).append(", ").append(calorieLimit).append("\n\n");
        }
        queryResults.setText(String.valueOf(ans));
    }
        catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    }
    @FXML
    private void RecipeOfAllFoods() throws SQLException{
        try {
        QueryFoodByIngredientTF = false;
        UsersFavoriteFoodTF = false;
        SearchCategory = false;

        QueryMenu.setText("What are the recipes for all the Foods?");
        ResultSet rs;
        Statement statement = connection.createStatement();
        rs = statement.executeQuery(" " +
                "select foodName, recipe " +
                "from Food;");
        StringBuilder ans = new StringBuilder();

        while (rs.next()){
            String FoodName = rs.getString(1);
            String Recipe = rs.getString(2);
            ans.append(FoodName).append("\n").append(Recipe).append("\n\n");
        }
        queryResults.setText(String.valueOf(ans));
    }
        catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    }

    @FXML
    private void MyFavoriteFood() throws SQLException{
        try {
            QueryFoodByIngredientTF = false;
            UsersFavoriteFoodTF = false;
            SearchCategory = false;

            QueryMenu.setText("What are the recipes for all the Foods?");
            ResultSet rs;
            Statement statement = connection.createStatement();
            rs = statement.executeQuery("select likedFood " +
                    "from FavoriteFoods " +
                    "where userID = '" + user + "'");
            StringBuilder ans = new StringBuilder();

            while (rs.next()) {
                String likedFood = rs.getString(1);
                ans.append(likedFood).append("\n\n");
            }
            queryResults.setText(String.valueOf(ans));
        }
        catch (SQLException ex) {
        System.err.println(ex.getMessage());
    }
    }

}

