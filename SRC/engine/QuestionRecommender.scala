package engine

import models.*

// This object handles only the recommendation logic.
// The actual questions are stored in QuestionBank, not here.
object QuestionRecommender {

  // Returns the saved user preferences from the current conversation state.
  // Example preferences: category -> funny, difficulty -> hard
  def getUserPreferences(state: ConversationState): Map[String, String] = {
    state.preferences
  }

  // Updates one preference in the state without changing the old state directly.
  // This is written as a curried function so it can be called like:
  // updatePreference("category")("funny")(state)
  def updatePreference(key: String)(value: String)(state: ConversationState): ConversationState = {
    state.copy(
      preferences = state.preferences + (key -> value)
    )
  }

  // Filters the list of questions based on the user's saved preferences.
  // If the user has no category preference, all categories are allowed.
  // If the user has no difficulty preference, all difficulties are allowed.
  def recommend(
    preferences: Map[String, String],
    questions: List[WouldYouRatherQuestion]
  ): List[WouldYouRatherQuestion] = {
    questions
      .filter(question =>
        preferences.get("category").forall(category => question.category == category)
      )
      .filter(question =>
        preferences.get("difficulty").forall(difficulty => question.difficulty == difficulty)
      )
      .sortBy(_.id) // Keeps the result ordered by question id
  }

  // Picks the best question to show next.
  // It avoids questions the user has already answered when possible.
  def pickQuestion(
    questions: List[WouldYouRatherQuestion],
    state: ConversationState
  ): WouldYouRatherQuestion = {

    // Get the ids of questions already answered by the user.
    val answeredIds =
      state.choices.map(_.questionId).toSet

    // Keep only questions that have not been answered yet.
    val unansweredQuestions =
      questions.filter(question => !answeredIds.contains(question.id))

    // Read the user's preferred category, if it exists.
    val preferredCategory =
      state.preferences.get("category")

    // Read the user's preferred difficulty, if it exists.
    val preferredDifficulty =
      state.preferences.get("difficulty")

    // Best case: question matches both the preferred category and difficulty.
    val exactMatches =
      unansweredQuestions.filter(question =>
        preferredCategory.forall(category => question.category == category) &&
        preferredDifficulty.forall(difficulty => question.difficulty == difficulty)
      )

    // Second best case: question matches the preferred difficulty.
    // This is useful if the exact category + difficulty combination is not available.
    val difficultyMatches =
      unansweredQuestions.filter(question =>
        preferredDifficulty.forall(difficulty => question.difficulty == difficulty)
      )

    // Third best case: question matches the preferred category.
    val categoryMatches =
      unansweredQuestions.filter(question =>
        preferredCategory.forall(category => question.category == category)
      )

    // Choose the best available option using fallback order:
    // 1. exact match
    // 2. difficulty match
    // 3. category match
    // 4. any unanswered question
    // 5. first question in the list as a final fallback
    exactMatches.headOption
      .orElse(difficultyMatches.headOption)
      .orElse(categoryMatches.headOption)
      .orElse(unansweredQuestions.headOption)
      .getOrElse(questions.head)
  }

  // Gives a simple explanation for why the question was recommended.
  def explainRecommendation(question: WouldYouRatherQuestion): String = {
    s"I recommended this because it is a ${question.difficulty} ${question.category} question with these tags: ${question.tags.mkString(", ")}."
  }

  // Looks at the user's previous choices and finds the category they answered most often.
  // It returns Option[String] because the user may not have answered any questions yet.
  def favoriteCategoryFromChoices(choices: List[UserChoice]): Option[String] = {
    choices
      .groupBy(_.category) // Group choices by category
      .map { case (category, categoryChoices) =>
        category -> categoryChoices.length // Count how many choices are in each category
      }
      .toList
      .sortBy { case (_, count) => -count } // Put the most common category first
      .headOption // Safely get the first item, if it exists
      .map { case (category, _) => category } // Keep only the category name
  }

  // Updates the category preference based on the user's previous choices.
  // If the user has not answered anything yet, the state stays the same.
  def updateCategoryFromChoices(state: ConversationState): ConversationState = {
    favoriteCategoryFromChoices(state.choices) match {
      case Some(category) =>
        state.copy(
          preferences = state.preferences + ("category" -> category)
        )

      case None =>
        state
    }
  }
}