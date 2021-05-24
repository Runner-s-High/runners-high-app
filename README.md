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

### Current Wireframe (May 29, 2021)

## Schema
### Models

**RunData (stored in Parse backend)**
| Property | Type         | Description                    |
| -------- | ------------ | ------------------------------ |
| objectId | String       | ObjectId stored in Parse       |
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

### Networking
- LoginActivity-requests to sign up, log in, check if logged in
- MainActivity: HomeFragment-Queries most recent 10 runs
- MainActivity: HistoryFragment-Queries all of user's runs
- MainActivity: FeedFragment-Queries first 25 runs from amongst all users
- MainActivity: PostRunFragment-Saves current run to backend
- MainActivity: SettingsFragment-Log out current user, update user profile picture

**Login Activity**
- (GET) Login check network request (when user presses login button)
```java=
ParseUser.logInInBackground(USERNAME, PASSWORD, new LogInCallback() {
    @Override
    public void done(ParseUser user, ParseException e) {
        if(user != null) {
            //Successful login
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            //LogIn failed
            //Display message about incorrect login
            Log.e("LoginActivity", e.getMessage());
        }
    }
});
```
- (POST) SignUp network request (when user presses signup button)
```java=
ParseUser user = new ParseUser();

user.setUsername(USER_TEXT_STRING);
user.setPassword(PASS_TEXT_STRING);

user.signUpInBackground(new SignUpCallback() {
    @Override
    public void done(ParseException e) {
        if (e == null) {
          //User successfully signed up
          startActivity(new Intent(this, MainActivity.class));
        } 
        else {
          //Error signing up
          //Print error message to user
          Log.e("LoginActivity", e.getMessage());
        }
    }
});
```
- (GET) Check if logged in already
```java=
if(ParseUser.getCurrentUser() != null) {
    startActivity(new Intent(this, MainActivity.class));
}
```

**Settings Activity**
- (DELETE) Log out current user
```java=
ParseUser.logOut();
ParseUser currentUser = ParseUser.getCurrentUser();
//Return to login screen
```

**Run Log (Main Activity)**
- (GET) Querying all runs to display in RecyclerView
    - Note that for the most recent run that will be displayed on the Home Fragment, we can take the first run from the list returned from this query
```java=
ParseQuery<Run> query = ParseQuery.getQuery(Run.class);
query.include(Post.KEY_USER);
query.addDescendingOrder(POST_CREATED_AT);
query.findInBackground(new FindCallback<Run>(){
    @Override
    public void done(List<Run> queryRuns, ParseException e) {
        if(e!=null){
            Log.e(TAG, "Error querying runs",e );
            //something has gone wrong
            return;
        }
        runs.addAll(queryruns);
        runAdapter.notifyDataSetChanged();
    }
});  
```
- (DELETE) Delete run from Parse (user wants to remove past run)
```java=
//Note: We will have references to objects through List<Run>
object.deleteInBackgroud(new DeleteCallback() {
    @Override
    public void done(ParseException e) {
        if(e == null) {
            //Run successfully deleted
            //Remove run from List<Run>
            runAdapter.notifyDataSetChanged();
        }
        else {
            //Error deleting object; tell user
            Log.e("MainActivity", e.getMessage());
        }
    }
});
```

**Running/Map Fragment**
- (POST) Save run request (after user ends run)
```java=
//Run is a ParseObject
Run run = new Run();

//These calls can be handled with object's methods
run.put("preMood", PRE_MOOD_OBJECT);
run.put("postMood", POST_MOOD_OBJECT);
run.put("path", LOCATION_DATA_FOR_RUN_PATH);
run.put("time", RUN_TIME);
run.put("date", DATE_OF_RUN);
run.put("distance", RUN_DISTANCE);
run.put("note", RUN_NOTE);

run.saveInBackground(new SaveCallback() {
    @Override
    public void done(ParseException e) {
        if(e == null) {
            //Update the List<Run> we have
            runAdapter.notifyDataSetChanged();
        }
        else {
            //Run could not be properly saved
            //Print error message to inform user
            Log.e("MainActivity", e.getMessage());
        }
    }
});
```

