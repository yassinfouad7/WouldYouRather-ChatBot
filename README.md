# Would You Rather Chatbot

A Scala 3 terminal-based chatbot that plays a **Would You Rather** game with the user.  
The chatbot can ask questions, store user choices, remember preferences, recommend future questions, summarize the conversation, show history, and detect the user's mood using simple keyword analysis.

---

## Project Overview

This project was developed for the **Advanced Programming** course using **Scala 3**, with a focus on declarative and functional programming concepts.

The chatbot works as a rule-based game assistant. The user can choose a question category and difficulty, answer questions using `A` or `B`, and ask the bot for recommendations, summaries, history, and mood detection.

The project demonstrates:

- Case classes
- Enums / ADTs
- Pattern matching
- Immutable collections
- Higher-order functions
- Option handling
- Currying
- Lazy evaluation
- Recursive program flow
- Functional state updates using `copy()`

---

## Features

### Core Chatbot

- Greets the user
- Reads terminal input
- Detects user intent
- Handles unknown inputs
- Provides a help menu
- Exits politely when the user types `bye`, `exit`, or `quit`

### Would You Rather Game

- Asks Would You Rather questions
- Each question has:
  - ID
  - Category
  - Difficulty
  - Option A
  - Option B
  - Tags

### Categories

The chatbot supports the following categories:

- funny
- scary
- deep
- adventure
- school
- money
- superpower
- social
- random

### Difficulty Levels

The chatbot supports:

- easy
- medium
- hard

### Recommendation System

The recommendation engine chooses questions based on:

- saved category preference
- saved difficulty preference
- previous answered questions
- available unanswered questions

The recommender tries to find the best match in this order:

1. Same category and same difficulty
2. Same difficulty only
3. Same category only
4. Any unanswered question
5. First question as a fallback

### Conversation Memory

The chatbot logs each interaction with:

- sequence number
- user input
- bot response
- detected intent
- timestamp

The user can type:

```text
history
