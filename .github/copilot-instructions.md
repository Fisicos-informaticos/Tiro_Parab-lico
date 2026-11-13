# Copilot instructions

## Build, test, and run

Use the Gradle wrapper from the repository root:

- `./gradlew :app:build` — compile and run the project's verification tasks.
- `./gradlew :app:test` — run all tests.
- `./gradlew :app:test --tests 'org.example.AppTest.alcancePara45Grados'` — run one test method; replace the class and method with the target test.
- `./gradlew :app:run` — launch the JavaFX application.

The app uses Kotlin/JVM on Java 21 and JavaFX 21.0.2. There is no dedicated lint task configured.

## Architecture

This is a Kotlin/JavaFX desktop simulator for parabolic projectile motion. The Gradle build has one application module, `app`.

- `MainApp` is the JavaFX entry point and composition layer for the window, canvas, controls, menus, and game loop. `LanzadorJuego` launches it.
- `simulacion/Simulacion` is the presentation-independent simulation coordinator. Its `ContextoJuego` interface exposes the state needed by UI/rendering, while `FabricaSimulacion` is the composition root that wires concrete implementations through simulation ports.
- Simulation behavior is split among session/state objects, command handlers, flight integration, trajectory preview, camera control, editing, effects, and metrics under `simulacion/`. Simulation modes live under `modos/`.
- The physics engine and collision model are separated into `fisica/` and `modelo/`; `PhysicsEngine` and related interfaces allow the simulation to depend on abstractions rather than concrete physics implementations.
- JavaFX drawing is organized under `grafico/`: renderers and drawing collaborators consume simulation state rather than owning game rules. Menus and control panels are under `ui/`, and input adapters are under `entrada/`.
- Tests are in `app/src/test/kotlin`. `AppTest` covers physics, collision, and environment behavior; `SimulacionTest` and `CaidaLibreTest` cover simulation behavior.

## Codebase conventions

- Keep game rules out of JavaFX classes. UI handlers should forward user actions through `UIEventListener`/simulation APIs; rendering should read state through the simulation context.
- Preserve the coordinator-and-collaborator pattern used by `MainApp` and `Simulacion`: coordinators route work to focused services instead of accumulating rules, rendering, UI construction, or physics responsibilities.
- Keep concrete dependency wiring in factories such as `FabricaSimulacion`; simulation services communicate through the interfaces declared in `Puertos.kt`.
- Simulation modes and states own their state-specific transitions and input/update behavior. Route state changes through the simulation/session rather than duplicating transition logic in UI code.
- Source identifiers and package names are Spanish; follow the existing terminology and package boundaries when adding code.
- `graphify-out/graph.json` is the repository's code knowledge graph. Before searching or reading source for a codebase question, run `graphify query "<question>"`; use `graphify path "<A>" "<B>"` for relationships and `graphify explain "<concept>"` for a focused view. Read source only after the graph locates relevant files/lines. Run `graphify update .` after code changes to refresh the graph.
