package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.event.LoggingReceive
import akka.actor.ActorRef
import akka.actor.Terminated
import play.libs.Akka
import akka.actor.Props

class BoardActor extends Actor with ActorLogging {
  var users = Set[ActorRef]()

  def receive = LoggingReceive { 
    case m:Message => {
      println("###[Actors/BoardActor.scala#receive]")
      println(m)
      users map { _ ! m}
    }
    case Subscribe => {
      println("###[Actors/BoardActor.scala#receive]")
      println("Subscribe")
      println(sender)
      users += sender
      context watch sender
    }
    case Terminated(user) => {
      println("###[Actors/BoardActor.scala#receive]")
      println("Terminated")
      println(user)
      users -= user
    }
  }
}

object BoardActor {
  println("###[Actors/BoardActor.scala#BoardActor]")
  lazy val board = Akka.system().actorOf(Props[BoardActor])
  def apply() = board
}

case class Message(uuid: String, s: String)
object Subscribe
