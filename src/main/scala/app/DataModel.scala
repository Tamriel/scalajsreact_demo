package app

import java.util.UUID

object DataModel {
  case class ItemId(id: UUID)

  object ItemId {
    def random = ItemId(UUID.randomUUID)
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
