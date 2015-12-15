package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.event.LoggingReceive
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import akka.actor.ActorRef
import akka.actor.Props
import scala.xml.Utility


class UserActor(uid: String, board: ActorRef, out: ActorRef) extends Actor with ActorLogging {


  override def preStart() = {
    BoardActor() ! Subscribe
  }

  def receive = LoggingReceive {
    case Message(muid, s) if sender == board => {
      println("###[Actors/UserActor#receive]")
      println("Message sender == board")
      val js = Json.obj("type" -> "message", "uid" -> muid, "msg" -> s)
      out ! js
    }
    case js: JsValue => {
      println("###[Actors/UserActor#receive]")
      println("JSValue")
      println(js)
      println(js \ "msg")
      println((js \ "msg").validate[String])
        (js \ "msg").validate[String] map {
          Utility.escape(_)
        } map {
          board ! Message(uid, _ )
        }
    }
    case other => {
      println("###[Actors/UserActor#receive]")
      println("other")
      log.error("unhandled: " + other)
    }
  }
}

object UserActor {
  def props(uid: String)(out: ActorRef) = Props(new UserActor(uid, BoardActor(), out))
}
