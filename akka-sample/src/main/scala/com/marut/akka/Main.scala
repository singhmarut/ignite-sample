import akka.actor.Actor.Receive
import akka.actor.{Actor, Props, ActorSystem}

class Node(val name: String){

  var nodeList: List[Node] = _

  override def toString: String = {
    name
  }
}
class GraphBuilder {

  val graph: Array[Array[String]] = Array(Array("1", "2", "3"), Array("2", "4", "5"),Array("4", "6"),Array("5", "6"))

  def addEdge(): Unit ={

  }

  def buildGraph = {
    graph.map(a => {

      a.foreach(node => new Node(node))
    })
  }

}
class NodePrinter {
}

trait printable {
  def print() : Unit = println(this)
}

class MyActor extends Actor{
  override def receive: Receive = {
    case msg: String =>
      println()
  }
}

object Main {
  def main(args: Array[String]) {
    val node = new Node("adf") with printable
    node.print()

    val system = ActorSystem("mySystem")
    val myActor = system.actorOf(Props[MyActor], "myactor2")

    myActor ! "Bang"

    println("Hello, world!")
  }
}