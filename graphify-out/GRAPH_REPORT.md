# Graph Report - Tiro_Parab-lico  (2026-11-02)

## Corpus Check
- 104 files · ~24,215 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: (none) 2, .properties 2, .toml 1)

## Summary
- 1232 nodes · 2725 edges · 75 communities (13 shown, 62 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 31 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `f53adce4`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Ball Customization
- Wall Type Configuration
- Main Application Entry
- MetricasTiro
- Pared
- Canvas Rendering Engine
- Simulation Environment
- Input and Coordinates
- Trajectory Calculations
- UI Panel State
- Physics World Management
- Ball Physics Properties
- Editor Mode Manager
- MenusJuego
- Launch Input Handler
- GeneradorEntorno
- PhysicsEngine
- Proyectil
- Simulation Scene States
- UI Layout Components
- Simulation Integration Tests
- ComandosPartida
- Sprite Management System
- Simulation Mode Factory
- Physics Data Readout
- TransformadorCoordenadas
- Camera Control System
- Wall Rendering Logic
- Placement Mode Logic
- Menu and Idle States
- Simulation State Interface
- Rest State Detector
- Free Fall Tests
- PresentadorConsola
- Coordinate Transformation Tools
- Drawing Context Wrapper
- Physics Vector Visualization
- Wall Destruction Effects
- Static Image Sprites
- ResultadoSimulacion
- Free Fall Mode
- Drag Input Logic
- Simulation Component Factory
- Game Presenter Logic
- Projectile Launch State
- Control Panel UI
- DibujanteObjetos
- Wall Creation Tool
- FormaCaja
- Parabolic Motion Mode
- DibujantePantallaInicio
- ObservadorFinSimulacion
- Capsule Collision Shape
- Gravedades
- MecanicaTiro
- Sprite Lifecycle Manager
- Physics Simulation Step
- SuperficiesLanzamiento
- MenuFinJuego
- Sprite Creation Factory
- DibujanteFlechaDireccion
- Simulation Coordinate Mapping
- Debug Testing
- Diagnostic Utilities
- Documentation Generation
- IntegradorVuelo
- UI Style Definitions
- Project Name
- DibujantePanelInfo
- DibujoVectoresFisica

## God Nodes (most connected - your core abstractions)
1. `Vector2D` - 307 edges
2. `Simulacion` - 169 edges
3. `Pared` - 103 edges
4. `TipoPelota` - 63 edges
5. `Proyectil` - 50 edges
6. `Pelota` - 48 edges
7. `TipoPared` - 45 edges
8. `MainApp` - 42 edges
9. `LecturaModo` - 39 edges
10. `AppTest` - 37 edges

## Surprising Connections (you probably didn't know these)
- `MotorFisico` --calls--> `ResolutorColisiones`  [INFERRED]
  app/src/main/kotlin/org/example/fisica/MotorFisico.kt → app/src/main/kotlin/org/example/fisica/ResolutorColisiones.kt
- `CanvasRenderer` --calls--> `DibujanteFlechaDireccion`  [INFERRED]
  app/src/main/kotlin/org/example/grafico/CanvasRenderer.kt → app/src/main/kotlin/org/example/grafico/DibujanteFlechaDireccion.kt
- `CanvasRenderer` --calls--> `DibujanteObjetos`  [INFERRED]
  app/src/main/kotlin/org/example/grafico/CanvasRenderer.kt → app/src/main/kotlin/org/example/grafico/DibujanteObjetos.kt
- `CanvasRenderer` --calls--> `DibujantePanelInfo`  [INFERRED]
  app/src/main/kotlin/org/example/grafico/CanvasRenderer.kt → app/src/main/kotlin/org/example/grafico/DibujantePanelInfo.kt
- `CanvasRenderer` --calls--> `DibujantePantallaInicio`  [INFERRED]
  app/src/main/kotlin/org/example/grafico/CanvasRenderer.kt → app/src/main/kotlin/org/example/grafico/DibujantePantallaInicio.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Graphify Tooling Suite** — gemini_graphify, gemini_graphify_out_graph_json, gemini_graphify_out_wiki_index_md, gemini_graphify_out_graph_report_md [EXTRACTED 1.00]

## Communities (75 total, 62 thin omitted)

### Community 0 - "Ball Customization"
Cohesion: 0.06
Nodes (19): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis (+11 more)

### Community 1 - "Wall Type Configuration"
Cohesion: 0.05
Nodes (8): Obstaculo, TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA, UIEventListener

### Community 2 - "Main Application Entry"
Cohesion: 0.09
Nodes (4): BucleJuego, AnimationTimer, LanzadorJuego, MainApp

### Community 10 - "Physics World Management"
Cohesion: 0.10
Nodes (3): MotorFisico, Tierra, FormaCirculo

### Community 13 - "MenusJuego"
Cohesion: 0.09
Nodes (3): DetectorFinDeVuelo, GestorMenus, MenusJuego

### Community 15 - "GeneradorEntorno"
Cohesion: 0.07
Nodes (6): DibujanteEntorno, Arbol, Brizna, GeneradorEntorno, Meta, Nube

### Community 17 - "Proyectil"
Cohesion: 0.05
Nodes (7): ResolutorColisiones, Hud, Mundo, Renderable, Renderer, Penetracion, Proyectil

### Community 18 - "Simulation Scene States"
Cohesion: 0.10
Nodes (3): EscenaEstado, EnVuelo, Inactivo

### Community 31 - "Rest State Detector"
Cohesion: 0.10
Nodes (4): Ambiente, DetectorDeReposo, FabricaDetectoresReposo, DetectorReposo

### Community 33 - "PresentadorConsola"
Cohesion: 0.13
Nodes (3): App, EntradaConsola, PresentadorConsola

### Community 42 - "Simulation Component Factory"
Cohesion: 0.18
Nodes (7): FabricaAmbiente, FabricaTierra, FabricaMotorFisico, FabricaMotorFisicoPredeterminada, ComponentesSimulacion, FabricaSimulacion, PrevisualizadorTrayectoria

### Community 48 - "FormaCaja"
Cohesion: 0.07
Nodes (7): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO, FormaCaja, FormaPoligono

### Community 65 - "Documentation Generation"
Cohesion: 0.50
Nodes (4): graphify, graphify-out/graph.json, graphify-out/GRAPH_REPORT.md, graphify-out/wiki/index.md

## Knowledge Gaps
- **21 isolated node(s):** `CARTON`, `MADERA`, `LADRILLO`, `HORMIGON`, `ACERO` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 323 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **62 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Input and Coordinates` to `Ball Customization`, `Wall Type Configuration`, `Main Application Entry`, `Pared`, `Canvas Rendering Engine`, `Simulation Environment`, `Trajectory Calculations`, `Physics World Management`, `Ball Physics Properties`, `Editor Mode Manager`, `MenusJuego`, `Launch Input Handler`, `GeneradorEntorno`, `Proyectil`, `Simulation Scene States`, `Simulation Integration Tests`, `ComandosPartida`, `Sprite Management System`, `Simulation Mode Factory`, `Physics Data Readout`, `TransformadorCoordenadas`, `Camera Control System`, `Wall Rendering Logic`, `Placement Mode Logic`, `Menu and Idle States`, `Simulation State Interface`, `Rest State Detector`, `Free Fall Tests`, `PresentadorConsola`, `Coordinate Transformation Tools`, `Drawing Context Wrapper`, `Physics Vector Visualization`, `Static Image Sprites`, `Free Fall Mode`, `Drag Input Logic`, `Simulation Component Factory`, `Game Presenter Logic`, `Projectile Launch State`, `DibujanteObjetos`, `Wall Creation Tool`, `FormaCaja`, `Parabolic Motion Mode`, `Capsule Collision Shape`, `MecanicaTiro`, `Sprite Lifecycle Manager`, `Physics Simulation Step`, `Unit Testing Framework`, `Sprite Creation Factory`, `DibujanteFlechaDireccion`, `Simulation Coordinate Mapping`, `Debug Testing`, `Diagnostic Utilities`, `IntegradorVuelo`, `DibujoVectoresFisica`?**
  _High betweenness centrality (0.527) - this node is a cross-community bridge._
- **Are the 13 inferred relationships involving `Simulacion` (e.g. with `ComandosEdicion` and `ComandosPartida`) actually correct?**
  _`Simulacion` has 13 INFERRED edges - model-reasoned connections that need verification._
- **What connects `CARTON`, `MADERA`, `LADRILLO` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Ball Customization` be split into smaller, more focused modules?**
  _Cohesion score 0.05706760316066725 - nodes in this community are weakly interconnected._
- **Why does `Simulacion` connect `Simulation Environment` to `Ball Customization`, `Wall Type Configuration`, `Main Application Entry`, `MetricasTiro`, `Pared`, `Input and Coordinates`, `UI Panel State`, `Ball Physics Properties`, `Editor Mode Manager`, `MenusJuego`, `Launch Input Handler`, `PhysicsEngine`, `Simulation Scene States`, `Simulation Integration Tests`, `ComandosPartida`, `Simulation Mode Factory`, `TransformadorCoordenadas`, `Camera Control System`, `Menu and Idle States`, `Simulation State Interface`, `Free Fall Tests`, `PresentadorConsola`, `Coordinate Transformation Tools`, `Wall Destruction Effects`, `Simulation Component Factory`, `Game Presenter Logic`, `Projectile Launch State`, `Wall Creation Tool`, `Parabolic Motion Mode`, `ObservadorFinSimulacion`, `Capsule Collision Shape`, `Gravedades`, `MecanicaTiro`, `SuperficiesLanzamiento`, `Unit Testing Framework`, `Debug Testing`, `Diagnostic Utilities`, `IntegradorVuelo`, `.distanciaRecorrida`?**
  _High betweenness centrality (0.243) - this node is a cross-community bridge._
- **Should `Wall Type Configuration` be split into smaller, more focused modules?**
  _Cohesion score 0.048625792811839326 - nodes in this community are weakly interconnected._
- **Why does `Pared` connect `Pared` to `Ball Customization`, `Wall Type Configuration`, `Coordinate Transformation Tools`, `Canvas Rendering Engine`, `Wall Destruction Effects`, `Input and Coordinates`, `Trajectory Calculations`, `Unit Testing Framework`, `Simulation Component Factory`, `Physics World Management`, `Editor Mode Manager`, `Wall Creation Tool`, `PhysicsEngine`, `Proyectil`, `Physics Simulation Step`, `SuperficiesLanzamiento`, `Wall Rendering Logic`, `Rest State Detector`?**
  _High betweenness centrality (0.108) - this node is a cross-community bridge._