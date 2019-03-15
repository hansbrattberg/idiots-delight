package idiotsdelight


fun main(args: Array<String>) {
    if (args.isEmpty()) {
        oneInteractiveGame()
    } else {
        manySilentSimulations(1000000)
    }
}

fun oneInteractiveGame() {
    println("Press enter/return key to move the game forward")
    val score = IdiotsDelight(Board(), printSteps = true, interactiveMode = true).play()
    if (score == 4) {
        println("Congrats!")
    } else {
        println("Sorry, try again!")
    }
}

fun manySilentSimulations(noOfTimes: Int) {
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
    for (cardCount in 4..52) {
        if (cardCount < 10) {
            print("┃   $cardCount  ")
        } else {
            print("┃  $cardCount  ")
        }
    }
    println()
    print("% of the runs ")

    // use joinToString() on array instead!
    for (cardCount in 4..52) {
        print("┃ " + String.format("%.02f", bucketsPercent[cardCount]) + " ")
    }
    println()
}