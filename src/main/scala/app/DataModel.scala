package app

import java.util.UUID

import scala.util.Random

object DataModel {
  case class TodoId(id: UUID)

  case class TreeItem(text: String = "",
                      id: String = Random.alphanumeric.take(10).mkString,
                      parentId: String = "is set later",
                      childrenIds: Vector[String] = Vector.empty,
                      deleted: Boolean = false,
                      expanded: Boolean = true) {
    def indexOf(item: TreeItem): Int = childrenIds.indexOf(item.id)
  }

  case class Tree(items: Map[String, TreeItem])

  val ROOTID = "root"
}
