package pekka

import org.apache.pekko.actor.typed.ActorSystem

var system = ActorSystem.create(HelloWorldMain.create(), 'hello')

system.tell(new HelloWorldMain.SayHello('World'))
system.tell(new HelloWorldMain.SayHello('Pekko'))

sleep 2000
system.terminate()
