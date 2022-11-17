package nl.gn0s1s.gpctree

import munit.ScalaCheckSuite

class GpcTreeSuite extends ScalaCheckSuite {
  val gpcTree = new GpcTree(scala.io.Source.fromResource("taxonomy-with-ids.nl-NL.txt"))

  property("can find a node") {
    assertEquals(gpcTree.findNodeInTree("6975"), Some(Node(Placement("6975", "Biochemicaliën"), Seq.empty)))
  }

  property("can find the parent of a node") {
    assertEquals(gpcTree.findParentInTree("6975").map(_.placement.code), Some("1624"))
  }

  property("can find the path of a node") {
    assertEquals(gpcTree.findPathInTree("6975").map(_.placement.code), Seq("111", "1624", "6975"))
  }

  property("can find common ancestors between nodes") {
    assertEquals(gpcTree.findCommonAncestors("6975", "4555").map(_.placement.code), Seq("111", "1624"))
  }

  property("can reconstruct a line in the taxonomy file") {
    assertEquals(
      gpcTree.findNodeInTree("500105").map(gpcTree.asString),
      Some("500105 - Eten, drinken en tabak > Voedselitems > Specerijen en sauzen > Witte en roomsauzen")
    )
  }
}
