val scala3Version = "3.5.1"

import org.scalajs.linker.interface.ModuleSplitStyle
import org.scalajs.jsenv.nodejs.NodeJSEnv

lazy val root = project
  .enablePlugins(ScalaJSPlugin)
  .in(file("."))
  .settings(
    name         := "Scala.js experimental WASM exercise",
    version      := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalameta" %%% "munit" % "1.0.0" % Test,
    scalaJSLinkerConfig := {
      scalaJSLinkerConfig.value
        .withModuleSplitStyle(
          ModuleSplitStyle.FewestModules,
        )                                     // required by the Wasm backend
        .withExperimentalUseWebAssembly(true) // use the Wasm backend
        .withModuleKind(ModuleKind.ESModule)  // required by the Wasm backend

    },

// Configure Node.js (at least v22) to support the required Wasm features
    jsEnv := {
      val config = NodeJSEnv
        .Config()
        .withArgs(
          List(
            "--experimental-wasm-exnref", // required
            "--experimental-wasm-imported-strings", // optional (good for performance)
            "--turboshaft-wasm", // optional, but significantly increases stability
          ),
        )
      new NodeJSEnv(config)
    },
  )
