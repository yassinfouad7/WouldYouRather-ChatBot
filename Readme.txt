Would You Rather Chatbot
========================

Project Overview
----------------

This project is a Scala 3 terminal-based chatbot that plays a "Would You Rather" game with the user. The chatbot asks the user choice-based questions, lets them answer with option A or B, remembers their preferences, recommends future questions based on their choices, and keeps track of the conversation history.

The project was built using a declarative and functional programming approach. Instead of relying on mutable global variables, the chatbot passes an immutable ConversationState through the program. Each user input produces a response and a new updated state.

The main goal of the project is to demonstrate important Scala and functional programming concepts such as case classes, enums, pattern matching, immutable collections, higher-order functions, Option handling, currying, lazy evaluation, and pure helper functions.


Project Idea
------------

The chatbot acts as a simple game host. The user can choose a category and difficulty, then the bot asks a matching Would You Rather question.

Example:

User: funny
Bot: Got it. I saved your preferred category as: funny

User: hard
Bot: Got it. I saved your preferred difficulty as: hard

User: start
Bot:
Would you rather:
A) accidentally laugh during every serious conversation
B) speak only in dramatic movie quotes forever
Category: funny, Difficulty: hard

User: A
Bot: You chose option A. Nice choice!

The chatbot can also recommend questions, summarize the chat, show conversation history, and detect the user's mood based on simple keywords.


Main Features
-------------

1. Greeting and Help Menu
   - The chatbot greets the user when the program starts.
   - The user can type "help" to see all available commands.

2. Would You Rather Questions
   - The chatbot asks questions with two options: A and B.
   - Each question has a category, difficulty, and tags.

3. Categories
   The chatbot supports these categories:
   - funny
   - scary
   - deep
   - adventure
   - school
   - money
   - superpower
   - social
   - random

4. Difficulty Levels
   The chatbot supports:
   - easy
   - medium
   - hard

5. User Choices
   - The user can answer with A or B.
   - The answer is stored as a UserChoice object.
   - The chatbot remembers the category of the answered question.

6. Recommendation Engine
   - The recommendation engine selects questions from the question bank.
   - It uses the user's saved preferences, such as category and difficulty.
   - It avoids already answered questions when possible.
   - It explains why a question was recommended.

7. Conversation Memory
   - The chatbot logs each interaction.
   - It stores the user input, bot response, detected intent, sequence number, and timestamp.
   - The user can type "history" to view previous messages.

8. Summary
   - The user can type "summary" to see:
     - number of conversation turns
     - number of answered questions
     - saved preferences

9. Mood Detection
   - The user can type "mood" to check the detected mood.
   - The chatbot checks previous user messages for positive or negative words.
   - It returns Happy, Negative, or Neutral.

10. Exit
   - The user can type "bye", "exit", or "quit" to stop the chatbot.


How to Run
----------

Make sure Scala 3 and sbt are installed.

From the project folder, run:

sbt run

Then interact with the chatbot in the terminal.


Example Commands
----------------

help
start
funny
scary
deep
easy
medium
hard
A
B
recommend
summary
history
mood
bye


Example Conversation
--------------------

Bot: Hi! I am WYRBot. Type 'help' to see commands.

User: help
Bot: Here are the commands you can use...

User: funny
Bot: Got it. I saved your preferred category as: funny

User: hard
Bot: Got it. I saved your preferred difficulty as: hard

User: start
Bot:
Would you rather:
A) accidentally laugh during every serious conversation
B) speak only in dramatic movie quotes forever
Category: funny, Difficulty: hard

User: A
Bot: You chose option A. Nice choice!

User: recommend
Bot:
Would you rather:
A) hear whispers every night
B) see shadows moving in your room
Category: scary, Difficulty: hard

I recommended this because it is a hard scary question with these tags: ghost, creepy, horror.

User: summary
Bot: We have talked for 6 turn(s). You answered 1 question(s). Preferences: category = funny, difficulty = hard, lastCategory = funny

User: history
Bot: Shows the previous logged interactions.

