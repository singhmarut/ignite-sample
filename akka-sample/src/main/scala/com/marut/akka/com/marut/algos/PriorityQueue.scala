package com.marut.akka.com.marut.algos

import akka.actor.{Props, ActorSystem}

/**
 * Created by marutsingh on 12/25/16.
 */
class PriorityQueue {
  //merge 2 sorted list
  //Create list equal to total size of both of them
  //iterate over 1 list (which is greater in length)
  //compare a[0] with b[0]
  //if a[i] < b[i] then c[i] =  a[i] i++ else c[i] = b[i]

  def mergeSortedLists(a: List[Int], b: List[Int] ): Unit ={
      val length: Integer = math.max(a.size,b.size)
  }


  def main(args: Array[String]) {

    println("Hello, world!")
  }
}
