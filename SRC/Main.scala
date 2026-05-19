import chatbot.ChatbotCore // Import the chatbot logic from ChatbotCore
import models.ConversationState // Import ConversationState, which stores the chatbot state

object Main: // Main is the starting point of the program

  // This is the first state of the chatbot before the user says anything.
  // It is private because only Main needs to use it.
  // It is a val because it should not be changed.
  private val initialState: ConversationState =
    ConversationState(
      history = List.empty, // No previous messages at the start
      preferences = Map.empty, // No saved user preferences at the start
      choices = List.empty, // No A/B choices made yet
      currentQuestion = None // No active question at the start
    )

  // This is the first function that runs when the program starts.
  def main(args: Array[String]): Unit =
    println(ChatbotCore.greetUser()) // Print the welcome message from ChatbotCore
    runChat(initialState) // Start the chatbot loop using the empty initial state

  // This function keeps the chatbot running.
  // It receives the current ConversationState each time it is called.
  private def runChat(state: ConversationState): Unit =
    Option(scala.io.StdIn.readLine("You: ")) // Read input safely; Option protects against null input
      .map(_.trim) // Remove extra spaces from the beginning and end of the input
      .filter(_.nonEmpty) match // Ignore empty input and pattern match on the result

      case None => // This happens if the input is empty or unavailable
        println("Bot: Empty input received. Please type a command.")
        runChat(state) // Keep the chatbot running with the same state

      case Some(input) if ChatbotCore.isExitInput(input) => // If the user typed bye, exit, or quit
        println("Bot: Thanks for playing. See you next time!") // End message
        // No recursive call here, so the program stops

      case Some(input) => // Normal non-empty input
        val (response, nextState) =
          ChatbotCore.handleUserInput(input, state) // Get the bot response and the updated state

        println(s"Bot: $response") // Print the chatbot response
        runChat(nextState) // Continue the chatbot using the updated state