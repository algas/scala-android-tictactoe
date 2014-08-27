import android.Keys._

lazy val buildSettings = Seq(
  version := "0.0.1-SNAPSHOT",
  organization := "com.geishatokyo",
  homepage := Some(url("http://www.geishatokyo.com")),
  licenses := Seq("MIT License" -> url("http://opensource.org/licenses/mit-license.php/")),
  scalaVersion := "2.11.2",
  scalacOptions := Seq("-deprecation", "-unchecked"),
  resolvers += Resolver.sonatypeRepo("public")
)

lazy val root = (project in file(".")).
  settings(buildSettings: _*).
  settings(androidBuild: _*).
  settings(
    name := "tictactoe",
    platformTarget in Android := "android-19",
    proguardOptions in Android ++= Seq("-dontwarn sun.misc.Unsafe")
  )
