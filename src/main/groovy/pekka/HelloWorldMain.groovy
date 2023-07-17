package pekka

import org.apache.pekko.actor.typed.ActorRef
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.javadsl.AbstractBehavior
import org.apache.pekko.actor.typed.javadsl.ActorContext
import org.apache.pekko.actor.typed.javadsl.Behaviors
import org.apache.pekko.actor.typed.javadsl.Receive

class HelloWorldMain extends AbstractBehavior<HelloWorldMain.SayHello> {

    static record SayHello(String name) { }

    static Behavior<SayHello> create() {
        Behaviors.setup(HelloWorldMain::new)
    }

    private final ActorRef<HelloWorld.Greet> greeter

    private HelloWorldMain(ActorContext<SayHello> context) {
        super(context)
        greeter = context.spawn(HelloWorld.create(), 'greeter')
    }

    @Override
    Receive<SayHello> createReceive() {
        newReceiveBuilder().onMessage(SayHello.class, this::onStart).build()
    }

    private Behavior<SayHello> onStart(SayHello command) {
        var replyTo = context.spawn(HelloWorldBot.create(3), command.name)
        greeter.tell(new HelloWorld.Greet(command.name, replyTo))
        this
    }
}
