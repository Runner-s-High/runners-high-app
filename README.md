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
Runner's High is a mood-tracking and run-tracking app that allows users to log their mood both before and after they go running. It also logs the user's run through location services and provides information on their pace and route. User will be able to look back at data from previous runs and see how their mood has changed over time.

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

- [x] User will report their mood before and after the run using radiobuttons that quantify various emotions-stress,anxiety etc. 
- [x] User will be able to track their run live and review it after finishing
- [x] User will be able to log in/sign up into their account

**Optional Nice-to-have Stories**

- [ ] Directions while running
- [ ] Announcements of running stats (text-to-speech)
- [ ] Setting end destination
- [ ] Being able to see graph of emotional progress over time
- [x] Being able to see relationship between pre-run mood and run performance or mood post-run and run performance.-definitely optional for now
- [ ] Implement map so user can see live position while running
- [x] Implement links to mental health links/phone numbers

### First App Walkthough GIF
<img src="https://github.com/Runner-s-High/runners-high-app/blob/main/first%20walkthrough.gif" width=250><br>

### Second App Walkthough GIF
<img src="https://github.com/Runner-s-High/runners-high-app/blob/main/2nd%20walkthrouhg.gif" width=250><br>




### 2. Screen Archetypes

* Home Screen/Start Screen
   * Display data on past run and mood
   * Display a button to start a new run
* Mood Logging Screen (Create Screen)
   * Features radio buttons to allow user to track anxiety,stress,overall mood on scales 1-10 and journal anything else on their mind if they choose.
* Settings Screen
    * Allows user to toggle TTS, when announcements are made, (etc.)

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Login Screen
* Home Screen
* Data visuals screen. Graphs etc
* Mood Logging Screen
* Map/Running Screen

**Flow Navigation** (Screen to Screen)

* Login Screen
    * => Home Screen (successful login/sign up)
* Settings Screen
   * => Back to Homescreen
* Home Screen
   * => Maps/Running Screen (Go for run button) 
   * => Settings Screen (Settings button in action bar)
* Running Screen
    * => Mood Screen (when starting and ending run)
* Settings Screen
    * => Back button to home screen

## Wireframes
<img src="https://i.imgur.com/I8yV65c.jpg" width=600>

## Schema
### Models

**RunLog**
| Property | Type         | Description                    |
| -------- | ------------ | ------------------------------ |
| objectId | String       | ObjectId stored in Parse       |
| preMood  | Mood         | Mood before the run            |
| postMood | Mood         | Mood after the run             |
| Path     | List<LatLng> | Path of the run                |
| Time     | int          | Text                           |
| Date     | int          | Date of run                    |
| Distance | double       | Distance of run                |
| Note     | String       | Extra notes about the run/mood |

**Mood**
| Property | Type   | Description        |
| -------- | ------ | ------------------ |
| objectId | String | ObjectId for Parse |
| Anxiety  | int    | level of anxiety   |
| Stress   | int    | level of stress    |
| Overall  | int    | overall vibes      |

**LatLng**
| Property  | Type   | Description          |
| --------- | ------ | -------------------- |
| Latitude  | double | a point of latitude  |
| Longitude | double | a point of longitude |



### Networking
- Login Screen-requests to sign up, log in, check if logged in
- Home Screen-displays all data for most recent run
- Data Screen-displays all data for all runs; user can delete run
- Running Screen-posts run to Parse when finished
- Settings Screen-Log out

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

