#csc-207-project-event-discovery
207 Final Project

# Table of Contents
- [Overview](#overview)
- [Usage Guide](#guide)
- [Contributors](#contributors)
- [Demo/Screenshots](#demo)
- [PR Style](#style)
- [Setup](#setup)


<h1></h1>

# Overview:
This project acts like an alternative to Ticketmaster, allowing users to search for local events, view them through a calendar, and saving these events into memory for viewing at later times.

# TODOs



# File Structure
- Users can create accounts with a personal username and password to be stored into local memory
- Users can search for events near them based on location
- Users can categorize the events they search for based on specific categories
- Users can view events in a calendar format, viewing events based off of date
- Users can save events to be viewed later
- Users can view the description of the events


#Contributors:
1. Christopher Mong



# PR Style:
- [chore]: Used to represent menial tasks that were simple fixes or style cleanup.

# Initial Setup:
In order to properly use the Ticketmaster API, you must sign up to Ticketmaster's open source developer site; then you will receive an API Key from the Discovery API. This should be substituted into the EventDataAccessObject's API_KEY constant. Only from here will you have access to real time events from Ticketmaster.

<img width="739" height="114" alt="Screenshot 2025-11-24 at 6 59 17 PM" src="https://github.com/user-attachments/assets/543518f4-4a39-4e21-8520-e2e40281e90e" />

# Usage Guide:
1. Build and run the application

---

## How to Use

### Searching for Events

#### Via Search Bar
1. Launch the application
2. You'll see a search bar at the top of the dashboard
3. Type an event name (e.g., "Raptors", "Drake", "Hamilton")
4. Press **Enter** or click the search button
5. The app will display matching events near your default location (Toronto)

**Example Searches:**
- `Raptors` → Find Toronto Raptors basketball games
- `Concert` → Find all concerts in your area

#### Changing Search Location
The default search location is Toronto, ON. To change it:


