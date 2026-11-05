name         := "gpc-tree"
organization := "nl.gn0s1s"
startYear    := Some(2022)
homepage     := Some(url("https://github.com/philippus/gpc-tree"))
licenses += ("MPL-2.0", url("https://www.mozilla.org/MPL/2.0/"))

developers := List(
  Developer(
    id = "philippus",
    name = "Philippus Baalman",
    email = "",
    url = url("https://github.com/philippus")
  )
)

crossScalaVersions := List("3.10.0")
scalaVersion       := crossScalaVersions.value.last

ThisBuild / versionScheme          := Some("semver-spec")
ThisBuild / versionPolicyIntention := Compatibility.BinaryCompatible

Compile / packageBin / packageOptions += Package.ManifestAttributes(
  "Automatic-Module-Name" -> "nl.gn0s1s.gpctree"
)

scalacOptions += "-deprecation"

libraryDependencies ++= Seq(
  "org.scalameta" %% "munit"            % "1.3.6" % Test,
  "org.scalameta" %% "munit-scalacheck" % "1.3.1" % Test
)

ThisBuild / turbo := true
