package data

import models.WouldYouRatherQuestion

object QuestionBank { // stores all the questions

  lazy val questions: List[WouldYouRatherQuestion] = List( // list containing questions

    // funny
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
      20,
      "funny",
      "medium",
      "sing every sentence",
      "whisper every sentence",
      List("voice", "awkward", "funny")
    ),

    WouldYouRatherQuestion(
      21,
      "funny",
      "hard",
      "accidentally laugh during every serious conversation",
      "speak only in dramatic movie quotes forever",
      List("funny", "awkward", "social")
    ),

    // scary
    WouldYouRatherQuestion(
      22,
      "scary",
      "easy",
      "hear a strange noise outside your window",
      "see your door slowly open by itself",
      List("scary", "fear", "night")
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

    // deep
    WouldYouRatherQuestion(
      23,
      "deep",
      "easy",
      "know what everyone thinks about you",
      "never know what anyone thinks about you",
      List("deep", "thoughts", "people")
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
      5,
      "deep",
      "hard",
      "know your future",
      "change your past",
      List("life", "future", "thoughts")
    ),

    WouldYouRatherQuestion(
      19,
      "deep",
      "hard",
      "save your best friend",
      "save five strangers",
      List("choice", "morals", "life")
    ),

    // adventure
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
      24,
      "adventure",
      "hard",
      "cross a desert with limited water",
      "sail through a storm with no map",
      List("adventure", "danger", "survival")
    ),

    // school
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
      25,
      "school",
      "hard",
      "get full marks but never understand the subject",
      "understand everything but always get average grades",
      List("school", "grades", "learning")
    ),

    // money
    WouldYouRatherQuestion(
      11,
      "money",
      "easy",
      "get money every day",
      "get millions after 20 years",
      List("money", "future", "cash")
    ),

    WouldYouRatherQuestion(
      26,
      "money",
      "medium",
      "have free food forever",
      "have free travel forever",
      List("money", "travel", "life")
    ),

    WouldYouRatherQuestion(
      12,
      "money",
      "hard",
      "be rich without friends",
      "be poor with loyal friends",
      List("money", "friends", "life")
    ),

    // superpower
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
      27,
      "superpower",
      "hard",
      "control time but lose one memory each time",
      "read minds but never turn the power off",
      List("superpower", "mind", "cost")
    ),

    // social
    WouldYouRatherQuestion(
      16,
      "social",
      "easy",
      "meet your favorite football player",
      "be famous on instagram",
      List("social", "famous", "people")
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
      28,
      "social",
      "hard",
      "lose your closest friend",
      "never make a new close friend again",
      List("social", "friends", "life")
    ),

    // random
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
      29,
      "random",
      "hard",
      "wake up in a new country every week",
      "forget one random thing every morning",
      List("random", "travel", "memory")
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