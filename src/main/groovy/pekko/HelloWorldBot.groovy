package pekko

import groovy.transform.CompileStatic
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior
import org.apache.pekko.actor.typed.javadsl.ActorContext
import org.apache.pekko.actor.typed.javadsl.Behaviors
import org.apache.pekko.actor.typed.javadsl.Receive

class HelloWorldBot extends AbstractBehavior<HelloWorld.Greeted> {

    static Behavior<HelloWorld.Greeted> create(int max) {
        Behaviors.setup(context -> new HelloWorldBot(context, max))
    }

    private final int max
    private int greetingCounter

    private HelloWorldBot(ActorContext<HelloWorld.Greeted> context, int max) {
        super(context)
        this.max = max
    }

    @Override
    Receive<HelloWorld.Greeted> createReceive() {
        newReceiveBuilder().onMessage(HelloWorld.Greeted.class, this::onGreeted).build()
    }

    @CompileStatic
    private Behavior<HelloWorld.Greeted> onGreeted(HelloWorld.Greeted message) {
        greetingCounter++
        context.log.info "Greeting $greetingCounter for $message.whom"
        if (greetingCounter == max) {
            return Behaviors.stopped()
        } else {
            message.from.tell(new HelloWorld.Greet(message.whom, context.self))
            return this
        }
    }
}
