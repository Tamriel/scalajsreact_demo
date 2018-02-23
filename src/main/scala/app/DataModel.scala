package app

import java.util.UUID

object DataModel {
  case class ItemId(id: UUID)

  object ItemId {
    def random = ItemId(UUID.randomUUID)
  }

  implicit class InsertVector(vector: Vector[ItemId]) {
    def insert(position: Int, t: ItemId): Vector[ItemId] = {
      val splitted = vector.splitAt(position)
      splitted._1 ++ Vector(t) ++ splitted._2
    }
  }

  case class TreeItem(parentId: ItemId,
                      id: ItemId = ItemId.random,
                      text: String = "",
                      childrenIds: Vector[ItemId] = Vector.empty,
                      deleted: Boolean = false,
                      expanded: Boolean = true) {
    def indexOf(item: TreeItem): Int = childrenIds.indexOf(item.id)
  }

  case class Tree(items: Map[ItemId, TreeItem])

  val ROOTID = ItemId(UUID.fromString("03e405e1-0750-418b-bf08-1cdb1d5bd25b"))
}
