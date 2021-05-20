# cs0320 Term Project 2021

Project Specifications: https://cs.brown.edu/courses/cs0320/projects/term-project.html

**Team Members:** ggallant jwebste5 abeckrui jfraust2

# Recipe Finder

## Division of Labor: 

- Georgia: backend class design/structure, creation of SQL database and database methods, graph classes and 
  recommendation algorithm

- Andres: User authentication with firebase, authentication frontend pages and integration, user database functions 
  and front end handlers, routing, autocorrect/validity for ingredients submission


- Jacob: Frontend design and implementation, React hook logic, axios requests/integration with backend, UI/UX 
  research/testing, frontend debugging

- Jack: JUnit Testing, backend class design/structure, graph classes and recommendation algorithm, integration 
  & implementation of handlers


## Known bugs:

- There are no known bugs in the program.

## Design Details specific to your code:


- User authentication:
  		For user authentication, we followed a youtube tutorial linked below. We used firebase 
  to store our user information and used firebase' authentication functions. 
  Link: https://www.youtube.com/watch?v=PKwu15ldZ7k&t=43s
  -User authentication components include:
  	-Signup:
  		-This component contains forms for users to input information including name, email, and password, and uses 
  		the firebase signup function to create a user account. It also creates a user object in the backend so that 
  		we can access the users' SQL data.
  	-Login:
  		-This component contains forms for email and password and uses the firebase login function to login. 
  		It also creates a user object in the backend so that we can access the users' SQL data.
  	-PrivateRoute:
  		-This component ensures that a user is logged in before accessing a certain route. If the user is not
  		logged in, we push the user back to the login page (/login)
  	-UpdatePassword:
  		-This component handles the logic for updating a user password, using firebase' update password function.
  	-ForgotPassword:
		-This component handles the logic if a user forgets their password, and sends an email to the user with a 
  		link to reset the password.
  	-AuthContext
  		-This component contains all the firebase functions needed for authentication and stores the current user
  	-firebase
  		-This component initializes the firebase connection

-Autocorrect:
	-We reused and modified the autocorrect code from previous CS32 labs so that we could suggest ingredients
when users began typing. We also used autocorrect to check that ingredients are valid, since we passed in the 
ingredients file into the autocorrect instance. If an ingredient is not valid, the user cannot submit the 
ingredient and an alert is shown. 

- Frontend:
	- The frontend includes the following pages (excluding login and signup pages):
		- Fridge:
			- This page includes the components that make up a user's Fridge. It also acts as a landing page after 
			  authentication. This inlucdes the list of current ingredients that a user has as well as a way to 
			  input more ingredients into their Fridge. The input is limited to ingredients found in our database 
			  (through an implementation of autocorrect). There are also accessible links to each of the other pages 
			  from this page.
		- Recipe Selection:
			- This page displays a scrollable div that includes a list of recipes presented to the user 
			  (acquired from backend graph and similarity/ranking algorithms). It includes a photo 
			  (if the recipe has one) for each recipe, as well as its name and the chef who created the recipe. 
			  The page includes accessible links to return to the Fridge if wanted.
		- Recipe:
			- This is the display page for any recipe selected. The page will dynamically display the ingredients and 
			  preparations needed for each recipel, as well as 3 similar recipes (acquired from backend algorithm). 
			  The page includes accessible links to return to the Recipe Selection page, the Fridge page, or to opening 
			  a link including the recipe on the BBC's website.
		- Profile: 
			- This page displays information about the user. This includes an option to delete/change their account. 
			  It also lists all the recipes and ingredients the user has previously rated, as well as a way to change 
			  those ratings dynamically.

- Graph:
	- The graph is a graph of central and non-central nodes. 
	- Created generically, so that other implementations can use the same algorithm without using Recipes and 
	  Ingredients
	  specifically.
	- Does not contain edges, and instead relies on the nodes themselves to reference their adjacent central/non-central
	  nodes.
	- Operates on classes that implement the Vertex interface. The Vertex interface requires that each node has a way to 
	  get the adjacent nodes, get the value of a node, and get its name property.
	- Has a function to search the graph, to find the most similar adjacent central node to a given central node. This 
	  search algorithm employs the similarity algorithm explained below.

- Similarity Algorithm: 
	- The similarity algorithm employs a combination of the Jacard similarity algorithm + rating prediction to 
	predict the similarity between nodes.
  	- The Jacard similarity algorithm finds the number of nodes that two adjacent nodes have in common, and divides
	this by the total number of adjacent nodes each central node has. This is one part of the index.
   - The second part of the index is the value (or "ranking"– for our implementation of the graph). The value is either
	the rating that a user gives a certain recipe, or an estimated rating which is calculated by averaging the ratings
	of the adjacent ingredients.
   - These two values are then combined using a weighted sum

## How to Build and Run

- You can build the program by running ‘mvn package’ in the terminal while located in the root directory.
	-NOTE: The GraphTest tests take roughly 5 minutes because the algorithm that computes similarity takes a while,
  and we make multiple successive calls to this method within the file.
  	-NOTE: The RandomInputTest takes around 15 seconds to run. This test adds random ingredients to the users fridge
  and checks for recipes.
  
- You can run the program with the frontend component by running ‘./run --gui’ in the terminal while located in the 
  root directory. You will also need to (in a separate terminal window) go into the 'recipe-finder' directory located 
  in the 'src/main' and run ‘npm start’. This will ensure that the GUI starts with React.

## What browser you used to test your GUI
- We primarily used Google Chrome to test Recipe Finder.



