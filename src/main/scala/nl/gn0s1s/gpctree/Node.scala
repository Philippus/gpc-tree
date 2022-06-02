package nl.gn0s1s.gpctree

import scala.collection.Seq

case class Node(placement: Placement, children: Seq[Node])
