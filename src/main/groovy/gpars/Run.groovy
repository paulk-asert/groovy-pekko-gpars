import groovyx.gpars.actor.Actor
import groovyx.gpars.actor.DefaultActor
//import groovyx.gpars.actor.StaticDispatchActor

import static groovyx.gpars.actor.Actors.actor

record Greet(String whom, Actor replyTo) { }

record Greeted(String whom, Actor from) {}

record SayHello(String name) { }

greeter = actor {
    loop {
        react { Greet command ->
            println "Hello $command.whom!"
            command.replyTo << new Greeted(command.whom, greeter)
        }
    }
}

class HelloWorldBot extends DefaultActor {
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
            greeter << new Greet(command.name, new HelloWorldBot(max: 3).start())
        }
    }
}

main << new SayHello('World')
main << new SayHello('GPars')

sleep 2000
main.terminate()

/*
An alternative implementation:

class HelloWorldBot extends StaticDispatchActor<Greeted> {
    int max
    private int greetingCounter = 0

    @Override
    void onMessage(Greeted message) {
        greetingCounter++
        println "Greeting $greetingCounter for $message.whom"
        if (greetingCounter < max) message.from << new Greet(message.whom, this)
        else terminate()
    }
}
*/