User: bad
Bot: I did not understand. Try: start, funny, scary, easy, hard, recommend, summary, A, B, or bye.

User: mood
Bot: Your current mood seems Negative.

User: bye
Bot: Thanks for playing. See you next time!


Project Structure
-----------------

src/main/scala/
|
|-- Main.scala
|
|-- chatbot/
|   |-- ChatbotCore.scala
|
|-- data/
|   |-- QuestionBank.scala
|
|-- engine/
|   |-- QuestionRecommender.scala
|
|-- memory/
|   |-- ConversationTracker.scala
|
|-- models/
    |-- ConversationState.scala
    |-- InteractionEntry.scala
    |-- PlayerIntent.scala
    |-- UserChoice.scala
    |-- WouldYouRatherQuestion.scala


File Descriptions
-----------------

Main.scala
----------

This is the entry point of the program. It creates the initial ConversationState, prints the greeting message, reads user input from the terminal, sends the input to ChatbotCore, prints the response, and continues the chat loop.

It uses recursion to keep the chatbot running.

Important responsibility:
- start the chatbot
- read terminal input
- stop when the user types bye, exit, or quit
- pass the updated state to the next loop


ChatbotCore.scala
-----------------

This is the main brain of the chatbot. It receives user input, parses it, detects the user's intent, and decides what action should happen.

It handles:
- greetings
- starting the game
- category selection
- difficulty selection
- A/B answers
- recommendations
- summary
- history
- mood
- help
- unknown input

Important functions:
- greetUser()
- parseInput(input)
- isExitInput(input)
- detectIntent(input)
- handleUserInput(input, state)
- extractCategory(input)
- extractDifficulty(input)
- handleChoice(selectedOption)(state)
- formatQuestion(question)
- helpMessage()


QuestionBank.scala
------------------

This file stores the actual Would You Rather questions.

Each question is represented as a WouldYouRatherQuestion case class with:
- id
- category
- difficulty
- optionA
- optionB
- tags

The question bank is stored as an immutable lazy val list. Lazy evaluation means the list is only created when it is first used.

The question bank contains easy, medium, and hard questions for all supported categories.


QuestionRecommender.scala
-------------------------

This file contains the recommendation logic.

It does not store questions itself. Instead, it receives questions from QuestionBank and filters them based on the user's preferences.

It uses:
- category preference
- difficulty preference
- previous user choices
- unanswered questions

Important functions:
- getUserPreferences(state)
- updatePreference(key)(value)(state)
- recommend(preferences, questions)
- pickQuestion(questions, state)
- explainRecommendation(question)
- favoriteCategoryFromChoices(choices)
- updateCategoryFromChoices(state)

The recommender first tries to find an exact match for both category and difficulty. If no exact match is available, it falls back to difficulty match, then category match, then any unanswered question.


ConversationTracker.scala
-------------------------

This file handles memory and context tracking.

It logs every user-bot interaction using InteractionEntry. It also provides functions for summary, history, repeated query checking, topic extraction, and mood detection.

Important functions:
- logInteraction(userInput, botResponse, intent, state)
- isRepeating(input, state)
- findTopics(state)
- summarizeConversation(state)
- getMood(state)
- getUserMood(history)
- formatHistory(history)

The chatbot uses this file to remember previous messages and give more useful responses.


Models
------

The models folder contains the data structures used by the whole project.

ConversationState.scala
-----------------------

Stores the full state of the chatbot.

Fields:
- history: previous interactions
- preferences: saved user preferences
- choices: previous A/B answers
- currentQuestion: the active question waiting for an answer

The chatbot updates this using state.copy(...) instead of changing the old state directly.


InteractionEntry.scala
----------------------

Represents one interaction between the user and the bot.

Fields:
- sequenceNumber
- userInput
- botResponse
- detectedIntent
- timestamp


PlayerIntent.scala
------------------

An enum that represents what the user wants to do.

Examples:
- Greeting
- StartGame
- ChooseA
- ChooseB
- SetCategory
- SetDifficulty
- Recommend
- Summary
- History
- Mood
- Help
- Unknown


