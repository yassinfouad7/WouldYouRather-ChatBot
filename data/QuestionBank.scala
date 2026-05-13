package data

import models.WouldYouRatherQuestion

object QuestionBank { // stores all the questions

  lazy val questions: List[WouldYouRatherQuestion] = List( // list containing questions

    WouldYouRatherQuestion(
      1,
      "funny",
      "easy",
      "have pizza instead of hair",
      "smell like garlic forever",
      List("funny", "food", "weird")
    ),

    WouldYouRatherQuestion(
      2,
      "funny",
      "easy",
      "laugh every time you sit down",
      "dance whenever music plays",
      List("awkward", "funny", "people")
    ),

    WouldYouRatherQuestion(
      3,
      "scary",
      "medium",
      "sleep in a haunted house",
      "walk in a dark forest alone",
      List("fear", "dark", "night")
    ),

    WouldYouRatherQuestion(
      4,
      "scary",
      "hard",
      "hear whispers every night",
      "see shadows moving in your room",
      List("ghost", "creepy", "horror")
    ),

    WouldYouRatherQuestion(
      5,
      "deep",
      "hard",
      "know your future",
      "change your past",
      List("life", "future", "thoughts")
    ),

    WouldYouRatherQuestion(
      6,
      "deep",
      "medium",
      "lose your favorite memory",
      "forget your best friend",
      List("memory", "friends", "life")
    ),

    WouldYouRatherQuestion(
      7,
      "adventure",
      "easy",
      "travel to space",
      "travel under the ocean",
      List("travel", "explore", "adventure")
    ),

    WouldYouRatherQuestion(
      8,
      "adventure",
      "medium",
      "climb a huge mountain",
      "live on an island for a year",
      List("nature", "challenge", "survival")
    ),

    WouldYouRatherQuestion(
      9,
      "school",
      "easy",
      "never have homework again",
      "never take exams again",
      List("school", "study", "student")
    ),

    WouldYouRatherQuestion(
      10,
      "school",
      "medium",
      "study 12 hours daily",
      "repeat one subject every year",
      List("college", "stress", "study")
    ),

    WouldYouRatherQuestion(
      11,
      "money",
      "easy",
      "get money every day",
      "get millions after 20 years",
      List("money", "future", "cash")
    ),

    WouldYouRatherQuestion(
      12,
      "money",
      "hard",
      "be rich without friends",
      "be poor with loyal friends",
      List("money", "friends", "life")
    ),

    WouldYouRatherQuestion(
      13,
      "superpower",
      "easy",
      "fly everywhere",
      "be invisible",
      List("power", "hero", "cool")
    ),

    WouldYouRatherQuestion(
      14,
      "superpower",
      "medium",
      "read minds",
      "stop time for 10 seconds",
      List("mind", "time", "power")
    ),

    WouldYouRatherQuestion(
      15,
      "social",
      "medium",
      "say everything on your mind",
      "never express your feelings",
      List("social", "emotion", "people")
    ),

    WouldYouRatherQuestion(
      16,
      "social",
      "easy",
      "meet your favorite football player",
      "be famous on instagram",
      List("social", "famous", "people")
    ),

    WouldYouRatherQuestion(
      17,
      "random",
      "easy",
      "never eat burgers again",
      "never drink coffee again",
      List("food", "daily", "random")
    ),

    WouldYouRatherQuestion(
      18,
      "random",
      "medium",
      "lose your phone for one year",
      "lose internet for one year",
      List("technology", "internet", "daily")
    ),

    WouldYouRatherQuestion(
      19,
      "deep",
      "hard",
      "save your best friend",
      "save five strangers",
      List("choice", "morals", "life")
    ),

    WouldYouRatherQuestion(
      20,
      "funny",
      "medium",
      "sing every sentence",
      "whisper every sentence",
      List("voice", "awkward", "funny")
    )

  )

  def easyQuestions() = { // gets easy questions only

    questions.filter(question => question.difficulty == "easy")

  }

  def funnyQuestions() = { // gets funny questions

    questions.filter(question => question.category == "funny")

  }

  def countFunnyQuestions() = { // counts funny questions

    questions.count(question => question.category == "funny")

  }

  def difficultyMessage(question: WouldYouRatherQuestion) = {

    // checks difficulty type
    question.difficulty match {

      case "easy" =>
        "Easy Question"

      case "medium" =>
        "Medium Question"

      case "hard" =>
        "Hard Question"

      case _ =>
        "Unknown Difficulty"

    }

  }

}
