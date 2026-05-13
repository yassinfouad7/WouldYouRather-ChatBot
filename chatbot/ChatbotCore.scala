package chatbot // This file belongs to the chatbot package

import models.* // Import all model classes
import data.QuestionBank // Import the real question list
import engine.QuestionRecommender // Import recommendation functions
import memory.ConversationTracker // Import memory/history functions

object ChatbotCore: // ChatbotCore contains the main chatbot logic

  // Valid question categories used by the chatbot and QuestionBank.
  private val categories: List[String] =
    List(
      "funny",
      "scary",
      "deep",
      "adventure",
      "school",
      "money",
      "superpower",
      "social",
      "random"
    )

  // Valid difficulty levels.
  private val difficulties: List[String] =
    List("easy", "medium", "hard")

  // Words that mean greeting, starting, or exiting.
  private val greetings: Set[String] =
    Set("hi", "hello", "hey")

  private val startWords: Set[String] =
    Set("start", "play", "begin")

  private val exitWords: Set[String] =
    Set("bye", "exit", "quit")

  // First message shown when the program starts.
  def greetUser(): String =
    "Hi! I am WYRBot. Type 'help' to see commands."

  // Cleans user input and splits it into lowercase words.
  def parseInput(input: String): List[String] =
    input
      .toLowerCase
      .trim
      .replaceAll("[^a-z0-9\\s]", "")
      .split("\\s+")
      .toList
      .filter(_.nonEmpty)

  // Checks if the user typed bye, exit, or quit.
  def isExitInput(input: String): Boolean =
    parseInput(input).exists(exitWords.contains)

  // Detects what the user wants to do.
  def detectIntent(input: String): PlayerIntent =
    val words = parseInput(input)
    val normalizedInput = words.mkString(" ")

    lazy val hasCategory: Boolean =
      words.exists(categories.contains)

    lazy val hasDifficulty: Boolean =
      words.exists(difficulties.contains)

    normalizedInput match
      case _ if greetings.exists(words.contains) =>
        PlayerIntent.Greeting

      case _ if startWords.exists(words.contains) =>
        PlayerIntent.StartGame

      case "a" | "option a" | "choose a" | "i choose a" =>
        PlayerIntent.ChooseA

      case "b" | "option b" | "choose b" | "i choose b" =>
        PlayerIntent.ChooseB

      case _ if words.exists(word => Set("recommend", "suggest").contains(word)) =>
        PlayerIntent.Recommend

      case _ if words.exists(word => Set("summary", "summarize").contains(word)) =>
        PlayerIntent.Summary

      case _ if words.contains("history") =>
        PlayerIntent.History

      case _ if words.contains("mood") =>
        PlayerIntent.Mood

      case _ if words.contains("help") =>
        PlayerIntent.Help

      case _ if hasCategory =>
        PlayerIntent.SetCategory

      case _ if hasDifficulty =>
        PlayerIntent.SetDifficulty

      case _ =>
        PlayerIntent.Unknown

  // Main chatbot handler. It returns the bot response and a new ConversationState.
  def handleUserInput(
    input: String,
    state: ConversationState
  ): (String, ConversationState) =

    val intent = detectIntent(input)

    val (response, updatedState) =
      intent match
        case PlayerIntent.Greeting =>
          respond("Hello! Ready for a Would You Rather question?")(state)

        case PlayerIntent.StartGame =>
          val question = QuestionRecommender.pickQuestion(QuestionBank.questions, state)
          respond(formatQuestion(question))(state.copy(currentQuestion = Some(question)))

        case PlayerIntent.SetCategory =>
          extractCategory(input)
            .map(category =>
              val newState = updatePreference("category")(category)(state)
              respond(s"Got it. I saved your preferred category as: $category")(newState)
            )
            .getOrElse(
              respond("I could not detect the category. Try funny, scary, deep, or adventure.")(state)
            )

        case PlayerIntent.SetDifficulty =>
          extractDifficulty(input)
            .map(difficulty =>
              val newState = updatePreference("difficulty")(difficulty)(state)
              respond(s"Got it. I saved your preferred difficulty as: $difficulty")(newState)
            )
            .getOrElse(
              respond("I could not detect the difficulty. Try easy, medium, or hard.")(state)
            )

        case PlayerIntent.ChooseA =>
          handleChoice("A")(state)

        case PlayerIntent.ChooseB =>
          handleChoice("B")(state)

        case PlayerIntent.Recommend =>
          val question = QuestionRecommender.pickQuestion(QuestionBank.questions, state)
          val message = formatQuestion(question) + "\n" + QuestionRecommender.explainRecommendation(question)
          respond(message)(state.copy(currentQuestion = Some(question)))

        case PlayerIntent.Summary =>
          respond(ConversationTracker.summarizeConversation(state))(state)

        case PlayerIntent.History =>
          respond(ConversationTracker.formatHistory(state.history))(state)

        case PlayerIntent.Mood =>
          respond(s"Your current mood seems ${ConversationTracker.getMood(state)}.")(state)

        case PlayerIntent.Help =>
          respond(helpMessage())(state)

        case PlayerIntent.Exit =>
          respond("Goodbye!")(state)

        case PlayerIntent.Unknown =>
          respond("I did not understand. Try: start, funny, scary, easy, hard, recommend, summary, A, B, or bye.")(state)

    val finalState =
      ConversationTracker.logInteraction(
        userInput = input,
        botResponse = response,
        intent = intent.toString,
        state = updatedState
      )

    (response, finalState)

  // Creates a response without changing the state.
  private def respond(message: String)(state: ConversationState): (String, ConversationState) =
    (message, state)

  // Curried helper that updates one preference immutably.
  private def updatePreference(
    key: String
  )(
    value: String
  )(
    state: ConversationState
  ): ConversationState =
    state.copy(preferences = state.preferences + (key -> value))

  // Finds a valid category in user input.
  def extractCategory(input: String): Option[String] =
    val words = parseInput(input)
    categories.find(words.contains)

  // Finds a valid difficulty in user input.
  def extractDifficulty(input: String): Option[String] =
    val words = parseInput(input)
    difficulties.find(words.contains)

  // Handles the user choosing A or B.
  def handleChoice(
    selectedOption: String
  )(
    state: ConversationState
  ): (String, ConversationState) =
    state.currentQuestion match
      case Some(question) =>
        val choice =
          UserChoice(
            questionId = question.id,
            selectedOption = selectedOption,
            category = question.category,
            tags = question.tags
          )

        val updatedState =
          state.copy(
            choices = state.choices :+ choice,
            currentQuestion = None,
            preferences = state.preferences + ("lastCategory" -> question.category)
          )

        respond(s"You chose option $selectedOption. Nice choice!")(updatedState)

      case None =>
        respond("There is no active question right now. Type 'start' first.")(state)

  // Formats a question into readable text.
  def formatQuestion(question: WouldYouRatherQuestion): String =
    s"""Would you rather:
       |A) ${question.optionA}
       |B) ${question.optionB}
       |Category: ${question.category}, Difficulty: ${question.difficulty}
       |""".stripMargin

  // Kept as a fallback helper, but Summary now uses ConversationTracker.
  def temporarySummary(state: ConversationState): String =
    ConversationTracker.summarizeConversation(state)

  // Help menu.
  def helpMessage(): String =
    """Here are the commands you can use:
      |
      |1. Start the game:
      |   - start
      |   - play
      |
      |2. Choose a question category:
      |   - funny
      |   - scary
      |   - deep
      |   - adventure
      |   - school
      |   - money
      |   - superpower
      |   - social
      |   - random
      |
      |3. Choose a difficulty level:
      |   - easy
      |   - medium
      |   - hard
      |
      |4. Answer a question:
      |   - A
      |   - B
      |
      |5. Other commands:
      |   - recommend  -> get a suggested question
      |   - summary    -> see a short summary of the chat
      |   - history    -> see previous messages
      |   - mood       -> check detected mood
      |   - help       -> show this message again
      |   - bye        -> exit the chatbot
      |
      |Example:
      |You: start
      |Bot: Would you rather...
      |You: A
      |Bot: You chose option A.
      |""".stripMargin
