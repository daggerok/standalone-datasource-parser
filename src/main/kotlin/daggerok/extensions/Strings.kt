@file:JvmName("Strings")

package daggerok.extensions

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.xml.stream.XMLInputFactory

private val log = LoggerFactory.getLogger("daggerok.extensions.Strings")

// <server xmlns="urn:jboss:domain:1.7">...</server>
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "server", namespace = "urn:jboss:domain:1.7")
data class Server(@JacksonXmlElementWrapper(localName = "profile")
                  var profile: List<Map<Any, Any>>? = null)

fun Server.getMap(key: String): Map<*, *> =
    this.profile
        .orEmpty()
        .first { it.containsKey(key) }

fun Map<*, *>.getMap(key: String): Map<*, *> =
    if (this.containsKey(key)) this[key] as Map<*, *> else mapOf<Any, Any>()

fun Map<*, *>.getString(key: String) =
    if (this.containsKey(key)) this[key] as String else ""

fun String.parseDatasource(): Map<*, *> {

  val path = Paths.get(this)

  if (Files.notExists(path) || Files.isDirectory(path)) {
    val message = "standalone.xml file '$this' not found"
    log.error(message)
    return mapOf("error" to message)
  }

  val xml = Paths.get(this).toFile()
  val xmlStream = XMLInputFactory.newInstance().createXMLStreamReader(FileInputStream(xml))
  val objectMapper = XmlMapper()

  objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
  objectMapper.registerModule(JaxbAnnotationModule())

  val rootElement = objectMapper.readValue(xmlStream, Server::class.java)
  val subsystem = rootElement.getMap("datasources")

  if (!subsystem.containsKey("datasources")) {
    val message = "datasources not found"
    log.error(message)
    return mapOf("error" to message)
  }

  val datasources = subsystem.getMap("datasources")

  if (!datasources.containsKey("datasource")) {
    val message = "datasource not found"
    log.error(message)
    return mapOf("error" to message)
  }

  val datasource = datasources.getMap("datasource")
  val security = datasource.getMap("security")
  val drivers = datasources.getMap("drivers")
  val driver = drivers.getMap("driver")

  xmlStream.close()

  return mapOf(
      "datasource" to mapOf(
          "jndi-name" to datasource.getString("jndi-name"),
          "pool-name" to datasource.getString("pool-name"),
          "enabled" to datasource.getString("enabled"),
          "use-java-context" to datasource.getString("use-java-context"),
          "security" to mapOf(
              "user-name" to security.getString("user-name"),
              "password" to security.getString("password")
          ),
          "driver" to mapOf(
              "name" to driver.getString("name"),
              "module" to driver.getString("module"),
              "xa-datasource-class" to driver.getString("xa-datasource-class")
          )
      )
  )
}
