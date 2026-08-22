package groovy6

import groovy.concurrent.Actor

record Greet(String whom, Actor<Greeted> replyTo) { }

record Greeted(String whom, Actor<Greet> from) { }

record SayHello(String name) { }

Actor<Greet> greeter
greeter = Actor.reactor { Greet command ->
    println "Hello $command.whom!"
    command.replyTo.send(new Greeted(command.whom, greeter))
}

def newBot = { int max ->
    Actor<Greeted> bot
    bot = Actor.stateful(0) { int count, Greeted message ->
        int next = count + 1
        println "Greeting $next for $message.whom"
        if (next < max) message.from.send(new Greet(message.whom, bot))
        else bot.stop()
        next
    }
    bot
}

def main = Actor.reactor { SayHello command ->
    greeter.send(new Greet(command.name, newBot(3)))
}

main.send(new SayHello('World'))
main.send(new SayHello('Groovy 6'))

sleep 2000
main.stop()
greeter.stop()