UserChoice.scala
----------------

Stores one A/B answer from the user.

Fields:
- questionId
- selectedOption
- category
- tags


WouldYouRatherQuestion.scala
----------------------------

Represents one Would You Rather question.

Fields:
- id
- category
- difficulty
- optionA
- optionB
- tags


Functional Programming Concepts Used
------------------------------------

1. Immutability

The project avoids mutable global variables. Instead of changing the old state, it creates a new updated state.

Example:

state.copy(preferences = state.preferences + ("category" -> "funny"))

2. Case Classes

Case classes are used to represent structured data, such as questions, user choices, conversation entries, and conversation state.

3. Enum / ADT

PlayerIntent is an enum used to represent the user's intent clearly and safely.

4. Pattern Matching

Pattern matching is used in ChatbotCore to decide what action should happen based on the detected intent.

Example:
- Greeting
- StartGame
- ChooseA
- Recommend
- Summary
- Unknown

5. Option

Option is used for safe handling of values that may or may not exist.

Example:
- currentQuestion: Option[WouldYouRatherQuestion]
- extractCategory(input): Option[String]
- extractDifficulty(input): Option[String]

This avoids using null.

6. Higher-Order Functions

The project uses many higher-order functions, including:
- map
- filter
- exists
- count
- groupBy
- sortBy
- flatMap
- getOrElse

7. Currying

Some helper functions are curried, meaning they receive parameters in multiple groups.

Example:

updatePreference("category")("funny")(state)

8. Lazy Evaluation

The question bank uses lazy val, so the list of questions is only created when first accessed.

9. Declarative Style

The code focuses on describing what should be selected or transformed instead of manually controlling every loop.

Example:

questions.filter(question => question.difficulty == "hard")


Testing
-------

The following test flow was used:

1. help
   Expected: shows all commands.

2. funny
   Expected: saves category as funny.

3. hard
   Expected: saves difficulty as hard.

4. start
   Expected: gives a funny hard question.

5. A
   Expected: saves the user's choice.

6. recommend
   Expected: recommends a question based on available preferences and unanswered questions.

7. summary
   Expected: shows number of turns, number of choices, and preferences.

8. history
   Expected: shows previous conversation logs with intents.

9. bad
   Expected: logged as user input.

10. mood
    Expected: detects negative mood.

11. bye
    Expected: exits the chatbot.


Known Limitations
-----------------

- The chatbot is rule-based, not AI-generated.
- It only understands predefined keywords.
- Mood detection is simple and keyword-based.
- The question bank is stored directly in code, not in a database or file.
- Recommendations depend on available questions in the question bank.
- If the exact category and difficulty are already answered or unavailable, the recommender falls back to another suitable question.


Future Improvements
-------------------

Possible future improvements include:

- Add a graphical user interface.
- Add multiplayer mode.
- Add scoring or statistics.
- Store questions in a JSON or database file.
- Add more questions and categories.
- Improve mood detection.
- Improve repeated query detection.
- Add randomization to question selection.
- Add user profiles that persist after closing the program.


Team Roles
----------

Member A: Core Chatbot and Integration
- Worked on Main.scala and ChatbotCore.scala.
- Connected the chatbot with the question bank, recommender, and conversation tracker.
- Handled input parsing, intent detection, and the main conversation flow.

Member B: Recommendation Engine
- Worked on QuestionRecommender.scala.
- Implemented filtering, question selection, preference handling, and recommendation explanations.

Member C: Conversation Memory
- Worked on ConversationTracker.scala and interaction logging.
- Implemented history, summary, mood detection, and topic tracking.

Member D: Question Bank and Documentation
- Worked on QuestionBank.scala.
- Added questions for all categories and difficulty levels.
- Helped with testing, README, and demo preparation.


Conclusion
----------

This project demonstrates a functional Scala 3 approach to building a rule-based chatbot. The chatbot uses immutable data, pattern matching, case classes, enums, Option, higher-order functions, currying, and lazy evaluation. It provides a complete Would You Rather game experience with question recommendations, conversation memory, summaries, history, and mood detection.