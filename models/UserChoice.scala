package models

// ─────────────────────────────────────────────
//  Domain Models for the Would-You-Rather Chatbot
//  Member 2 — Recommendation Engine
// ─────────────────────────────────────────────

/** A single Would-You-Rather question with metadata used for filtering & scoring. */
case class WYRQuestion(
  id         : Int,
  optionA    : String,
  optionB    : String,
  category   : String,   // "fun" | "lifestyle" | "hypothetical" | "moral" | "deep" | "social"
  difficulty : String,   // "easy" | "medium" | "hard"
  tags       : List[String]
)

/** One recorded answer by the user. */
case class UserChoice(
  questionId    : Int,
  selectedOption : String,   // "A" or "B"
  category      : String,
  tags          : List[String]
)

/**
 * Immutable snapshot of everything the engine knows about the user.
 * Never mutated — each update produces a fresh copy via .copy().
 *
 * @param preferredCategories  category → interaction count
 * @param preferredTags        tag      → interaction count
 * @param difficultyLevel      current recommended difficulty tier
 * @param answeredQuestionIds  set of question IDs already seen
 * @param totalAnswered        running total, drives difficulty progression
 */
case class UserProfile(
  preferredCategories  : Map[String, Int],
  preferredTags        : Map[String, Int],
  difficultyLevel      : String,
  answeredQuestionIds  : Set[Int],
  totalAnswered        : Int
)

object UserProfile {
  /** A brand-new profile for a user who has answered nothing yet. */
  val empty: UserProfile = UserProfile(
    preferredCategories = Map.empty,
    preferredTags       = Map.empty,
    difficultyLevel     = "easy",
    answeredQuestionIds = Set.empty,
    totalAnswered       = 0
  )
}