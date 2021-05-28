CodePath App Development Project
===

# Runner's High

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Runner's High is a mood-tracking and run-tracking app that allows users to log their mood both before and after they go running. It also logs the user's run through location services and provides information on their pace and route. User will be able to look back at data from previous runs and see how their mood and stress levels have changed over time.

### Rationale
The CDC estimates that more than 60% of U.S. adults don’t get enough exercise and that 25% of U.S. adults don’t exercise at all. Our goal was to create a full fitness app that also had a psychological component in that users would rate their mood and level of stress before and after each run.Users would be able to see not only the physical benefits of exercising (calories burned), but also the psychological benefits of exercise first hand. The end goal is that more people would ultimately start exercising and continue exercising because of this app.
“Has technology made us lazier?” They ask….not this time.


### App Evaluation
- **Category:** Health and Fitness.
- **Mobile:** Having the phone with them allows a user to track their run.
- **Story:** Allows users to track their mood and see the effect of exercise on their mood both long term and short term.
- **Market:** Anyone invested in their physical or mental health would enjoy this app.
- **Habit:** Users with a regular exercise routine would make use of the app in accordance with their routine. The app would also influence people to develop a routine by showing promising results.
- **Scope:** The app is still in the developmental phase. 

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [x] User will be able to report their mood/stress before and after the run
- [x] User will be able to track their run live and review it after finishing
- [x] User will be able to log in/sign up into their account through querying the backend

**Optional Nice-to-have Stories**

- [ ] Achievement system
- [x] Allowing users to share progress
- [x] Changing units between miles (mi) and Kilometers (km)
- [ ] Allowing user to set end destination and get directions while running
- [ ] Announcements of running stats (text-to-speech)
- [ ] Being able to see graph of emotional progress over time
- [x] Being able to see relationship between pre-run mood and run performance or mood post-run and run performance.
- [ ] Implement map so user can see live position while running
- [x] Implement links to mental health links/phone numbers

### First Sprint Walkthough GIF (March 22, 2021)
<img src="https://github.com/Runner-s-High/runners-high-app/blob/main/first%20walkthrough.gif" width=250><br>

### Second Sprint Walkthough GIF (March 24, 2021)
<img src="https://github.com/Runner-s-High/runners-high-app/blob/main/2nd%20walkthrouhg.gif" width=250><br>

### Third Sprint Walkthough GIF (March 29, 2021)
<img src="https://github.com/Runner-s-High/runners-high-app/blob/main/walkthrough%203.gif" width=250><br>

### Fourth Sprint Walkthrough GIF (April 5, 2021)
<img src="https://github.com/Runner-s-High/runners-high-app/blob/main/walkthrough4.gif" width=250><br>

### Demo Day Walkthrough GIF (May 29, 2021)
<img src="https://github.com/Runner-s-High/runners-high-app/blob/main/walkthrough5.gif" width=250><br>

### 2. Screen Archetypes

* Splash Screen
    * Briefly shown while app is loading 
* Login/Register Screen
    * Login/registration fields 
* Home Screen/Start Screen
    * Display data on past runs
    * Display a button to start a new run
* Stream Screens
    * Display user's personal runs
    * Display feed of everyone's runs
* Detail Screen
    * Display run statistics and graphs of how mood/stress has changed
* Creation Screen
    * User can start run
    * User fills out post-run information   
