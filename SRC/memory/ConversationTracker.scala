package memory //different package

import models.{ConversationState, InteractionEntry}
import java.time.LocalDateTime

object ConversationTracker { // singleton since only 1 object of this instance exists

  // These are the topics/categories used in the Would You Rather chatbot.
  private val wouldYouRatherTopics = List(
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

  def logInteraction(userInput: String, botResponse: String, intent: String, state: ConversationState): ConversationState = {
    val newEntry = InteractionEntry(
      sequenceNumber = state.history.size + 1, // counts how many entries already exist and adds 1 for the current entry
      userInput = userInput,
      botResponse = botResponse,
      detectedIntent = intent,
      timestamp = LocalDateTime.now()
    )

    state.copy(history = state.history :+ newEntry) // creates a copy of ConversationState with the new interaction added to history
  }

  def getConversationHistory(state: ConversationState): List[InteractionEntry] = {
    state.history
  }

  def getLastNInteractions(n: Int, state: ConversationState): List[InteractionEntry] = {
    state.history.takeRight(n)
  }

  private def cleanText(text: String): String = {
    text.toLowerCase.trim.replaceAll("[^a-z0-9\\s]", "")
  }

  def detectRepeatedQuery(input: String, history: List[InteractionEntry]): Boolean = {
    val cleanedInput = cleanText(input)

    history.exists(entry => cleanText(entry.userInput) == cleanedInput)
  }

  def isRepeating(input: String, state: ConversationState): Boolean = {
    detectRepeatedQuery(input, state.history)
  }

  def extractTopics(history: List[InteractionEntry]): List[String] = {
    history
      .flatMap { entry =>
        val text = entry.userInput.toLowerCase

        wouldYouRatherTopics.filter(word => text.contains(word))
      }
      .distinct
  }

  def findTopics(state: ConversationState): List[String] = {
    extractTopics(state.history)
  }

  def getMostDiscussedTopics(history: List[InteractionEntry]): List[(String, Int)] = {
    history
      .flatMap { entry =>
        val text = entry.userInput.toLowerCase

        wouldYouRatherTopics.filter(word => text.contains(word))
      }
      .groupBy(topic => topic)
      .map { case (topic, occurrences) => topic -> occurrences.size }
      .toList
      .sortBy { case (_, count) => -count }
  }

  def getSummary(state: ConversationState): String = {
    summarizeConversation(state)
  }

  def summarizeConversation(state: ConversationState): String = {
    val preferenceText =
      if (state.preferences.isEmpty) "No preferences saved yet."
      else state.preferences.map { case (key, value) => s"$key = $value" }.mkString(", ")

    s"We have talked for ${state.history.size} turn(s). You answered ${state.choices.size} question(s). Preferences: $preferenceText"
  }

  def getMood(state: ConversationState): String = {
    getUserMood(state.history)
  }

  def getUserMood(history: List[InteractionEntry]): String = {
    val positiveWords = List("good", "great", "happy", "awesome", "fun", "nice", "cool", "love")
    val negativeWords = List("bad", "sad", "angry", "boring", "hate", "annoying", "tired")

    val allUserText = history.map(_.userInput.toLowerCase).mkString(" ")

    val positiveScore = positiveWords.count(word => allUserText.contains(word))
    val negativeScore = negativeWords.count(word => allUserText.contains(word))

    if (positiveScore > negativeScore) "Happy"
    else if (negativeScore > positiveScore) "Negative"
    else "Neutral"
  }

  def formatHistory(history: List[InteractionEntry]): String = {
    if (history.isEmpty) "No conversation history yet."
    else {
      history
        .map(entry =>
          s"${entry.sequenceNumber}. You said: '${entry.userInput}' | Bot replied: '${entry.botResponse}' | Intent: ${entry.detectedIntent}"
        )
        .mkString("\n")
    }
  }
}