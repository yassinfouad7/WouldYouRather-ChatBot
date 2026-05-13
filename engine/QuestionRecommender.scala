package engine

import models._

/**
 * Personalized Recommendation Engine — Would-You-Rather Chatbot
 * Member 2 — Recommendation Engine
 *
 * Design principles
 * ─────────────────
 *  • Every public function is PURE (no side effects, same input → same output).
 *  • State is IMMUTABLE: updates return new values, never mutate in place.
 *  • CURRYING turns multi-parameter logic into reusable, composable pieces.
 *  • HIGHER-ORDER FUNCTIONS (filter, map, sortBy, foldLeft, groupBy) drive all
 *    data transformations declaratively.
 *  • LAZY EVALUATION defers the knowledge base and its derived views until first use.
 *  • Option types handle missing data without throwing exceptions.
 */
object QuestionRecommender {

  // ═══════════════════════════════════════════════════════════════════════════
  //  KNOWLEDGE BASE
  //  lazy val → computed only once, on first access (lazy evaluation)
  // ═══════════════════════════════════════════════════════════════════════════

  lazy val knowledgeBase: List[WYRQuestion] = List(
    // ── easy ────────────────────────────────────────────────────────────────
    WYRQuestion(1,  "be able to fly",
                    "be invisible",
                    "fun", "easy", List("superpowers", "fantasy")),

    WYRQuestion(2,  "always feel cold",
                    "always feel hot",
                    "lifestyle", "easy", List("comfort", "habits")),

    WYRQuestion(3,  "give up social media forever",
                    "give up watching TV / streaming forever",
                    "lifestyle", "easy", List("media", "habits")),

    WYRQuestion(4,  "be the funniest person in every room",
                    "be the smartest person in every room",
                    "social", "easy", List("personality", "social")),

    WYRQuestion(5,  "eat the same meal every single day",
                    "never eat your favourite food again",
                    "fun", "easy", List("food", "habits")),

    WYRQuestion(6,  "have a pet dragon",
                    "have a pet unicorn",
                    "fun", "easy", List("fantasy", "animals")),

    WYRQuestion(7,  "never have to sleep",
                    "never have to eat",
                    "hypothetical", "easy", List("lifestyle", "fantasy")),

    // ── medium ──────────────────────────────────────────────────────────────
    WYRQuestion(8,  "have unlimited money",
                    "have unlimited free time",
                    "hypothetical", "medium", List("money", "time")),

    WYRQuestion(9,  "be able to talk to animals",
                    "speak every human language fluently",
                    "hypothetical", "medium", List("communication", "fantasy")),

    WYRQuestion(10, "be able to teleport anywhere instantly",
                    "be able to read minds",
                    "fun", "medium", List("superpowers", "fantasy")),

    WYRQuestion(11, "live permanently in the past (any era you choose)",
                    "live permanently in the future (100 years from now)",
                    "hypothetical", "medium", List("time", "history")),

    WYRQuestion(12, "have a rewind button for your life",
                    "have a pause button for your life",
                    "deep", "medium", List("time", "control")),

    WYRQuestion(13, "have perfect memory of everything",
                    "be able to deliberately forget anything you want",
                    "deep", "medium", List("mind", "memory")),

    WYRQuestion(14, "live without music",
                    "live without movies",
                    "lifestyle", "medium", List("media", "art")),

    WYRQuestion(15, "always win every argument",
                    "always lose arguments but do so with grace",
                    "social", "medium", List("ego", "social")),

    // ── hard ────────────────────────────────────────────────────────────────
    WYRQuestion(16, "know exactly how you will die",
                    "know exactly when you will die",
                    "deep", "hard", List("mortality", "knowledge")),

    WYRQuestion(17, "lose every memory you currently have",
                    "be unable to form any new memories from today",
                    "deep", "hard", List("memory", "identity")),

    WYRQuestion(18, "be famous and universally disliked",
                    "be completely unknown but deeply loved by those close to you",
                    "social", "hard", List("fame", "relationships")),

    WYRQuestion(19, "be forced to always tell the truth",
                    "be forced to always lie",
                    "moral", "hard", List("honesty", "ethics")),

    WYRQuestion(20, "have no internet access for an entire year",
                    "have no friends for an entire year",
                    "lifestyle", "hard", List("technology", "relationships")),

    WYRQuestion(21, "be the richest person alive but have no friends",
                    "be the poorest person alive but surrounded by loyal friends",
                    "moral", "hard", List("money", "relationships")),

    WYRQuestion(22, "end world hunger permanently",
                    "achieve permanent world peace",
                    "moral", "hard", List("ethics", "society")),

    WYRQuestion(23, "never feel physical pain again",
                    "never feel emotional pain again",
                    "deep", "hard", List("emotions", "wellbeing"))
  )

