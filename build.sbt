//val ivyLocal = Resolver.file("ivy", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

val settings = Common.settings ++ Seq(
  name := "AMFRunner",
  version := "0.0.1",

  libraryDependencies ++= Seq(
    "org.mule.amf" %% "amf-client" % "1.3.0-SNAPSHOT",
    "org.raml" % "raml-parser-2" % "1.0.17",
    "org.raml" % "raml-parser-2" % "1.0.17",
    "org.raml" % "raml-parser" % "0.8.21",
    "io.swagger" % "swagger-parser" % "1.0.33",
    "com.typesafe.play" %% "play-json" % "2.6.3",
    "com.lihaoyi" %% "scalatags" % "0.6.7",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
    "org.scalactic" %% "scalactic" % "3.0.1",
    "com.github.benhutchison" %% "prickle" % "1.1.13",
    "org.scalatest" %% "scalatest" % "3.0.0" % Test,
    "io.spray" %%  "spray-json" % "1.3.3"
  ),
  resolvers ++= List(Common.releasesPublic, Common.snapshots, Common.external, Resolver.mavenLocal),
  credentials ++= Common.credentials(),

  // Redefining publish for valkyr pipeline, which includes a publish task
  publish := {
    println("..Future publish task...")
  }
)

//resolvers += ivyLocal

lazy val root = project
  .in(file("."))
  .settings(settings: _*)

addCommandAlias("report", "; run src/main/resources/tests.zip")

lazy val coverage = taskKey[Unit]("Prints '...Future coverage...'")
coverage := println("..Future coverage task...")
// uncomment this to run main with coverage task
coverage := (runMain in Compile).toTask(" org.mulesoft.tck.Main /Users/lucas.block/mulesoft/oas-raml-converter-lrg notTckRepo").value

lazy val coverageReport = taskKey[Unit]("Prints '...Future coverage report...'")
coverageReport := println("..Future coverage report task...")
