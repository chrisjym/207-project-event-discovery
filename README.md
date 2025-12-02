#csc-207-project-event-discovery
# 207 Final Project: Ticket Monster
- Have you ever wanted to find local events quick and fast near you? Well, look no further! Ticket Monster is here to help you find places to laugh, cry, and have an overall good time in no time at all!


# Table of Contents
- [Overview](#overview)
- [Usage Guide](#usage-guide)
- [Contributors](#contributors)
- [PR Style](#style)
- [Setup](#setup)
- [Features](#features)
- [Links](#links)


<h1></h1>

# Overview:
Event Gate: This project acts like an alternative to Ticketmaster, allowing users to search for local events, view them through a calendar, and saving these events into memory for viewing at later times.


# Features
- Users can create accounts with a personal username and password to be stored into local memory
- Users can search for events near them based on location
- Users can categorize the events they search for based on specific categories
- Users can view events in a calendar format, viewing events based off of date
- Users can save events to be viewed later
- Users can view the description of the events


# Contributors
1. Christopher Mong
2. Joyi Xue
3. Min-Hsuan (Angelie) Wu
4. Yifei Yang



# Style
- [chore]: Used to represent menial tasks that were simple fixes or style cleanup.
- [feat/Feature]: used to represent a large task that is a new feature to the program.

# Setup
In order to properly use the Ticketmaster API, you must sign up to Ticketmaster's open source developer site; then you will receive an API Key from the Discovery API. This should be substituted into the EventDataAccessObject's API_KEY constant. Only from here will you have access to real time events from Ticketmaster.

<img width="739" height="114" alt="Screenshot 2025-11-24 at 6 59 17 PM" src="https://github.com/user-attachments/assets/543518f4-4a39-4e21-8520-e2e40281e90e" />

# Usage Guide
## Features

### Searching for Events

#### Login/Signup
1. Launch the application
2. You will see a sign up page
3. Sign up, or navigate to the login page to login to a pre-existing user
4. Click the log in/sign up button
   
<img width="927" height="727" alt="Screenshot 2025-12-01 at 10 22 06 PM" src="https://github.com/user-attachments/assets/1df4b3ce-ba4e-40e7-a815-14021a9d4721" />

<img width="923" height="725" alt="Screenshot 2025-12-01 at 10 22 35 PM" src="https://github.com/user-attachments/assets/c7bfe609-f156-46a2-b728-0b4df58078d9" />

#### Via Search Bar
1. Sign Up/ Log In
2. You'll see a search bar at the top of the dashboard
3. Type an event name (e.g., "Raptors", "Drake", "Hamilton")
4. Press **Enter** or click the search button
5. The app will display matching events near your default location (Toronto)
   
<img width="327" height="51" alt="Screenshot 2025-12-01 at 10 22 58 PM" src="https://github.com/user-attachments/assets/45d60006-f98a-484d-8a92-dba02229c07d" />

#### Via Location
1. Sign Up / Log In
2. If you wish to change the location in viewing events, click the search bar at the top of the program
3. Type in anything from a specific address to a general city

<img width="926" height="41" alt="Screenshot 2025-12-01 at 10 23 13 PM" src="https://github.com/user-attachments/assets/967c8423-90c1-4f67-bced-b11dea2f8698" />

#### ViA Category
1. Sign Up / Log In
2. If you wish to diplay events by different categories, click the category drop down
3. Change it to the category you want

#### ViA Distance/Datetime/Name
1. Sign Up / Log In
2. If you wish to diplay events by Distance/Datetime/Name, click the Distance drop down which is Distance default
3. Change to the events which you want these to be sorted by

<img width="601" height="40" alt="Screenshot 2025-12-01 at 10 23 52 PM" src="https://github.com/user-attachments/assets/854b4c44-441a-4276-a66c-9076d45a09b3" />


#### ViA Calender
1. Sign Up / Log In
2. Click on the calender button at the left side of the dashboard
3. You will see a calendar view,
4. Clicking on a specific date shows all the events occurring on that day

<img width="917" height="721" alt="Screenshot 2025-12-01 at 10 24 06 PM" src="https://github.com/user-attachments/assets/e9ed034e-4f71-48c0-b979-c6037a401e97" />


**Example Searches:**
- `Raptors` → Find Toronto Raptors basketball games
- `Concert` → Find all concerts in your area

#### Changing Search Location
The default search location is Toronto, ON.

## Links
MVP Planning: https://docs.google.com/document/d/1tQHwKZNdJIcIRBtkS7yiY-cOgxgPninWoLvrRGcAmMo/edit?usp=sharing
Slides: https://docs.google.com/presentation/d/1xiP1yBKb-so6dTxyAJHRgGxrDK1V3najGgcjckkXNWE/edit?slide=id.g3ab8aa55bac_2_0#slide=id.g3ab8aa55bac_2_0

## Future Plans:
The next key step for this idea to launch, is implementing a way for users to actually purchase the ticket on the program. Although this is not possible through Ticketmaster as it is strictly forbidden to use their events with a third-party payment, there are other possible ways to fetch events, and even more to implement a payment method! We would use an API like Stripe, easy to learn and very well documented.

#### Special Thanks to: Professor Calver, Professor Gries, TA Wang for helping us along our journey!



