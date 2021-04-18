# cs0320 Term Project 2021

**Team Members:** ggallant jwebste5 abeckrui jfraust2

**Team Strengths and Weaknesses:** 
Weaknesses:
- Have never worked on a project this loosely structured before
- Most have limited experience with front end development
- None of us have worked together before (but this might be a strength?!)

Strengths:
- Polymorphic programming + generics (we all took cs15)
- Some of us have taken deep learning + AI so if we do a project related to that, the background might prove useful
- One of us has taken UIUX so has a basic understanding of front end design (css)

**Project Idea(s):** _Fill this in with three unique ideas! (Due by March 1)_

### Idea 1
Recipe generator: 
- Website requires login 
- Each profile has kitchen with ingredients + quantities of food items in user's (real) kitchen
- Ability to select food preferences (vegetarian, etc.) and generate a recipe from querying database of recipes to find recipe that fits input criteria
- Recipe generators learns based on recipes most cooked
- Updates contents of kitchen when a given recipe is cooked
- Additional feature: Ability to dynamically add items to shopping cart when kitchen is empty of a desired ingredient

Algorithm complexity stems from generating reciped based on
	1) what is in kitchen
	2) prioritization of recipes based on user preferences
	3) history of recipes user has cooked

Challenges:
- Login + tracking users
- Using user history to prioritize recipes
- Creating user-friendly (non tedious) interface

**HTA Approval (crusch):** Approved, but it's going to be really important that your algorithm has some level of complexity and that this isn't a CRUD app!

### Idea 2
Spotify Snippets:
- Website requires login
- Each profile has music preferences based on 1) what user has selected as music preferences and 2) user's listening history
- Ability to create a "song picker" song which consists of an endless stream of 20 second song snippets that the user can choose whether they
like each song or not. The generator automatically puts liked songs in a playlist of songs of a similar genre.

Algorithm complexity stems from 
1) using user data (including history) to find song to play in "song picker" song
2) Making playlists of songs of the same genre

Challenges:
- Login + tracking users
- Using user history to identify genre preferences
- Using genre prefences to select song snippets

**HTA Approval (crusch):** Rejected — this will either be too straightforward or too complex.

### Idea 3
Critical Review 2.0:
- Website requires login
- Each profile would have a record of classes they have taken 
- Each class has a page with the ability to comment. Each comment has tags (difficulty, time required, etc.)
- Tags are aggregated to form stats for the page
- Users are required to comment X times before they can view more than 3 other class reviews
- Ability to input concentration, and receive a set of class recommendations based on the stats of the class (would not tell
you to take all really difficult classes at the same time) + requirements and prereqs
- Additional feature: each user can search other friends to find the classes they are taking

Algorithmic complexity comes from generating personalized class recomendations based on concentration + class stats

Challenges:
- Login + tracking users
- Creating class recommendations 
- Storing class data in a way that is efficiently accessible (tree with classes as nodes?)
- Limits to extensibility because certain classes have certain prereqs that have to be inputted manually

**HTA Approval (crusch):** Rejected — it sounds useful, but it seems like it'll end up being a CRUD app that lacks algorithmic complexity.


**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 15)_

**4-Way Checkpoint:** _(Schedule for on or before April 5)_

**Adversary Checkpoint:** _(Schedule for on or before April 12 once you are assigned an adversary TA)_


# Recipe Finder

## Division of Labor: 

- Georgia: backend class design/structure, creation of SQL database and database methods, graph classes and recommendation algorithm

- Andres: User authentication with firebase, authentication frontend pages and integration, user database functions and front end handlers, routing, autocorrect/validity for ingredients submission


- Jacob: Frontend design and implementation, React hook logic, axios requests/integration with backend, UI/UX research/testing, frontend debugging

- Jack: JUnit Testing, backend class design/structure, graph classes and recommendation algorithm, integration & implementation of handlers


## Known bugs:

- There are no known bugs in the program.

## Design Details specific to your code:

- User authentication:

- Frontend:
	- The frontend includes the following pages (excluding login and signup pages):
		- Fridge:
			- This page includes the components that make up a user's Fridge. It also acts as a landing page after authentication. This inlucdes the list of current ingredients that a user has as well as a way to input more ingredients into their Fridge. The input is limited to ingredients found in our database (through an implementation of autocorrect). There are also accessible links to each of the other pages from this page.
		- Recipe Selection:
			- This page displays a scrollable div that includes a list of recipes presented to the user (acquired from backend graph and similarity/ranking algorithms). It includes a photo (if the recipe has one) for each recipe, as well as its name and the chef who created the recipe. The page includes accessible links to return to the Fridge if wanted.
		- Recipe:
			- This is the display page for any recipe selected. The page will dynamically display the ingredients and preparations needed for each recipel, as well as 3 similar recipes (acquired from backend algorithm). The page includes accessible links to return to the Recipe Selection page, the Fridge page, or to opening a link including the recipe on the BBC's website.
		- Profile: 
			- This page displays information about the user. This includes an option to delete/change their account. It also lists all the recipes and ingredients the user has previously rated, as well as a way to change those ratings dynamically.

- Graph:

- Similarity Algorithm:

## How to Build and Run

- You can build the program by running ‘mvn package’ in the terminal while located in the root directory.

- You can run the program with the frontend component by running ‘./run --gui’ in the terminal while located in the root directory. You will also need to (in a separate terminal window) go into the 'recipe-finder' directory located in the 'src/main' and run ‘npm start’. This will ensure that the GUI starts with React.

## What browser you used to test your GUI
- We primarily used Google Chrome to test Recipe Finder.



