# Would You Rather Chatbot

A Scala 3 terminal chatbot that plays a **Would You Rather** game with the user.  
The bot asks questions, stores user choices, saves preferences, recommends questions, tracks conversation history, summarizes the chat, and detects mood using simple keyword analysis.

---

## Project Overview

This project was built for an Advanced Programming course using **Scala 3**, with a focus on declarative and functional programming.

The chatbot is rule-based. It does not use AI-generated responses. Instead, it detects the user's intent from keywords and responds according to predefined rules.

The main purpose of the project is to demonstrate:

- Case classes
- Enums / ADTs
- Pattern matching
- Immutable data structures
- Higher-order functions
- Option handling
- Currying
- Lazy evaluation
- Recursive program flow
- Functional state updates using `copy()`

---

## Features

### Core Chatbot

The chatbot can:

- Greet the user
- Show a help menu
- Read terminal input
- Detect user intent
- Handle unknown input
- Exit politely

### Would You Rather Game

The chatbot asks Would You Rather questions with two options:

```text
A) First option
B) Second option
````

Each question has:

* ID
* Category
* Difficulty
* Option A
* Option B
* Tags

### Categories

The chatbot supports:

* funny
* scary
* deep
* adventure
* school
* money
* superpower
* social
* random

### Difficulty Levels

The chatbot supports:

* easy
* medium
* hard

### Recommendation System

The recommendation engine selects questions based on:

* saved category preference
* saved difficulty preference
* previously answered questions
* available unanswered questions

The recommender tries to pick questions in this order:

1. Same category and same difficulty
2. Same difficulty only
3. Same category only
4. Any unanswered question
5. First question as a fallback

### Conversation Memory

The chatbot logs each interaction using:

* sequence number
* user input
* bot response
* detected intent
* timestamp

The user can type:

```text
history
```

to view previous messages.

### Summary

The user can type:

```text
summary
```

to see:

* number of conversation turns
* number of answered questions
* saved preferences

### Mood Detection

The user can type:

```text
mood
```

The chatbot checks previous user messages for positive and negative words.

It returns:

* Happy
* Negative
* Neutral

---

## How to Run

Make sure Scala 3 and sbt are installed.

From the project folder, run:

```bash
sbt run
```

Then type commands in the terminal.

---

## Demo Input

Use this sequence for a demo video:

```text
help
funny
hard
start
A
recommend
summary
history
bad
mood
bye
```

---

## Example Conversation

```text
Bot: Hi! I am WYRBot. Type 'help' to see commands.

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
Bot: Shows previous logged interactions.

User: bad
Bot: I did not understand. Try: start, funny, scary, easy, hard, recommend, summary, A, B, or bye.

User: mood
Bot: Your current mood seems Negative.

User: bye
Bot: Thanks for playing. See you next time!
```

---

## Project Structure

```text
src/main/scala/
│
├── Main.scala
│
├── chatbot/
│   └── ChatbotCore.scala
│
├── data/
│   └── QuestionBank.scala
│
├── engine/
│   └── QuestionRecommender.scala
│
├── memory/
│   └── ConversationTracker.scala
│
└── models/
    ├── ConversationState.scala
    ├── InteractionEntry.scala
    ├── PlayerIntent.scala
    ├── UserChoice.scala
    └── WouldYouRatherQuestion.scala
