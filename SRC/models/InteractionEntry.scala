package models

import java.time.LocalDateTime

case class InteractionEntry(
  sequenceNumber: Int,
  userInput: String,
  botResponse: String,
  detectedIntent: String,
  timestamp: LocalDateTime
)