  // Lazy derived indexes — built once from knowledgeBase on first access
  lazy val byCategory  : Map[String, List[WYRQuestion]] = knowledgeBase.groupBy(_.category)
  lazy val byDifficulty: Map[String, List[WYRQuestion]] = knowledgeBase.groupBy(_.difficulty)

  // ═══════════════════════════════════════════════════════════════════════════
  //  CURRIED FILTER FUNCTIONS
  //  Each returns List[WYRQuestion] => List[WYRQuestion] — a composable stage.
  //  Partial application lets callers bake in one argument and reuse the rest.
  // ═══════════════════════════════════════════════════════════════════════════

  /** Keep only questions in the given category. */
  val filterByCategory: String => List[WYRQuestion] => List[WYRQuestion] =
    category => _.filter(_.category == category)

  /** Keep only questions at the given difficulty level. */
  val filterByDifficulty: String => List[WYRQuestion] => List[WYRQuestion] =
    difficulty => _.filter(_.difficulty == difficulty)

  /** Remove questions the user has already answered. */
  val excludeAnswered: Set[Int] => List[WYRQuestion] => List[WYRQuestion] =
    answeredIds => _.filterNot(q => answeredIds.contains(q.id))

  /** Keep only questions that carry the given tag. */
  val filterByTag: String => List[WYRQuestion] => List[WYRQuestion] =
    tag => _.filter(_.tags.contains(tag))

  // ═══════════════════════════════════════════════════════════════════════════
  //  PROFILE BUILDING
  //  Pure function: fold over a list of past choices to derive a UserProfile.
  //  foldLeft accumulates state immutably — no vars, no mutation.
  // ═══════════════════════════════════════════════════════════════════════════

  def buildProfile(choices: List[UserChoice]): UserProfile =
    choices.foldLeft(UserProfile.empty) { (profile, choice) =>

      // Immutably increment the category counter
      val updatedCategories = profile.preferredCategories.updatedWith(choice.category) {
        case Some(n) => Some(n + 1)
        case None    => Some(1)
      }

      // Immutably increment every tag counter for this choice
      val updatedTags = choice.tags.foldLeft(profile.preferredTags) { (tagMap, tag) =>
        tagMap.updatedWith(tag) {
          case Some(n) => Some(n + 1)
          case None    => Some(1)
        }
      }

      val newTotal = profile.totalAnswered + 1

      profile.copy(
        preferredCategories = updatedCategories,
        preferredTags       = updatedTags,
        difficultyLevel     = progressDifficulty(newTotal),
        answeredQuestionIds = profile.answeredQuestionIds + choice.questionId,
        totalAnswered       = newTotal
      )
    }

  // ── Difficulty progression — pure function, pattern match on total answered
  private val progressDifficulty: Int => String = {
    case n if n < 5  => "easy"
    case n if n < 12 => "medium"
    case _           => "hard"
  }

  // ═══════════════════════════════════════════════════════════════════════════
  //  PREFERENCE MANAGEMENT
  //  getUserPreferences   — read-only view of profile as a plain Map
  //  updatePreferences    — curried, returns a NEW profile (immutable update)
  // ═══════════════════════════════════════════════════════════════════════════

