# Graph Report - Tiro_Parab-lico  (2026-11-06)

## Corpus Check
- 109 files · ~25,743 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: (none) 2, .properties 2, .toml 1)

## Summary
- 1472 nodes · 3161 edges · 81 communities (18 shown, 63 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 32 edges (avg confidence: 0.85)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `9ed954e0`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Ball Customization
- TipoPared
- Main Application Entry
- MetricasTiro
- Pared
- Canvas Rendering Engine
- Simulacion
- Input and Coordinates
- Trajectory Calculations
- EstadoPanel
- MotorFisico
- ContextoJuego
- Editor Mode Manager
- MenusJuego
- Launch Input Handler
- GeneradorEntorno
- PhysicsEngine
- Proyectil
- Simulation Scene States
- SesionPartida
- Pelota
- ComandosPartida
- GestionSprites
- ModoSimulacion
- Physics Data Readout
- GestorMenus
- .componentes
- MetricasTiro
- Placement Mode Logic
- Menu and Idle States
- Simulation State Interface
- Ambiente
- Free Fall Tests
- PresentadorConsola
- Coordinate Transformation Tools
- Drawing Context Wrapper
- Physics Vector Visualization
- Wall Destruction Effects
- Static Image Sprites
- UIEventListener
- Free Fall Mode
- Drag Input Logic
- SesionJuego
- Game Presenter Logic
- Projectile Launch State
- Penetracion
- DibujanteObjetos
- FormaCaja
- FormaCapsula
- Parabolic Motion Mode
- DibujantePantallaInicio
- ComandosEdicion
- FormaPoligono
- Sprite Lifecycle Manager
- CatalogoModos
- ComponentesSimulacion
- Unit Testing Framework
- Sprite Creation Factory
- DibujanteFlechaDireccion
- FabricaSimulacion.kt
- Documentation Generation
- PanelControl
- UI Style Definitions
- Project Name
- DibujantePanelInfo
- Obstaculo
- Renderable
- Metricas
- EditorEscenario
- ConstructorParedes
- .crear
- ComandosEditor
- Mecanica
- EnSuelo
- EnVuelo
- SeccionControl.kt

## God Nodes (most connected - your core abstractions)
1. `Vector2D` - 337 edges
2. `Pared` - 120 edges
3. `Simulacion` - 119 edges
4. `ContextoJuego` - 105 edges
5. `TipoPelota` - 75 edges
6. `Pelota` - 55 edges
7. `Proyectil` - 51 edges
8. `TipoPared` - 48 edges
9. `ComponentesSimulacion` - 44 edges
10. `MainApp` - 42 edges

## Surprising Connections (you probably didn't know these)
- `CatalogoModos` --calls--> `ModoCaidaLibre`  [INFERRED]
  app/src/main/kotlin/org/example/modos/CatalogoModos.kt → app/src/main/kotlin/org/example/modos/ModoCaidaLibre.kt
- `CatalogoModos` --calls--> `ModoTiroParabolico`  [INFERRED]
  app/src/main/kotlin/org/example/modos/CatalogoModos.kt → app/src/main/kotlin/org/example/modos/ModoTiroParabolico.kt
- `App` --calls--> `EntradaConsola`  [INFERRED]
  app/src/main/kotlin/org/example/App.kt → app/src/main/kotlin/org/example/EntradaConsola.kt
- `App` --calls--> `PresentadorConsola`  [INFERRED]
  app/src/main/kotlin/org/example/App.kt → app/src/main/kotlin/org/example/PresentadorConsola.kt
- `ConstructorEstadoPanel` --references--> `CoordinadorMenus`  [EXTRACTED]
  app/src/main/kotlin/org/example/ConstructorEstadoPanel.kt → app/src/main/kotlin/org/example/CoordinadorMenus.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Graphify Tooling Suite** — gemini_graphify, gemini_graphify_out_graph_json, gemini_graphify_out_wiki_index_md, gemini_graphify_out_graph_report_md [EXTRACTED 1.00]

## Communities (81 total, 63 thin omitted)

### Community 0 - "Ball Customization"
Cohesion: 0.06
Nodes (19): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis (+11 more)

### Community 1 - "TipoPared"
Cohesion: 0.10
Nodes (6): TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA

### Community 2 - "Main Application Entry"
Cohesion: 0.09
Nodes (4): BucleJuego, AnimationTimer, LanzadorJuego, MainApp

### Community 3 - "MetricasTiro"
Cohesion: 0.07
Nodes (5): ObservadorFinSimulacion, ObservadoresFin, RegistroObservadoresFin, ResultadoSimulacion, MenuFinJuego

### Community 6 - "Simulacion"
Cohesion: 0.05
Nodes (4): Simulacion, DbgTest, Diag, SimulacionTest

### Community 7 - "Input and Coordinates"
Cohesion: 0.09
Nodes (3): InputListener, Vector2D, CondicionPrevia

### Community 15 - "GeneradorEntorno"
Cohesion: 0.08
Nodes (6): DibujanteEntorno, Arbol, Brizna, GeneradorEntorno, Meta, Nube

### Community 17 - "Proyectil"
Cohesion: 0.05
Nodes (9): DetectorDeReposo, FabricaDetectoresReposo, DetectorReposo, FabricaResolutor, FabricaResolutorPredeterminada, ResolutorColisiones, ResolutorFisico, Mundo (+1 more)

### Community 25 - "GestorMenus"
Cohesion: 0.05
Nodes (4): CoordinadorMenus, DetectorFinDeVuelo, GestorMenus, MenuJuego

### Community 26 - ".componentes"
Cohesion: 0.12
Nodes (3): Camara, CamaraSimulacion, PosicionPelota

### Community 27 - "MetricasTiro"
Cohesion: 0.07
Nodes (3): DibujanteParedes, MetricasTiro, SuperficiesLanzamiento

### Community 31 - "Ambiente"
Cohesion: 0.11
Nodes (4): Ambiente, FabricaMotorFisico, FabricaMotorFisicoPredeterminada, PrevisualizadorTrayectoria

### Community 33 - "PresentadorConsola"
Cohesion: 0.13
Nodes (3): App, EntradaConsola, PresentadorConsola

### Community 48 - "FormaCapsula"
Cohesion: 0.08
Nodes (7): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO, FormaCapsula, FormaCirculo

### Community 58 - "ComponentesSimulacion"
Cohesion: 0.08
Nodes (8): ComponentesSimulacion, ComandosJuego, ControlCamara, Efectos, Integrador, Previsualizador, SuperficiesApoyo, UbicadorPelota

### Community 62 - "FabricaSimulacion.kt"
Cohesion: 0.16
Nodes (4): TransformadorCoordenadasSimulacion, FabricaAmbiente, FabricaTierra, FabricaSimulacion

### Community 65 - "Documentation Generation"
Cohesion: 0.50
Nodes (4): graphify, graphify-out/graph.json, graphify-out/GRAPH_REPORT.md, graphify-out/wiki/index.md

### Community 76 - "Renderable"
Cohesion: 0.25
Nodes (3): Hud, Renderable, Renderer

## Knowledge Gaps
- **21 isolated node(s):** `CARTON`, `MADERA`, `LADRILLO`, `HORMIGON`, `ACERO` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 424 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **63 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Input and Coordinates` to `Ball Customization`, `Main Application Entry`, `Pared`, `Canvas Rendering Engine`, `Simulacion`, `Trajectory Calculations`, `MotorFisico`, `ContextoJuego`, `Editor Mode Manager`, `MenusJuego`, `Launch Input Handler`, `GeneradorEntorno`, `PhysicsEngine`, `Proyectil`, `Simulation Scene States`, `SesionPartida`, `Pelota`, `ComandosPartida`, `GestionSprites`, `ModoSimulacion`, `Physics Data Readout`, `.componentes`, `MetricasTiro`, `Placement Mode Logic`, `Menu and Idle States`, `Simulation State Interface`, `Ambiente`, `Free Fall Tests`, `PresentadorConsola`, `Coordinate Transformation Tools`, `Drawing Context Wrapper`, `Physics Vector Visualization`, `Static Image Sprites`, `Free Fall Mode`, `Drag Input Logic`, `SesionJuego`, `Game Presenter Logic`, `Projectile Launch State`, `Penetracion`, `DibujanteObjetos`, `FormaCaja`, `FormaCapsula`, `Parabolic Motion Mode`, `MenusJuego.kt`, `FormaColision.kt`, `FormaPoligono`, `Sprite Lifecycle Manager`, `ComponentesSimulacion`, `Unit Testing Framework`, `Sprite Creation Factory`, `DibujanteFlechaDireccion`, `FabricaSimulacion.kt`, `Obstaculo`, `EditorEscenario`, `.crear`, `ComandosEditor`, `Mecanica`, `EnSuelo`?**
  _High betweenness centrality (0.519) - this node is a cross-community bridge._
- **What connects `CARTON`, `MADERA`, `LADRILLO` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Ball Customization` be split into smaller, more focused modules?**
  _Cohesion score 0.05706760316066725 - nodes in this community are weakly interconnected._
- **Why does `ContextoJuego` connect `ContextoJuego` to `Ball Customization`, `TipoPared`, `MetricasTiro`, `Simulacion`, `EstadoPanel`, `Launch Input Handler`, `Simulation Scene States`, `Pelota`, `ModoSimulacion`, `Physics Data Readout`, `GestorMenus`, `Menu and Idle States`, `Simulation State Interface`, `PresentadorConsola`, `Wall Destruction Effects`, `Game Presenter Logic`, `Projectile Launch State`, `FormaColision.kt`, `EnSuelo`, `EnVuelo`?**
  _High betweenness centrality (0.153) - this node is a cross-community bridge._
- **Should `TipoPared` be split into smaller, more focused modules?**
  _Cohesion score 0.09956709956709957 - nodes in this community are weakly interconnected._
- **Why does `Simulacion` connect `Simulacion` to `Ball Customization`, `TipoPared`, `Main Application Entry`, `MetricasTiro`, `Pared`, `Input and Coordinates`, `ContextoJuego`, `Launch Input Handler`, `PhysicsEngine`, `SesionPartida`, `ComandosPartida`, `.componentes`, `Free Fall Tests`, `PresentadorConsola`, `Wall Destruction Effects`, `SesionJuego`, `Game Presenter Logic`, `ComandosEdicion`, `FormaColision.kt`, `ComponentesSimulacion`, `Unit Testing Framework`, `FabricaSimulacion.kt`, `Metricas`, `EditorEscenario`, `ComandosEditor`, `Mecanica`?**
  _High betweenness centrality (0.117) - this node is a cross-community bridge._
- **Should `Main Application Entry` be split into smaller, more focused modules?**
  _Cohesion score 0.08603145235892692 - nodes in this community are weakly interconnected._