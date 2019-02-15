# bookApp
Test-Driven backend application to display list of books and their average rating

Stack: Spring Boot

Modules:

User Management
- Create User
- Update Existing User
- Delete User
- Toggle User Status
- View All Users
- View User by ID
- View User by Username

Role Management
- Create Role
- Update Role
- Delete Role
- Toggle Role Status
- View All Roles
- View Role By ID
- View Role By Role Name

Book Management
- Create Book
- Update Book
- Delete Book
- Toggle Book Status
- View All Books
- View Book by Author
- View Book by Title
- View Book by ISBN Number

Book Rating
- Rate A Book
- View Rating Average

User Login Credentials:
S/N	        Username			Password
1. 	        author				Password@123
2.		    editor				Password@123
3.		    reviewer			        Password@123
4.		    publisher			        Password@123

Roles:
S/N		Role			Access Control	
1.		Editor			Super Admin 		
2.		Author			Admin
3. 		Reviewer		Regular User
4.		Publisher		Regular User


Super Admi - Role Access Control Definition:
User: 	Can Create User, Update User, Delete User, Toggle User Status, View All Users, View a user, 
Role: 	Create Role, Update Role, Delete Role, Toggle Role Status, View All Roles, View Role
Book: 	Create Book, Update Book, Delete Book, Toggle Book Status, View All Books, View Book
Rating:	Rate a book, View Book Average Rating
 
 
Admin -  Role Access Control Definition: 
User: 	Can Create User, Update User, View All Users, View a user, 
Role: 	Create Role, Update Role, View All Roles, View Role
Book: 	Create Book, Update Book, View All Books, View Book
Rating:	Rate a book, View Book Average Rating
 
Regular User - Role Access Control Definition:
Book: 	View All Books, View Book
Rating:	Rate a book

Access to Swagger Documentation here: https://zonetechbookapp.herokuapp.com/swagger-ui