* Settings Screen
    * Allows user to toggle units, profile picture

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* MainActivity
  - HomeFragment (Welcome and Past Runs Summary)
  - StartRunFragment (Starting runs)
  - FeedFragment (everyone's runs)
  - HistoryFragment (List of past runs)
  - ResourcesFragment (Mental health resources)
  - SettingsFragment (Distance units, profile picture)
* MoreInfoActivity: ResultsFragment
  - StatsFragment
  - GraphsFragment

**Flow Navigation** (Screen to Screen)

* LoginActivity
    * => MainActivity: HomeFragment (successful login)
    * => SetupProfileActivity (successful sign-up)
* SetupProfileActivity
    * => MainActivity: HomeFragment (Submit Profile button) 
* MainActivity: StartRunFragment
    * => PreRunMoodDialogFragment (Start Run button)
    * => RunningFragment (after finishing dialogfragment)
* MainActivity: FeedFragment
    * => MoreInfoActivity (More Info button)
* MainActivity: HistoryFragment
    * => MoreInfoActivity (More Info button)
* MainActivity: SettingsFragment
    * => Intent to Photo Gallery (Change Profile Image button)
    * => LoginActivity (Log Out button)  
* MainActivity: RunningFragment
    * => PostRunFragment (Stop button)
* MainActivity: PostRunFragment
    * => HistoryFragment (Save Run, Exit Without Saving buttons)
* MoreInfoActivity
    * => MainActivity: HistoryFragment (Go Back button)

## Wireframes
### Original Wireframe (March 15, 2021)
<img src="https://i.imgur.com/I8yV65c.jpg" width=600>

Note that the app layout has significantly changed since this first blueprint design.

## Schema
### Models

**RunData (stored in Parse backend)**
| Property | Type         | Description                    |
| -------- | ------------ | ------------------------------ |
| objectId | String       | ObjectId generated by Parse    |
| createdAt | Date        | Date that run was added to backend |
| updatedAt | Date        | Date of last update to run in backend |
| TheUser   | ParseUser*  | Pointer to user who created this run  |
| PreRunMood  | int        | Mood before the run            |
| PostRunMood | int         | Mood after the run             |
| PreRunStress | int       | Stress level before run        |
| PostRunStress | int      | Stress level after run        |
| RunDate     | String         | Date of run (e.g Monday, May 24, 2021) |
| RunTime     | String          | Time of run in mm:ss format |
| RunDistance | double       | Distance of run (in miles)  |
| RunNotes    | String       | Extra notes about the run/mood/stress |
| RunLatList  | double[]    | Array of latitude values defining path coordinates |
| RunLngList  | double[]    | Array of longitude values defining path coordinates |

**Comment (stored in Parse backend)**
| Propery | Type       | Description                         |
| ------- | ---------- | ----------------------------------- |
| objectId | String    | ObjectId generated by Parse         |
| createdAt | Date     | Date that run was added to backend  |
| updatedAt | Date     | Date of last update to run in backend |
| PostingUser | ParseUser* | Pointer to user who created this comment |
| Run        | RunData*    | Pointer to run being commented on |
| CommentText | String  | Text of the comment |

### Network Requests (using Parse)
- LoginActivity-requests to sign up, log in, check if logged in
- MainActivity: HomeFragment-Queries most recent 10 runs
- MainActivity: HistoryFragment-Queries all of user's runs
- MainActivity: FeedFragment-Queries first 25 runs from amongst all users, queries users for each run (if needed)
- MainActivity: PostRunFragment-Saves current run to backend
- MainActivity: SettingsFragment-Log out current user, update user profile picture
- MoreInfoActivity: CommentFragment-Queries all comments associated with this run, saves new comments, queries posting users for comment (if needed)

**Login Activity**
- (GET) Login check network request (when user presses login button)
```java=
//Attempt to login to user account through query to Parse backend
ParseUser.logInInBackground(username, password , (user, e) -> {
   if(user != null) {
       //Successful login
       Log.i(TAG, "Successful Parse Login; starting MainActivity");
       etUser.setText("");
       etPassword.setText("");

       //Transition to MainActivity
       startActivity(new Intent(LoginActivity.this, MainActivity.class));
       finish();
   }
   else {
       //LogIn failed
       //Display Toast to user
       Toast.makeText(LoginActivity.this, "Login Error", Toast.LENGTH_SHORT).show();
       Log.e(TAG, e.getMessage());
   }
});
```
- (POST) SignUp network request (when user presses signup button)
```java=
//Attempt to create new user entry in Parse backend
user.signUpInBackground(e -> {
   if (e == null) {
       //User successfully signed up
       Log.i(TAG, "User successfully signed up; starting OnboardingActivity");
       etUser.setText("");
       etPassword.setText("");

       //Transition to SetupProfile Activity
       startActivity(new Intent(LoginActivity.this, SetupProfileActivity.class));
       finish();
   }
   else {
       //Error signing up
       //Display Toast to user
       Toast.makeText(LoginActivity.this, "SignUp Error", Toast.LENGTH_SHORT).show();
       Log.e(TAG, e.getMessage());
   }
});
```
- (GET) Check if logged in already
```java=
//Checking if already logged in
if(ParseUser.getCurrentUser() != null) {
   Log.i(TAG, "User already logged in; starting MainActivity");
   startActivity(new Intent(this, MainActivity.class));
   finish();
}
```

**MainActivity: HomeFragment**
- (GET) Query 10 most recent runs
```java=
//Query the last 10 runs the user completed
ParseQuery<RunData> query=ParseQuery.getQuery(RunData.class);
query.include(RunData.KEY_USER);
query.setLimit(10);
query.whereEqualTo(RunData.KEY_USER, ParseUser.getCurrentUser());
query.addDescendingOrder(RunData.KEY_CREATED_AT);
query.findInBackground((runs, e) -> {
   if (e != null) {
       return;
   }

  //Perform math operations on the run stats
  //...
});
```

**MainActivity: HistoryFragment**
- (GET) Query all of a user's runs
```java=
ParseQuery<RunData> query = ParseQuery.getQuery(RunData.class);
query.include(RunData.KEY_USER);
query.whereEqualTo(RunData.KEY_USER, ParseUser.getCurrentUser());
query.addDescendingOrder(RunData.KEY_CREATED_AT);      //orders posts by time
query.findInBackground((theruns, e) -> {
   if (e!=null){
       return;
   }

   runs.addAll(theruns);
   RunAdapter.notifyDataSetChanged();
});
```

**MainActivity: FeedFragment**
- (GET) Query last 25 runs of current user
```java=
ParseQuery<RunData> query = ParseQuery.getQuery(RunData.class);
query.addDescendingOrder(RunData.KEY_CREATED_AT);
query.setLimit(25);
query.findInBackground((runs, e) -> {
   if(e!=null) {
       Toast.makeText(getContext(), "Error fetching runs", Toast.LENGTH_SHORT).show();
       return;
   }

   runFeed.addAll(runs);
   feedAdapter.notifyDataSetChanged();
});
```

- (GET) Optional query of user
```java=
ParseUser runUser = run.getUser();
try {
    runUser = runUser.fetchIfNeeded();
} catch (ParseException e) {
    e.printStackTrace();
}
userString = runUser.getUsername();
```

**MainActivity: PostRunFragment**
- (POST) Save finished run to backend
```java=
runData.saveInBackground(e -> {
   if(e!=null) {
       Toast.makeText(context, "Error Saving Run", Toast.LENGTH_SHORT).show();
   }
   else
       Toast.makeText(context, "Run Saved", Toast.LENGTH_SHORT).show();
});
```

**MainActivity: SettingsFragment**
- (GET) Log out current user
```java=
ParseUser.logOut();
ParseUser currentUser = ParseUser.getCurrentUser();
//Return to login screen
```

- (PATCH) Update user profile picture

**MainActivity: CommentFragment**
- (GET) Get all comments for given run
```java=
ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
query.whereEqualTo(Comment.KEY_RUN, run);
query.addAscendingOrder(Comment.KEY_CREATED_AT);
query.findInBackground((commentList, e) -> {
   if(e != null)
       return;

   comments.addAll(commentList);
   commentAdapter.notifyDataSetChanged();
});
```

- (POST) Save new comment to backend
```java=
comment.saveInBackground(e -> {
    if(e != null)
        return;

    Toast.makeText(getContext(), "Comment saved", Toast.LENGTH_SHORT).show();
    comments.add(comment);
    commentAdapter.notifyDataSetChanged();
});
```

- (GET) Optional query of comment posting users
```java=
ParseUser postingUser = comment.getPostingUser();
try {
    postingUser = postingUser.fetchIfNeeded();
} catch (ParseException e) {
    e.printStackTrace();
}
userString = postingUser.getUsername();
```