  /** Extract a human-readable preference summary. */
  def getUserPreferences(profile: UserProfile): Map[String, String] = Map(
    "topCategory"   -> profile.preferredCategories.maxByOption(_._2).map(_._1).getOrElse("none"),
    "topTag"        -> profile.preferredTags.maxByOption(_._2).map(_._1).getOrElse("none"),
    "difficulty"    -> profile.difficultyLevel,
    "totalAnswered" -> profile.totalAnswered.toString
  )

  /**
   * Curried immutable preference update.
   * Usage:  val newProfile = updatePreferences("category")("moral")(profile)
   *
   * Explicit preferences receive a larger boost (+3) than organic ones (+1)
   * so the user's stated wishes outweigh passive interaction counts.
   */
  val updatePreferences: String => String => UserProfile => UserProfile =
    key => value => profile => key match {

      case "category" => profile.copy(
        preferredCategories = profile.preferredCategories.updatedWith(value) {
          case Some(n) => Some(n + 3)
          case None    => Some(3)
        }
      )

      case "difficulty" => profile.copy(difficultyLevel = value)

      case "tag" => profile.copy(
        preferredTags = profile.preferredTags.updatedWith(value) {
          case Some(n) => Some(n + 2)
          case None    => Some(2)
        }
      )

      case _ => profile   // unknown key → no change, return same profile
    }

  // ═══════════════════════════════════════════════════════════════════════════
  //  SCORING — curried pure function
  //  Returns a relevance score for one question given the user's profile.
  //  Higher-order: accepts and applies functions stored as values (foldLeft).
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Score = (category affinity × 2) + (sum of tag affinities).
   * Category is weighted double because it's a stronger preference signal.
   */
  val scoreQuestion: UserProfile => WYRQuestion => Double =
    profile => question => {
      val categoryScore =
        profile.preferredCategories.getOrElse(question.category, 0).toDouble * 2.0

      val tagScore = question.tags.foldLeft(0.0) { (acc, tag) =>
        acc + profile.preferredTags.getOrElse(tag, 0).toDouble
      }

      categoryScore + tagScore
    }

  // ═══════════════════════════════════════════════════════════════════════════
  //  RECOMMENDATION PIPELINE
  //  Composes curried filters with `andThen` into a single transformation.
  //  Returns Option[WYRQuestion] — None if every question is exhausted.
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Primary recommendation.
   *  1. Build a pipeline: exclude answered → filter by current difficulty.
   *  2. Score remaining questions and return the highest-scoring one.
   *  3. Fall back to any unanswered question if the difficulty tier is empty.
   *  4. Return None only when the user has answered everything.
   */
  def recommend(profile: UserProfile)(questions: List[WYRQuestion]): Option[WYRQuestion] = {
    val pipeline: List[WYRQuestion] => List[WYRQuestion] =
      excludeAnswered(profile.answeredQuestionIds) andThen
      filterByDifficulty(profile.difficultyLevel)

    val scored: List[(WYRQuestion, Double)] =
      pipeline(questions).map(q => q -> scoreQuestion(profile)(q))

    scored.sortBy { case (_, score) => -score }
          .headOption
          .map(_._1)
          .orElse(fallbackRecommend(profile)(questions))
  }

  /** Ignore difficulty; just avoid already-answered questions. */
  private def fallbackRecommend(profile: UserProfile)(questions: List[WYRQuestion]): Option[WYRQuestion] =
    excludeAnswered(profile.answeredQuestionIds)(questions).headOption

  // ═══════════════════════════════════════════════════════════════════════════
  //  EXPLANATION — pure, human-readable string for each recommendation
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Explain why a question was chosen, citing the user's top matching signals.
   * Returns a friendly sentence suitable for display in the chatbot.
   */
  def explainRecommendation(profile: UserProfile)(question: WYRQuestion): String = {
    val categoryReason: Option[String] =
      profile.preferredCategories.get(question.category)
        .map(n => s"you've shown interest in ${question.category} questions ($n interactions)")

    val tagReasons: List[String] =
      question.tags.flatMap { tag =>
        profile.preferredTags.get(tag).map(n => s"you engage with '$tag' topics ($n times)")
      }

    val reasons = (categoryReason.toList ++ tagReasons).take(2)

    if (reasons.isEmpty)
      s"Here's a fresh ${question.difficulty} ${question.category} question for you!"
    else
      s"Picked because ${reasons.mkString(" and ")}."
  }

