CREATE TABLE Food(
foodName varchar(30) primary key,
ingredients varchar(500) not null,
recipe varchar(1000) not null,
difficulty enum('Easy','Medium','Hard') not null,
calorieCount INTEGER not null,
category varchar(30)); 

CREATE TABLE User(
userID varchar(30) primary key,
password varchar(30) not null,
name varchar(30) not null,
calorieLimit int not null);

-- multiple entries per day per meal eaten
CREATE TABLE MealPlanner(
planID int auto_increment primary key,
foodName varchar(30),
mealType enum('Breakfast','Lunch','Dinner','Dessert','Snack') not null,
userID varchar(30),
mealDate DATE,
Foreign key(foodName) references Food(foodName),
Foreign key (userID) references User(userID));

CREATE TABLE FavoriteFoods(
userID varchar(30),
likedFood varchar(30),
foreign key(userID) references User(userID),
foreign key(likedFood) references Food(foodName),
Primary key (userID, likedFood));

