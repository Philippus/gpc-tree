package nl.gn0s1s.gpctree

import scala.annotation.tailrec
import scala.collection.Seq
import scala.io.BufferedSource

class GpcTree(source: BufferedSource) {
  lazy val googleProductCategoriesTree: Seq[Node] = buildTree()

  private def readSourceIntoSeq(): scala.collection.mutable.Buffer[(String, String)] = {
    val seq: scala.collection.mutable.Buffer[(String, String)] = scala.collection.mutable.Buffer.empty[(String, String)]
    for (line <- source.getLines().filterNot(l => l.isEmpty || l.startsWith("#"))) {
      val Array(code, category) = line
        .split("\\ -\\ ", 2)
      seq.+=((code, category))
    }
    seq
  }

  @tailrec private def treeHugger(queue: Seq[Node], code: String, function: Node => Boolean): Option[Node] = {
    queue match {
      case Seq() =>
        None
      case Seq(hd, tl @ _*) =>
        if (function(hd))
          Some(hd)
        else
          treeHugger(hd.children ++ tl, code, function)
    }
  }

  def findCommonAncestors(code1: String, code2: String): Seq[Node] =
    findPathInTree(code1).intersect(findPathInTree(code2))

  def findPathInTree(code: String): Seq[Node] = {
    @tailrec def helper(code: String, acc: Seq[Node]): Seq[Node] = {
      findParentInTree(code) match {
        case None =>
          acc
        case Some(parent) =>
          helper(parent.placement.code, parent +: acc)
      }
    }
    val node = findNodeInTree(code)
    node match {
      case None =>
        Seq.empty
      case Some(node) =>
        helper(node.placement.code, Seq(node))
    }
  }

  def findNodeInTree(code: String): Option[Node] = {
    treeHugger(googleProductCategoriesTree, code, n => n.placement.code == code)
  }

  def findParentInTree(code: String): Option[Node] = {
    treeHugger(googleProductCategoriesTree, code, n => n.children.exists(_.placement.code == code))
  }

  def asString(node: Node) = {
    @tailrec def helper(nodes: Seq[Node], acc: String): String = {
      nodes match {
        case Seq() => acc
        case Seq(hd) => acc + hd.placement.category
        case Seq(hd, tl @ _*) => helper(tl, acc + hd.placement.category + " > ")
      }
    }
    val path = findPathInTree(node.placement.code)
    helper(path, path.lastOption.map(_.placement.code + " - ").getOrElse(""))
  }

  def buildTree(): Seq[Node] = {
    val googleProductCategories = readSourceIntoSeq()

    @tailrec def helper(queue: Seq[Node], acc: Seq[Node]): Seq[Node] = {
      queue match {
        case Seq() =>
          acc
        case Seq(hd, tl @ _*) =>
          val parentCode = googleProductCategories
            .find(_._1 == hd.placement.code)
            .flatMap(leaf =>
              googleProductCategories.find(_._2 == leaf._2.splitAt(leaf._2.lastIndexOf(" > "))._1).map(_._1)
            )
          parentCode match {
            case None =>
              helper(tl, acc :+ hd)
            case Some(parentCode) =>
              val parentIndex = tl.indexWhere(node => node.placement.code == parentCode)
              val parentNode = tl(parentIndex)
              helper(
                tl.patch(
                  parentIndex,
                  Seq(
                    parentNode.copy(children =
                      parentNode.children :+ hd.copy(placement =
                        Placement(
                          hd.placement.code,
                          hd.placement.category.splitAt(hd.placement.category.lastIndexOf(" > ") + 3)._2
                        )
                      )
                    )
                  ),
                  1
                ),
                acc
              )
          }
      }
    }

    val l: Seq[Node] =
      googleProductCategories.sortBy(-_._2.count(_ == '>')).map(x => Node(Placement(x._1, x._2), Seq.empty[Node]))
    helper(l, Seq.empty[Node])
  }
}
