package mill.main.sbt

import mainargs.{Flag, ParserForClass, arg, main}
import mill.main.build.{BuildObject, Node, Tree}
import mill.runner.FileImportGraph.backtickWrap

import scala.collection.immutable.{SortedMap, SortedSet}
import scala.jdk.CollectionConverters.*

/**
 * Converts a Sbt build to Mill by generating Mill build file(s) from POM file(s).
 *
 * The generated output should be considered scaffolding and will likely require edits to complete conversion.
 *
 * ===Capabilities===
 * The conversion
 *  - handles deeply nested modules
 *  - captures project metadata
 *  - configures dependencies for scopes:
 *    - compile
 *    - provided
 *    - runtime
 *    - test
 *  - configures testing frameworks:
 *    - JUnit 4
 *    - JUnit 5
 *    - TestNG
 *  - configures multiple, compile and test, resource directories
 *
 * ===Limitations===
 * The conversion does not support:
 *  - plugins, other than maven-compiler-plugin
 *  - packaging, other than jar, pom
 *  - build extensions
 *  - build profiles
 */
@mill.api.internal
object BuildGen {

  def main(args: Array[String]): Unit = {
    // Start by just extracting project structure
    
  }
}

@main
@mill.api.internal
case class BuildGenConfig(
    @arg(doc = "name of generated base module trait defining project metadata settings")
    baseModule: Option[String] = None,
    @arg(doc = "name of generated nested test module")
    testModule: String = "test",
    @arg(doc = "name of generated companion object defining constants for dependencies")
    depsObject: Option[String] = None,
    @arg(doc = "capture properties defined in pom.xml for publishing")
    publishProperties: Flag = Flag(),
    @arg(doc = "merge build files generated for a multi-module build")
    merge: Flag = Flag(),
) extends ModelerConfig
