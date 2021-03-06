//val ivyLocal = Resolver.file("ivy", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}


val settings = Common.settings ++ Seq(
  name := "amf-runner",
  version := "0.0.1",

  libraryDependencies ++= Seq(
    "com.github.amlorg" %% "amf-client" % "3.0.0-RC.3",
    "org.raml" % "raml-parser-2" % "1.0.29-SNAPSHOT",
    "org.raml" % "raml-parser" % "0.9-SNAPSHOT",
    "io.swagger" % "swagger-parser" % "1.0.33"
  ),
  resolvers ++= List(Common.releases, Common.snapshots, Resolver.mavenLocal),
  resolvers += "jitpack" at "https://jitpack.io",
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