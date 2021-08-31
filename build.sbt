import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.integrationTestSettings
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val appName = "capmovie-frontend"

val silencerVersion = "1.7.3"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
  .settings(
    majorVersion                     := 0,
    scalaVersion                     := "2.12.13",
    libraryDependencies              ++= AppDependencies.compile ++ AppDependencies.test,
    TwirlKeys.templateImports ++= Seq(
      "uk.gov.hmrc.govukfrontend.views.html.components._",
      "uk.gov.hmrc.govukfrontend.views.html.helpers._",
      "uk.gov.hmrc.hmrcfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.helpers._"
    ),
    pipelineStages in Assets := Seq(gzip),
    // ***************
    // Use the silencer plugin to suppress warnings
    scalacOptions += "-P:silencer:pathFilters=routes",
    libraryDependencies ++= Seq(
      compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
      "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full,
      "org.jsoup"  %  "jsoup"  % "1.14.2",
    )

    // ***************
  )
  .settings(publishingSettings: _*)
  .configs(IntegrationTest)
  .settings(integrationTestSettings(): _*)
  .settings(resolvers += Resolver.jcenterRepo)
  .settings(scoverageSettings)


libraryDependencies += ws
libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % "test, it"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"
libraryDependencies += "com.github.tomakehurst" % "wiremock-jre8" % "2.30.1"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.4"
libraryDependencies += "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-28" % "0.50.0"
libraryDependencies += "uk.gov.hmrc.mongo" %% "hmrc-mongo-test-play-28" % "0.50.0"

lazy val scoverageSettings = Seq(
  ScoverageKeys.coverageExcludedPackages := "<empty>;Reverse.*;config.*;.*(AuthService|BuildInfo|Routes).*;.*views.html*;.*ErrorHandler*;",
  ScoverageKeys.coverageMinimumStmtTotal := 95,
  ScoverageKeys.coverageFailOnMinimum := true,
  ScoverageKeys.coverageHighlighting := true
)

scalacOptions += s"-Wconf:src=${target.value}/.*:s"

PlayKeys.devSettings := Seq("play.server.http.port" -> "9001")
