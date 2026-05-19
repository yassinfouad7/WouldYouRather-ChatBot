package models

case class ConversationState(
  history: List[InteractionEntry] = List.empty,
  preferences: Map[String, String] = Map.empty,
  choices: List[UserChoice] = List.empty,
  currentQuestion: Option[WouldYouRatherQuestion] = None
)