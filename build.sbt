name := "slackeroni"

version := "1.0"

lazy val `slackeroni` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  ehcache,
  ws,
  filters
)

val appDependencies = Seq(
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "org.playframework.anorm" %% "anorm" % "2.6.2"
)

libraryDependencies ++= appDependencies

javaOptions in Universal ++= Seq("-Dpidfile.path=/dev/null")
