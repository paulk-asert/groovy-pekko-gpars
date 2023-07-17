import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.DefaultActor

import static groovyx.gpars.actor.Actors.actor

record Greet(String whom, Actor replyTo) { }

record Greeted(String whom, Actor from) {}

record SayHello(String name) { }

helloWorld = actor {
    loop {
        react { Greet command ->
            println "Hello $command.whom!"
            command.replyTo << new Greeted(command.whom, helloWorld)
        }
    }
}

class BotActor extends DefaultActor {
    int max
    private int greetingCounter = 0
    @Override
    protected void act() {
        loop {
            react { Greeted message ->
                greetingCounter++
                println "Greeting $greetingCounter for $message.whom"
                if (greetingCounter < max) message.from << new Greet(message.whom, this)
                else terminate()
            }
        }
    }
}

var main = actor {
    loop {
        react { SayHello command ->
            helloWorld << new Greet(command.name, new BotActor(max: 3).start())
        }
    }
}

main << new SayHello('World')
main << new SayHello('GPars')

sleep 2000
main.terminate()
