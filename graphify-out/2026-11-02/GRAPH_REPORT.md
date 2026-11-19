# Graph Report - Tiro_Parab-lico  (2026-11-02)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 1136 nodes · 2531 edges · 72 communities (13 shown, 59 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 19 edges (avg confidence: 0.85)
- Token cost: 4,114 input · 790 output

## Graph Freshness
- Built from commit: `f53adce4`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Ball Customization
- Wall Type Configuration
- Main Application Entry
- Shot Metrics Tracking
- Physics Engine Factory
- Canvas Rendering Engine
- Simulation Environment
- Input and Coordinates
- Trajectory Calculations
- UI Panel State
- Physics World Management
- Ball Physics Properties
- Editor Mode Manager
- Game Menu Builder
- Launch Input Handler
- Environment Decorative Sprites
- Projectile Physics Model
- Simulation Scene States
- Simulation Integration Tests
- Heads-Up Display
- Sprite Management System
- Simulation Mode Factory
- Physics Data Readout
- Procedural Environment Generator
- Camera Control System
- Wall Rendering Logic
- Placement Mode Logic
- Menu and Idle States
- Simulation State Interface
- Rest State Detector
- Free Fall Tests
- CLI Application Controller
- Coordinate Transformation Tools
- Drawing Context Wrapper
- Physics Vector Visualization
- Wall Destruction Effects
- Static Image Sprites
- Environment Physics Settings
- Free Fall Mode
- Drag Input Logic
- Simulation Component Factory
- Game Presenter Logic
- Projectile Launch State
- Control Panel UI
- Collision Resolution System
- Wall Creation Tool
- Box Collision Shape
- Parabolic Motion Mode
- Collision Penetration Logic
- Capsule Collision Shape
- Polygon Collision Shape
- Obstacle Damage Logic
- Sprite Lifecycle Manager
- Physics Simulation Step
- Collision Shape Types
- Circle Collision Shape
- Sprite Creation Factory
- Simulation Mode Registry
- Simulation Coordinate Mapping
- Debug Testing
- Diagnostic Utilities
- Documentation Generation
- UI Control Sections
- UI Style Definitions
- Project Name

## God Nodes (most connected - your core abstractions)
1. `Vector2D` - 292 edges
2. `Simulacion` - 166 edges
3. `Pared` - 100 edges
4. `TipoPelota` - 58 edges
5. `Proyectil` - 48 edges
6. `Pelota` - 46 edges
7. `MainApp` - 45 edges
8. `TipoPared` - 43 edges
9. `LecturaModo` - 39 edges
10. `AppTest` - 37 edges

## Surprising Connections (you probably didn't know these)
- `MotorFisico` --calls--> `ResolutorColisiones`  [INFERRED]
  app/src/main/kotlin/org/example/fisica/MotorFisico.kt → app/src/main/kotlin/org/example/fisica/ResolutorColisiones.kt
- `Simulacion` --calls--> `IntegradorVuelo`  [INFERRED]
  app/src/main/kotlin/org/example/simulacion/Simulacion.kt → app/src/main/kotlin/org/example/simulacion/IntegradorVuelo.kt
- `Simulacion` --calls--> `GestorEdicion`  [INFERRED]
  app/src/main/kotlin/org/example/simulacion/Simulacion.kt → app/src/main/kotlin/org/example/simulacion/GestorEdicion.kt
- `Simulacion` --calls--> `SuperficiesLanzamiento`  [INFERRED]
  app/src/main/kotlin/org/example/simulacion/Simulacion.kt → app/src/main/kotlin/org/example/simulacion/SuperficiesLanzamiento.kt
- `Simulacion` --calls--> `ControladorCamara`  [INFERRED]
  app/src/main/kotlin/org/example/simulacion/Simulacion.kt → app/src/main/kotlin/org/example/simulacion/ControladorCamara.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Graphify Tooling Suite** — gemini_graphify, gemini_graphify_out_graph_json, gemini_graphify_out_wiki_index_md, gemini_graphify_out_graph_report_md [EXTRACTED 1.00]

## Communities (72 total, 59 thin omitted)

### Community 0 - "Ball Customization"
Cohesion: 0.06
Nodes (19): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis (+11 more)

### Community 1 - "Wall Type Configuration"
Cohesion: 0.05
Nodes (9): ConstructorParedes, CreadorParedes, TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA (+1 more)

### Community 2 - "Main Application Entry"
Cohesion: 0.10
Nodes (3): LanzadorJuego, MainApp, AnimationTimer

### Community 3 - "Shot Metrics Tracking"
Cohesion: 0.06
Nodes (5): MetricasTiro, ObservadorFinSimulacion, RegistroObservadoresFin, ResultadoSimulacion, MenuFinJuego

### Community 4 - "Physics Engine Factory"
Cohesion: 0.07
Nodes (5): FabricaMotorFisico, FabricaMotorFisicoPredeterminada, PhysicsEngine, Pared, PrevisualizadorTrayectoria

### Community 15 - "Environment Decorative Sprites"
Cohesion: 0.10
Nodes (3): Arbol, Brizna, Nube

### Community 18 - "Simulation Scene States"
Cohesion: 0.10
Nodes (3): EscenaEstado, EnVuelo, Inactivo

