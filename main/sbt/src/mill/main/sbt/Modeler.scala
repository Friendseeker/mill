package mill.main.sbt

import java.io.File
import java.util.Properties

import org.jetbrains.sbt._
import org.jetbrains.sbt.XmlSerializer._

import scala.sys.process.Process

/**
 * Builds a [[Model]].
 *
 */

@mill.api.internal
object Modeler {

  def apply(sbtBuildDir: os.Path,
            providedSbtPath: Option[os.Path],
            sbtStructureConfig: SbtStructureConfig,
            structureDir: os.Path): StructureData = {
    // Install sbt
    val sbtPath = providedSbtPath.getOrElse {
      val fetchSbt = new FetchSbt(ExecutionContext.global)
      fetchSbt(version)
    }
    // Write plugin file
    writeSbtStructurePluginFile(sbtBuildDir, sbtStructureConfig)
    // invoke sbt & extract project structure
    // TODO: respect structureDir
    val command = "dumpStructureTo structure.xml"
    generateSBTStructure(sbtPath)
  }

  private def generateSbtStructure(sbtPath: os.Path, command: String): StructureData = {
    val command = "dumpStructureTo structure.xml"
    val exitcode = Process(sbtPath.toString(), command) !
    val structureXml: Elem = XML.load()
    val structure: Either[Throwable, StructureData] = structureXml.deserialize[StructureData]
  }

  private def writeSbtStructurePluginFile(sbtBuildDir: os.Path, sbtStructureConfig: SbtStructureConfig): Unit = {
    val (sbtStructureVersion, sbtVersion) = (sbtStructureConfig.sbtStructureVersion, sbtStructureConfig.sbtVersion)

    val projectStructurePlugin = sbtBuildDir / "project" / "mill.sbt"

    os.write(projectStructurePlugin,
      s"""
         |addSbtPlugin("org.jetbrains.scala" % "sbt-structure-extractor" % "${sbtStructureVersion}", "${sbtVersion}")
         |""".stripMargin)
  }
}

@mill.api.internal
case class SbtStructureConfig(val sbtStructureVersion: String, val sbtVersion: String)