  // ═══════════════════════════════════════════════════════════════════════════
  //  LAZY QUESTION STREAM
  //  Uses LazyList (#::) to produce an infinite-like sequence of recommendations
  //  evaluated on demand — questions are only selected as the user asks for them.
  // ═══════════════════════════════════════════════════════════════════════════

  /**
   * Builds a LazyList of questions in personalised order.
   * Each element is computed only when `.head` is accessed — lazy evaluation.
   */
  def questionStream(profile: UserProfile, questions: List[WYRQuestion]): LazyList[WYRQuestion] = {
    def loop(remaining: List[WYRQuestion]): LazyList[WYRQuestion] =
      recommend(profile)(remaining) match {
        case None    => LazyList.empty
        case Some(q) => q #:: loop(remaining.filterNot(_.id == q.id))
      }
    loop(questions)
  }

  /** Take the next N best questions without evaluating the rest of the stream. */
  def getTopNRecommendations(n: Int)(profile: UserProfile)(questions: List[WYRQuestion]): List[WYRQuestion] =
    questionStream(profile, questions).take(n).toList

  // ═══════════════════════════════════════════════════════════════════════════
  //  ANALYTICS — HOF-driven insights over UserChoice history
  // ═══════════════════════════════════════════════════════════════════════════

  /** Category frequencies, sorted descending. Uses groupBy + map + sortBy. */
  def getCategoryStats(choices: List[UserChoice]): List[(String, Int)] =
    choices.groupBy(_.category)
           .map { case (cat, cs) => cat -> cs.length }
           .toList
           .sortBy { case (_, count) => -count }

  /** Detect whether the user has previously answered a question in the same category. */
  val hasAnsweredCategoryBefore: String => List[UserChoice] => Boolean =
    category => choices => choices.exists(_.category == category)

  /**
   * Summarise what the engine knows about the user.
   * Useful for the context tracker's summarizeConversation output.
   */
  def summarisePreferences(profile: UserProfile): String = {
    val prefs = getUserPreferences(profile)
    val topCat = prefs("topCategory")
    val topTag = prefs("topTag")
    val diff   = prefs("difficulty")
    val total  = prefs("totalAnswered")

    s"Answered $total question(s). " +
    s"Favourite category: $topCat. " +
    s"Most engaged tag: $topTag. " +
    s"Current difficulty tier: $diff."
  }

  // ═══════════════════════════════════════════════════════════════════════════
  //  CHATBOT INTEGRATION HELPERS
  //  These functions connect Member A's ChatbotCore with Member D's QuestionBank.
  //  They use the WouldYouRatherQuestion model used by the running chatbot.
  // ═══════════════════════════════════════════════════════════════════════════

  def recommend(
    preferences: Map[String, String],
    questions: List[WouldYouRatherQuestion]
  ): List[WouldYouRatherQuestion] = {
    questions
      .filter(question => preferences.get("category").forall(category => question.category == category))
      .filter(question => preferences.get("difficulty").forall(difficulty => question.difficulty == difficulty))
      .sortBy(_.id)
  }

  def pickQuestion(
    questions: List[WouldYouRatherQuestion],
    state: ConversationState
  ): WouldYouRatherQuestion = {
    val answeredIds = state.choices.map(_.questionId).toSet

    recommend(state.preferences, questions)
      .find(question => !answeredIds.contains(question.id))
      .orElse(questions.find(question => !answeredIds.contains(question.id)))
      .getOrElse(questions.head)
  }

  def explainRecommendation(question: WouldYouRatherQuestion): String = {
    s"I recommended this because it is a ${question.difficulty} ${question.category} question with these tags: ${question.tags.mkString(", ")}."
  }

}