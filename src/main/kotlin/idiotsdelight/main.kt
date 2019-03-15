package idiotsdelight

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt


class CommandHandling : CliktCommand() {
    val choice: String by option(help = "How to run, interactive or simulation").prompt("Interactive(1), One game(2) or Simulation(3)?")

    override fun run() {
        if (choice == "1") {
            oneGame()
        } else if( choice == "2"){
            oneGame(false)
        } else {
            manySilentSimulations(100000)
        }
    }
}

fun main(args: Array<String>) = CommandHandling().main(args)

fun oneGame(interactiveMode: Boolean = true) {
    println("Press enter/return key to move the game forward")
    val score = IdiotsDelight(Board(), interactiveMode, printSteps = true).play()
    if (score == 4) {
        println("Congrats!")
    } else {
        println("Sorry, try again!")
    }
}

fun manySilentSimulations(noOfTimes: Int) {

    println("Be patient, we are running the game $noOfTimes times!")

    var countSuccess = 0
    val buckets = hashMapOf<Int, Int>()
    for (key in 4..52) {
        buckets[key] = 0
    }

    for (playCount in 1..noOfTimes) {
        val table = IdiotsDelight(Board(), interactiveMode = false)
        val cardsCount = table.play()

        var count = buckets[cardsCount]
        if (count != null) {
            count++
        } else {
            count = 1
        }
        buckets[cardsCount] = count

        if (cardsCount == 4) {
            countSuccess++
        }

    }

    val bucketsPercent = hashMapOf<Int, Double>()
    for (cardCount in 4..52) {
        val count = buckets[cardCount]
        if (count != null) {
            bucketsPercent[cardCount] = Math.round(count / noOfTimes.toDouble() * 10000) / 100.toDouble()
        }

    }

    print("Cards left    ")
    for (cardCount in 4..25) {
        if (cardCount < 10) {
            print("┃   $cardCount  ")
        } else {
            print("┃  $cardCount  ")
        }
    }
    println()
    print("% of the runs ")

    // use joinToString() on array instead!
    for (cardCount in 4..25) {
        print("┃ " + String.format("%.02f", bucketsPercent[cardCount]) + " ")
    }
    println()
}

