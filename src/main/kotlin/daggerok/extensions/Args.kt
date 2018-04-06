@file:JvmName("Args")

package daggerok.extensions

/**
 * Extends pair of strings to key(string)-values(list of string) pair
 */
fun Pair<String, String>.splitValues(): Pair<String, List<String>> {

  val (prefix, suffix) = this
  val key = prefix.substring(2)
  val values = suffix
      .split(";", ",")
      .filterNot { it.trim().isBlank() }

  return key to values
}

/**
 * Parse main arguments.
 *
 * This is a simple and strict parser supported following rules:
 * <ul>
 *   <li>any argument required value</li>
 *   <li>accepted key-value format: --key=value</li>
 *   <li>arguments could be split with spaces or tabs</li>
 * </ul>
 *
 * If you need more advanced CLI, please use something like:
 * <ul>
 *   <li><a href="http://commons.apache.org/proper/commons-cli/">Apache Commons CLI library</a></li>
 *   <li><a href="http://www.martiansoftware.com/jsap/">JSAP: the Java Simple Argument Parser</a></li>
 *   <li>or some other analogs...</li>
 * </ul>
 *
 * That one was developed in order to be smallest as possible and less dependent
 *
 * Accepted only values of types:
 * <ul>
 *  <li>String</li>
 *  <li>Integer</li>
 *  <li>Long</li>
 *  <li>Boolean</li>
 * </ul>
 *
 * <pre><code>
 *
 *   java -jar app.jar \
 *        --string="Hello, World!" \
 *        --strings=value-1,"value 2" \
 *        --integer=-1 \
 *        --integers=0,22,-5 \
 *        --long=-123123132123 \
 *        --longs=1234567890,0,-1 \
 *        --boolean=true,
 *        --booleans=true,false,false,true,true,false
 *
 * </code></pre>
 */
fun Array<String>.props() = this
    .toList()
    //.flatMap { it.split("\\s+".toRegex()) }
    .filterNot { it.trim().isBlank() }
    .filter { it.startsWith("--") }
    .filter { it.contains("=") }
    .map { it.split("=") }
    .filterNot { it[0].isBlank() }
    .filterNot { it[1].trim().isBlank() }
    .map { it[0] to it[1] }
    .map { it.splitValues() }
    .filterNot { it.second.isEmpty() }
    .toMap()

/**
 * Extends System property string to key(string)-values(list of strings) pair
 */
fun String.systemPropertyToKeyValuesPair() =
    (this to listOf(System.getProperty(this, "").trim()))

/**
 * System properties
 */
fun Array<String>.systemProperties() = System
    .getProperties()
    .propertyNames()
    .toList()
    .filterNotNull()
    .map { it.toString() }
    .filterNot { it.isBlank() }
    .map { it.systemPropertyToKeyValuesPair() }
    .filterNot { it.second.isEmpty() }
    .toMap()

/**
 * Extends Environment variable string to key(string)-values(list of strings) pair
 */
fun String.systemEnvToKeyValuesPair() =
    this to listOf(System.getenv(this).trim())

/**
 * Environment variables
 */
fun Array<String>.environmentVariables() = System
    .getenv()
    .keys
    .filterNot { it.isNullOrBlank() }
    .map { it.systemEnvToKeyValuesPair() }
    .filterNot { it.second.isEmpty() }
    .toMap()

/**
 * Merge all properties together:
 *
 * <ul>
 *   <li>props</li>
 *   <li>system properties</li>
 *   <li>environment variables</li>
 * </ul>
 */
fun Array<String>.allProps() = this
    .props()
    .entries
    .union(this.systemProperties().entries)
    .union(this.environmentVariables().entries)
    .map { (it.key to it.value) }
    .toMap()

/**
 * Any strings
 */
fun Map<String, List<String>>.anyStrings(key: String, defaultValues: List<String>): List<String> {
  val values = this.getOrDefault(key, emptyList())
  return if (values.isEmpty()) defaultValues else values
}

/**
 * Any strings or default
 */
fun Map<String, List<String>>.anyStrings(key: String) = anyStrings(key, emptyList())

/**
 * First string
 */
fun Map<String, List<String>>.firstString(key: String, defaultValue: String) =
    this.getOrDefault(key, listOf(defaultValue))
        .first()

/**
 * First string or default
 */
fun Map<String, List<String>>.firstString(key: String) = firstString(key, "")

/**
 * Any integers
 */
fun Map<String, List<String>>.anyIntegers(key: String, defaultValues: List<Int>): List<Int> {
  val values = this.getOrDefault(key, emptyList())
  return if (values.isEmpty()) defaultValues else values.map { it.toInt() }
}

/**
 * Any integers or default
 */
fun Map<String, List<String>>.anyIntegers(key: String) = anyIntegers(key, emptyList())

/**
 * First integer
 */
fun Map<String, List<String>>.firstInteger(key: String, defaultValue: Int) =
    this.getOrDefault(key, listOf(defaultValue.toString()))
        .map { it.toInt() }
        .first()

/**
 * First integer or default
 */
fun Map<String, List<String>>.firstInteger(key: String) = firstInteger(key, 0)

/**
 * Any longs
 */
fun Map<String, List<String>>.anyLongs(key: String, defaultValues: List<Long>): List<Long> {
  val values = this.getOrDefault(key, emptyList())
  return if (values.isEmpty()) defaultValues else values.map { it.toLong() }
}

/**
 * Any longs or default
 */
fun Map<String, List<String>>.anyLongs(key: String) = anyLongs(key, emptyList())

/**
 * First long
 */
fun Map<String, List<String>>.firstLong(key: String, defaultValue: Long) =
    this.getOrDefault(key, listOf(defaultValue.toString()))
        .map { it.toLong() }
        .first()

/**
 * First long or default
 */
fun Map<String, List<String>>.firstLong(key: String) = firstLong(key, 0L)

/**
 * Any booleans
 */
fun Map<String, List<String>>.anyBooleans(key: String, defaultValue: List<Boolean>): List<Boolean> {
  val values = this.getOrDefault(key, emptyList())
  return if (values.isEmpty()) defaultValue else values.map { it.toBoolean() }
}

/**
 * Any booleans or default
 */
fun Map<String, List<String>>.anyBooleans(key: String) = anyBooleans(key, emptyList())

/**
 * First boolean
 */
fun Map<String, List<String>>.firstBoolean(key: String, defaultValue: Boolean) =
    this.getOrDefault(key, listOf(defaultValue.toString()))
        .map { it.toBoolean() }
        .first()

/**
 * First boolean or default
 */
fun Map<String, List<String>>.firstBoolean(key: String) = firstBoolean(key, false)
