package activeobject

import groovy.transform.ActiveMethod
import groovy.transform.ActiveObject

@ActiveObject
class HelloWorld {
    @ActiveMethod(blocking = false)
    void greet(String whom, HelloWorldBot replyTo) {
        println "Hello $whom!"
        replyTo.greeted(whom, this)
    }
}

@ActiveObject
class HelloWorldBot {
    private final int max
    private int greetingCounter

    HelloWorldBot(int max) { this.max = max }

    @ActiveMethod(blocking = false)
    void greeted(String whom, HelloWorld from) {
        greetingCounter++
        println "Greeting $greetingCounter for $whom"
        if (greetingCounter < max) from.greet(whom, this)
    }
}

@ActiveObject
class HelloWorldMain {
    private final HelloWorld greeter = new HelloWorld()

    @ActiveMethod(blocking = false)
    void sayHello(String name) {
        greeter.greet(name, new HelloWorldBot(3))
    }
}

var main = new HelloWorldMain()

main.sayHello('World')
main.sayHello('Groovy 6')

sleep 2000