```

---

## File Descriptions

### `Main.scala`

This is the entry point of the program.

It is responsible for:

* creating the initial `ConversationState`
* printing the greeting message
* reading user input
* calling `ChatbotCore`
* printing bot responses
* keeping the chat loop running
* stopping when the user types `bye`, `exit`, or `quit`

The chat loop is recursive. Each turn receives a state and passes the updated state to the next turn.

---

### `ChatbotCore.scala`

This file contains the main chatbot logic.

It is responsible for:

* parsing user input
* detecting user intent
* handling commands
* updating preferences
* starting the game
* handling A/B choices
* calling the recommender
* calling the memory tracker
* returning the bot response and updated state

Important functions:

```scala
greetUser()
parseInput(input)
isExitInput(input)
detectIntent(input)
handleUserInput(input, state)
extractCategory(input)
extractDifficulty(input)
handleChoice(selectedOption)(state)
formatQuestion(question)
helpMessage()
```

---

### `QuestionBank.scala`

This file stores all Would You Rather questions.

Each question is represented by the `WouldYouRatherQuestion` case class.

Each question has:

* `id`
* `category`
* `difficulty`
* `optionA`
* `optionB`
* `tags`

The question bank uses:

```scala
lazy val questions
```

This means the question list is created only when it is first needed.

---

### `QuestionRecommender.scala`

This file contains the recommendation logic.

It does not store questions itself.
Instead, it receives questions from `QuestionBank` and filters them based on the current `ConversationState`.

It is responsible for:

* reading user preferences
* filtering by category
* filtering by difficulty
* avoiding answered questions
* choosing the best available question
* explaining recommendations

Important functions:

```scala
getUserPreferences(state)
updatePreference(key)(value)(state)
recommend(preferences, questions)
pickQuestion(questions, state)
explainRecommendation(question)
favoriteCategoryFromChoices(choices)
updateCategoryFromChoices(state)
```

---

### `ConversationTracker.scala`

This file handles memory and context tracking.

It is responsible for:

* logging interactions
* storing conversation history
* summarizing the conversation
* formatting history
* detecting mood
* extracting topics
* checking repeated inputs

Important functions:

```scala
logInteraction(userInput, botResponse, intent, state)
summarizeConversation(state)
formatHistory(history)
getMood(state)
getUserMood(history)
findTopics(state)
isRepeating(input, state)
```

---

## Models

### `ConversationState.scala`

Stores the full chatbot state.

Fields:

```scala
history: List[InteractionEntry]
preferences: Map[String, String]
choices: List[UserChoice]
currentQuestion: Option[WouldYouRatherQuestion]
```

The chatbot updates this state immutably using:

```scala
state.copy(...)
```

---

### `InteractionEntry.scala`

Stores one user-bot interaction.

Fields:

```scala
sequenceNumber: Int
userInput: String
botResponse: String
detectedIntent: String
timestamp: LocalDateTime
```

---

### `PlayerIntent.scala`

An enum that represents what the user wants to do.

Examples:

```scala
Greeting
StartGame
ChooseA
ChooseB
SetCategory
SetDifficulty
Recommend
Summary
History
Mood
Help
Unknown
```

---

### `UserChoice.scala`

Stores one answer selected by the user.

Fields:

```scala
questionId: Int
selectedOption: String
category: String
tags: List[String]
```

---

### `WouldYouRatherQuestion.scala`

Represents one Would You Rather question.

Fields:

```scala
id: Int
category: String
difficulty: String
optionA: String
optionB: String
tags: List[String]
```

---

## Functional Programming Concepts Used

### Immutability

The project avoids mutable global state.

Instead of changing the current state directly, it creates a new state:

```scala
state.copy(preferences = state.preferences + ("category" -> "funny"))
```

---

### Case Classes

Case classes are used for structured data:

* `WouldYouRatherQuestion`
* `UserChoice`
* `InteractionEntry`
* `ConversationState`

---

### Enum / ADT

`PlayerIntent` is used to represent the meaning of user input in a safe and readable way.

---

### Pattern Matching

Pattern matching is used to handle different user intents.

Example:

```scala
intent match
  case PlayerIntent.Greeting =>
  case PlayerIntent.StartGame =>
  case PlayerIntent.Recommend =>
```

---

### Option

`Option` is used to safely handle values that may not exist.

Examples:

```scala
currentQuestion: Option[WouldYouRatherQuestion]
extractCategory(input): Option[String]
extractDifficulty(input): Option[String]
```

This avoids using `null`.

---

### Higher-Order Functions

The project uses higher-order functions such as:

```scala
map
filter
flatMap
exists
count
groupBy
sortBy
getOrElse
```

---

### Currying

Some helper functions are curried.

Example:

```scala
updatePreference("category")("funny")(state)
```

---

### Lazy Evaluation

The question bank uses:

```scala
lazy val questions
```

This means the questions are created only when first accessed.

---

## Testing

Suggested test sequence:

```text
help
funny
hard
start
A
recommend
summary
history
bad
mood
bye
```

Expected results:

| Input       | Expected Result                       |
| ----------- | ------------------------------------- |
| `help`      | Shows available commands              |
| `funny`     | Saves category preference             |
| `hard`      | Saves difficulty preference           |
| `start`     | Shows a funny hard question           |
| `A`         | Stores the answer                     |
| `recommend` | Recommends another suitable question  |
| `summary`   | Shows turns, choices, and preferences |
| `history`   | Shows logged interactions             |
| `bad`       | Adds a negative word to history       |
| `mood`      | Detects negative mood                 |
| `bye`       | Exits the chatbot                     |

---

## Known Limitations

* The chatbot is rule-based.
* It only understands predefined keywords.
* Mood detection is keyword-based and simple.
* Questions are stored directly in the code.
* Recommendations depend on available questions in the question bank.
* If no exact question match exists, the recommender falls back to another suitable question.

---

## Future Improvements

* Add a graphical user interface
* Add multiplayer mode
* Add score tracking
* Store questions in JSON or a database
* Add more questions
* Improve mood detection
* Improve repeated query detection
* Save user profiles between sessions

---

## Team Roles

### Member A: Core Chatbot and Integration

Worked on:

* `Main.scala`
* `ChatbotCore.scala`

Responsibilities:

* main chatbot flow
* input parsing
* intent detection
* integration between modules

---

### Member B: Recommendation Engine

Worked on:

* `QuestionRecommender.scala`

Responsibilities:

* filtering questions
* selecting recommended questions
* explaining recommendations
* avoiding answered questions

---

### Member C: Conversation Memory

Worked on:

* `ConversationTracker.scala`
* `InteractionEntry.scala`
* `ConversationState.scala`

Responsibilities:

* logging interactions
* history tracking
* summary generation
* mood detection

---

### Member D: Question Bank and Documentation

Worked on:

* `QuestionBank.scala`
* documentation
* testing

Responsibilities:

* creating questions
* organizing categories and difficulties
* testing commands
* preparing documentation

---

## Conclusion

This project demonstrates a complete Scala 3 rule-based chatbot using functional and declarative programming principles.

The chatbot can play a Would You Rather game, remember user choices, recommend questions, track conversation history, summarize the chat, and detect mood using simple keyword analysis.

```
```
