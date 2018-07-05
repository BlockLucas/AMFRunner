//val ivyLocal = Resolver.file("ivy", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)

val settings = Common.settings ++ Seq(
  name := "amf-runner",
  version := "0.0.1",

  libraryDependencies ++= Seq(
    "com.github.amlorg" %% "amf-client" % "1.7.0-SNAPSHOT",
    "org.raml" % "raml-parser-2" % "1.0.23-SNAPSHOT",
    "org.raml" % "raml-parser" % "0.9-SNAPSHOT",
    "io.swagger" % "swagger-parser" % "1.0.33"
  ),
  resolvers ++= List(Common.releases, Common.snapshots, Resolver.mavenLocal),
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