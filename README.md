# Gpc-tree

[![build](https://github.com/Philippus/gpc-tree/workflows/build/badge.svg)](https://github.com/Philippus/gpc-tree/actions/workflows/scala.yml?query=workflow%3Abuild+branch%3Amain)
[![codecov](https://codecov.io/gh/Philippus/gpc-tree/branch/master/graph/badge.svg)](https://codecov.io/gh/Philippus/gpc-tree)
![Current Version](https://img.shields.io/badge/version-0.0.1-brightgreen.svg?style=flat "0.0.1")
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![license](https://img.shields.io/badge/license-MPL%202.0-blue.svg?style=flat "MPL 2.0")](LICENSE)

Gpc-tree is a library to work with the Google product categories / taxonomy in a tree structure where every node in the
tree consists of a placement in the Google Product Taxonomy and its children (if there are any). It can load any of the
available lists of [Google product categories](https://support.google.com/merchants/answer/6324436?hl=en) and has
methods to:

- find a node in the tree
- find the parent of a node
- find the path of a node
- find the common ancestors between nodes
- reconstruct a line in the taxonomy file

## Installation
Gpc-tree is published for Scala 2.13. To start using it add the following to your `build.sbt`:

    libraryDependencies += "nl.gn0s1s" %% "gpc-tree" % "0.0.1"

## Example usage

```scala
import java.nio.charset.StandardCharsets

import nl.gn0s1s.gpctree.GpcTree

val gpcTree = new GpcTree(scala.io.Source.fromURL("https://www.google.com/basepages/producttype/taxonomy-with-ids.nl-NL.txt", StandardCharsets.UTF_8.toString))

gpcTree.findNodeInTree("6975") // res0: Option[nl.gn0s1s.Node] = Some(Node(Placement(6975,Biochemicaliën),List()))

gpcTree.findNodeInTree("500105").map(gpcTree.asString) // res1: Option[String] = Some(500105 - Eten, drinken en tabak > Voedselitems > Specerijen en sauzen > Witte en roomsauzen)

// find siblings of a node
gpcTree.findParentInTree("500105").map(_.children) // res2: Option[scala.collection.Seq[nl.gn0s1s.Node]] = Some(List(Node(Placement(6772,Cocktailsaus),List()), Node(Placement(6905,Currysaus),List()), Node(Placement(6845,Dessertsauzen),List(Node(Placement(6854,Fruittoppings),List()), Node(Placement(6844,Siropen voor ijs),List()))), Node(Placement(4947,Honing),List()), Node(Placement(4614,Hot sauce),List()), Node(Placement(500076,Ingelegde vruchten en groenten),List()), Node(Placement(5762,Jus),List()), Node(Placement(2018,Ketchup),List()), Node(Placement(500074,Marinades en grillsauzen),List()), Node(Placement(1568,Mayonaise),List()), Node(Placement(6782,Mierikswortelsaus),List()), Node(Placement(1387,Mosterd),List()), Node(Placement(5760,Olijven en kappertjes),List()), Node(Placement(5759,Pastasauzen),List()),...
```

## Resources
- [Google product category](https://support.google.com/merchants/answer/6324436?hl=en)

## License
The code is available under the [Mozilla Public License, version 2.0](LICENSE).
