# supermarket
Supermarket Loyalty Card System


Microservices Developer Task (Spring Boot)
For this task you are required to design and implement a RESTful API, backing service and data models to create a simple Supermarket Loyalty Card
System where interaction with this API is made using HTTP requests. For the system you can use any technology within the Java ecosystem you feel
comfortable with, but using Java with Spring Boot is recommended. Swagger documents are required to help users discover and use the API. Scripts may
be used to seed your database with initial data (e.g. sample accounts and cashier details).
User Accounts are created by supplying the name, surname, a mobile number and an ID card number. Both the mobile number and the ID card number
can be used to fetch the user accounts uniquely. Once an account is created, one can add 10 purchase points for every 5 euros spent.
Purchase points can be redeemed either by discounting the last price from the last purchase (1 euro discount for every 100 points) or as a free packet of
water (1 packet for every 150 points). Purchase points are calculated by the API which accepts the total amount spent at the cash. The points to
redeem are accepted in multiples of 100/150 (depending on which redeem option is used).
All operations (both purchase and redeem) requires that the cashier's ID (that can be checked against a list of cashier IDs) is entered every time for audit
trail purposes.
A specific call will return all unclaimed (positive) balances for existing users.
APIs will use JSON payloads when applicable and error codes need to be returned when operations do not succeed. In order to make the application more
portable, an in-memory database as a backing store is recommended.
As mentioned, the application needs to be built using Java. Use Maven for dependency management and include unit testing for your service layer. The
source code can be committed on any version control repository of your choice as long as it's private and we're given
access to it.

#Implementation

1-> Cashier login at the system

2-> Cashier check single user by scanning card - >  he has all his information there, name, surname, purchase points ect

3-> Cashier check all products and gives to the user amount to pay(total), same time he also gives to the user 2 more options to use purchase points with current purchase, get discount if enough or take any free packed of water if purchase points are enough.

	Option 1: user buys something and leaves -> validate cashier, userid,  add purchase, calculate points and add to user
	option 2: user buys something and uses purchase points to get discount for current bill, calculated purchase points are removed from user 
	Option 3: user buys something and uses purchase points to get free water for current bill

To Mention when Cashier gives to the User those 2 options for using purchasePoints, purchasePoints are those of previouis purchases. The current ones are stored and not used for that bill.


APIs:

1 create single user/client

2 get user based on id, cardId or phone number provided

3 get positive balance(purchase points) for all users

4 calculate amount to pay, by providing discount or free packets of water if requested by client/user on current bill based on existing purchase points.

5 purchase amount by also including if user needed discount or free packets of water (check cashier, user, check if user needed discount(if yes apply discount and reduce amount + create RedeemedRewards and insert in db), calculate purchase point for newly purchase, create purchase and insert)