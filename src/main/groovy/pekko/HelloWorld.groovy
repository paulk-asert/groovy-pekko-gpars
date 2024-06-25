package pekko

import groovy.transform.CompileStatic
import org.apache.pekko.actor.typed.ActorRef
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior
import org.apache.pekko.actor.typed.javadsl.ActorContext
import org.apache.pekko.actor.typed.javadsl.Behaviors
import org.apache.pekko.actor.typed.javadsl.Receive

class HelloWorld extends AbstractBehavior<Greet> {

    static record Greet(String whom, ActorRef<Greeted> replyTo) {}
    static record Greeted(String whom, ActorRef<Greet> from) {}

    static Behavior<Greet> create() {
        Behaviors.setup(HelloWorld::new)
    }

    private HelloWorld(ActorContext<Greet> context) {
        super(context)
    }

    @Override
    Receive<Greet> createReceive() {
        newReceiveBuilder().onMessage(Greet.class, this::onGreet).build()
    }

    @CompileStatic
    private Behavior<Greet> onGreet(Greet command) {
        context.log.info "Hello $command.whom!"
        command.replyTo.tell(new Greeted(command.whom, context.self))
        this
    }
}

