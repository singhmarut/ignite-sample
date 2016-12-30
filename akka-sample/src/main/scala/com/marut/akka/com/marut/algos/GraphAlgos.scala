package com.marut.akka.com.marut.algos

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Created by marutsingh on 12/28/16.
 */

class Graph(val rootNode: Int){

  var graph : scala.collection.mutable.HashMap[Int,ListBuffer[Int]] = new mutable.HashMap[Int,ListBuffer[Int]]
  init()

  def init(): Unit = {
    addNode(rootNode)
  }

  def addNode(node: Int): Unit = {
    if (!graph.contains(node)){
      graph += (node-> ListBuffer())
    }
  }

  def addEdge(startNode: Int, endNode: Int): Unit ={
    //It has to be made sure that a node is not added duplicate
    if (graph.contains(startNode)){
      graph(startNode) += (endNode) //Add the node of adjacency list of startNode
      addNode(endNode)
    }
  }

  def addVertex(startNode: Int, adjacencyList: ListBuffer[Int]): Unit ={
    if (graph.contains(startNode)){
      //Check for duplicates
      graph(startNode) ++= (adjacencyList)
    }else{
      graph += (startNode-> ListBuffer())
    }
  }

  def breadthFirstTraversal(node: Int): Unit = {
    println(node)
    if (graph(node).size > 0){
      graph(node).foreach(iNode => breadthFirstTraversal(iNode))
    }
  }
}
object GraphAlgos{

  def createGraph(): Unit ={
    val newGraph = new Graph(1)
    newGraph.addEdge(1,2)
    newGraph.addEdge(1,7)
    newGraph.addEdge(2,3)
    newGraph.addEdge(2,6)
    newGraph.addEdge(3,4)
    newGraph.addEdge(3,5)
    newGraph.addEdge(3,10)
    newGraph.addEdge(7,8)
    newGraph.addEdge(7,9)

    newGraph.breadthFirstTraversal(newGraph.rootNode)
  }

  def main(args: Array[String]): Unit = {
    createGraph()
  }
}
