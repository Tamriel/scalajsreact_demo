package app

import app.MainComponent.Tree

import com.softwaremill.quicklens._

case class SimpleDatabase(tree: Tree) {

  def setText(id: String, newText: String): SimpleDatabase =
    this.modify(_.tree.items.at(id).text).setTo(newText)

  def deleteItem(id: String): SimpleDatabase =
    this.modify(_.tree.items(id))
//
//  def addSibling(id: String): Tree = ???
//
//  def addChild(id: String): Tree = ???
//
//  def moveUp(id: String): Tree = ???
//
//  def moveDown(id: String): Tree = ???
//
//  def moveLeft(id: String): Tree = ???
//
//  def moveRight(id: String): Tree = ???
}