### Community 21 - "Heads-Up Display"
Cohesion: 0.11
Nodes (4): Hud, Mundo, Renderable, Renderer

### Community 26 - "Camera Control System"
Cohesion: 0.15
Nodes (3): Camara, CamaraSimulacion, ControladorCamara

### Community 31 - "Rest State Detector"
Cohesion: 0.20
Nodes (3): DetectorDeReposo, FabricaDetectoresReposo, DetectorReposo

### Community 42 - "Simulation Component Factory"
Cohesion: 0.31
Nodes (4): FabricaAmbiente, FabricaTierra, ComponentesSimulacion, FabricaSimulacion

### Community 57 - "Collision Shape Types"
Cohesion: 0.25
Nodes (5): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO

### Community 65 - "Documentation Generation"
Cohesion: 0.50
Nodes (4): graphify, graphify-out/graph.json, graphify-out/GRAPH_REPORT.md, graphify-out/wiki/index.md

## Knowledge Gaps
- **21 isolated node(s):** `BEISBOL`, `BLOQUE`, `BOLICHE`, `FUTBOL`, `GOMA` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 306 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **59 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Input and Coordinates` to `Ball Customization`, `Main Application Entry`, `Physics Engine Factory`, `Canvas Rendering Engine`, `Simulation Environment`, `Trajectory Calculations`, `Physics World Management`, `Ball Physics Properties`, `Editor Mode Manager`, `Game Menu Builder`, `Launch Input Handler`, `Game State Actions`, `Projectile Physics Model`, `Simulation Scene States`, `UI Layout Components`, `Simulation Integration Tests`, `Heads-Up Display`, `Sprite Management System`, `Simulation Mode Factory`, `Physics Data Readout`, `Camera Control System`, `Wall Rendering Logic`, `Placement Mode Logic`, `Menu and Idle States`, `Simulation State Interface`, `Rest State Detector`, `Free Fall Tests`, `CLI Application Controller`, `Coordinate Transformation Tools`, `Drawing Context Wrapper`, `Physics Vector Visualization`, `Static Image Sprites`, `Environment Physics Settings`, `Free Fall Mode`, `Drag Input Logic`, `Simulation Component Factory`, `Game Presenter Logic`, `Projectile Launch State`, `Collision Resolution System`, `Wall Creation Tool`, `Box Collision Shape`, `Parabolic Motion Mode`, `Math and Collision Utils`, `Collision Penetration Logic`, `Capsule Collision Shape`, `Polygon Collision Shape`, `Obstacle Damage Logic`, `Sprite Lifecycle Manager`, `Physics Simulation Step`, `Circle Collision Shape`, `Unit Testing Framework`, `Sprite Creation Factory`, `Simulation Coordinate Mapping`, `Debug Testing`, `Diagnostic Utilities`?**
  _High betweenness centrality (0.528) - this node is a cross-community bridge._
- **Are the 8 inferred relationships involving `Simulacion` (e.g. with `ControladorCamara` and `GestorEdicion`) actually correct?**
  _`Simulacion` has 8 INFERRED edges - model-reasoned connections that need verification._
- **What connects `BEISBOL`, `BLOQUE`, `BOLICHE` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Ball Customization` be split into smaller, more focused modules?**
  _Cohesion score 0.05706760316066725 - nodes in this community are weakly interconnected._
- **Why does `Simulacion` connect `Simulation Environment` to `Ball Customization`, `Wall Type Configuration`, `Main Application Entry`, `Shot Metrics Tracking`, `Physics Engine Factory`, `Input and Coordinates`, `Ball Physics Properties`, `Editor Mode Manager`, `Launch Input Handler`, `Game State Actions`, `Simulation Scene States`, `Simulation Integration Tests`, `Sprite Management System`, `Simulation Mode Factory`, `Camera Control System`, `Menu and Idle States`, `Simulation State Interface`, `Free Fall Tests`, `Coordinate Transformation Tools`, `Wall Destruction Effects`, `Simulation Component Factory`, `Game Presenter Logic`, `Projectile Launch State`, `Wall Creation Tool`, `Parabolic Motion Mode`, `Math and Collision Utils`, `Unit Testing Framework`, `Debug Testing`, `Diagnostic Utilities`?**
  _High betweenness centrality (0.221) - this node is a cross-community bridge._
- **Should `Wall Type Configuration` be split into smaller, more focused modules?**
  _Cohesion score 0.04846938775510204 - nodes in this community are weakly interconnected._
- **Why does `Pared` connect `Physics Engine Factory` to `Ball Customization`, `Wall Type Configuration`, `Canvas Rendering Engine`, `Input and Coordinates`, `Trajectory Calculations`, `Physics World Management`, `Ball Physics Properties`, `Editor Mode Manager`, `Heads-Up Display`, `Wall Rendering Logic`, `Rest State Detector`, `Coordinate Transformation Tools`, `Wall Destruction Effects`, `Collision Resolution System`, `Wall Creation Tool`, `Collision Penetration Logic`, `Obstacle Damage Logic`, `Physics Simulation Step`, `Unit Testing Framework`?**
  _High betweenness centrality (0.095) - this node is a cross-community bridge._