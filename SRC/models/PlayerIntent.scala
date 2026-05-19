package models

enum PlayerIntent:
  case Greeting
  case StartGame
  case ChooseA
  case ChooseB
  case SetCategory
  case SetDifficulty
  case Recommend
  case Summary
  case History
  case Mood
  case Help
  case Exit
  case Unknown