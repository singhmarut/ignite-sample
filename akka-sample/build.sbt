scalaVersion := "2.11.8"

lazy val root = (project in file(".")).
  settings(
    name := "hello",
    version := "1.0",
    scalaVersion := "2.11.8"
  );



libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.10" % "2.2.3"
)
