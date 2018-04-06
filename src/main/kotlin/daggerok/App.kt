package daggerok

import daggerok.extensions.firstString
import daggerok.extensions.parseDatasource
import daggerok.extensions.props

fun main(args: Array<String>) {

  val props = args.props()
  val path = if (props.isEmpty()) "standalone-7.1.xml"
  else props.firstString("standalone-file")

  val datasource = path.parseDatasource()
  println(datasource)
}
