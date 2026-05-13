package models

// this class stores one would you rather question
case class WouldYouRatherQuestion(

  id: Int, // unique id for each question

  category: String, // category like funny, sports, food, etc

  difficulty: String, // difficulty level easy medium hard

  optionA: String, // first option in the question

  optionB: String, // second option in the question

  tags: List[String] // list of tags related to the question

)
