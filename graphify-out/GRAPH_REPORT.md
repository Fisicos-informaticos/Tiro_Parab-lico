# Graph Report - Tiro_Parab-lico  (2026-11-06)

## Corpus Check
- 109 files · ~25,432 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: (none) 2, .properties 2, .toml 1)

## Summary
- 1454 nodes · 3134 edges · 103 communities (19 shown, 84 thin omitted)
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
- AppTest
- EstadoPanel
- ContextoJuego
- Editor Mode Manager
- MenusJuego
- Launch Input Handler
- GeneradorEntorno
- PhysicsEngine
- Proyectil
- Simulation Scene States
- Pelota
- SimulacionTest
- ComandosPartida
- GestionSprites
- ModoSimulacion
- Physics Data Readout
- GestorMenus
- .componentes
- DibujanteParedes
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
- Transformaciones
- DibujanteObjetos
- FormaCaja
- FormaCapsula
- Parabolic Motion Mode
- DibujantePantallaInicio
- ComandosEdicion
- MenusJuego.kt
- DetectorReposo
- FormaPoligono
- Sprite Lifecycle Manager
- CatalogoModos
- Mundo
- ComponentesSimulacion
- Sprite Creation Factory
- DibujanteFlechaDireccion
- .componentes
- HerramientaParedes
- ResolutorColisiones
- Documentation Generation
- PanelControl
- UI Style Definitions
- Project Name
- DibujantePanelInfo
- DibujanteEntorno
- FabricaMotorFisico
- Obstaculo
- Renderable
- Metricas
- EditorEscenario
- ConstructorParedes
- CreadorParedes
- MecanicaTiro
- .crear
- ComandosEditor
- SuperficiesLanzamiento
- Mecanica
- ResolutorColisiones.kt
- EnSuelo
- UbicadorPelota
- EnVuelo
- Gravedades
- CalculadoraTrayectoria
- ComandosJuego
- FabricaModos
- SuperficiesApoyo
- TransformadorCoordenadasSimulacion
- EntradaConsola
- DbgTest.kt
- Diag.kt

## God Nodes (most connected - your core abstractions)
1. `Vector2D` - 333 edges
2. `Simulacion` - 119 edges
3. `Pared` - 118 edges
4. `ContextoJuego` - 105 edges
5. `TipoPelota` - 75 edges
6. `Pelota` - 55 edges
7. `Proyectil` - 51 edges
8. `TipoPared` - 48 edges
9. `ComponentesSimulacion` - 44 edges
10. `MainApp` - 42 edges

## Surprising Connections (you probably didn't know these)
- `App` --calls--> `EntradaConsola`  [INFERRED]
  app/src/main/kotlin/org/example/App.kt → app/src/main/kotlin/org/example/EntradaConsola.kt
- `App` --calls--> `PresentadorConsola`  [INFERRED]
  app/src/main/kotlin/org/example/App.kt → app/src/main/kotlin/org/example/PresentadorConsola.kt
- `CatalogoModos` --calls--> `ModoCaidaLibre`  [INFERRED]
  app/src/main/kotlin/org/example/modos/CatalogoModos.kt → app/src/main/kotlin/org/example/modos/ModoCaidaLibre.kt
- `CatalogoModos` --calls--> `ModoTiroParabolico`  [INFERRED]
  app/src/main/kotlin/org/example/modos/CatalogoModos.kt → app/src/main/kotlin/org/example/modos/ModoTiroParabolico.kt
- `ConstructorEstadoPanel` --references--> `CoordinadorMenus`  [EXTRACTED]
  app/src/main/kotlin/org/example/ConstructorEstadoPanel.kt → app/src/main/kotlin/org/example/CoordinadorMenus.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Graphify Tooling Suite** — gemini_graphify, gemini_graphify_out_graph_json, gemini_graphify_out_wiki_index_md, gemini_graphify_out_graph_report_md [EXTRACTED 1.00]

## Communities (103 total, 84 thin omitted)

### Community 0 - "Ball Customization"
Cohesion: 0.05
Nodes (19): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis (+11 more)

### Community 1 - "TipoPared"
Cohesion: 0.10
Nodes (6): TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA

### Community 2 - "Main Application Entry"
Cohesion: 0.09
Nodes (4): BucleJuego, AnimationTimer, LanzadorJuego, MainApp

### Community 3 - "MetricasTiro"
Cohesion: 0.05
Nodes (6): MetricasTiro, ObservadorFinSimulacion, ObservadoresFin, RegistroObservadoresFin, ResultadoSimulacion, MenuFinJuego

### Community 7 - "Input and Coordinates"
Cohesion: 0.09
Nodes (4): InputListener, Penetracion, Vector2D, CondicionPrevia

### Community 8 - "AppTest"
Cohesion: 0.12
Nodes (3): MotorFisico, Tierra, AppTest

### Community 14 - "Launch Input Handler"
Cohesion: 0.09
Nodes (4): InputHandler, CaidaLibre, TiroParabolico, ControladorCamara

### Community 15 - "GeneradorEntorno"
Cohesion: 0.12
Nodes (5): Arbol, Brizna, GeneradorEntorno, Meta, Nube

### Community 19 - "Pelota"
Cohesion: 0.06
Nodes (3): Pelota, IntegradorVuelo, SesionPartida

### Community 25 - "GestorMenus"
Cohesion: 0.05
Nodes (4): CoordinadorMenus, DetectorFinDeVuelo, GestorMenus, MenuJuego

### Community 26 - ".componentes"
Cohesion: 0.13
Nodes (3): Camara, CamaraSimulacion, PosicionPelota

### Community 31 - "Ambiente"
Cohesion: 0.13
Nodes (3): Ambiente, FabricaAmbiente, FabricaTierra

### Community 48 - "FormaCapsula"
Cohesion: 0.09
Nodes (7): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO, FormaCapsula, FormaCirculo

### Community 53 - "DetectorReposo"
Cohesion: 0.20
Nodes (3): DetectorDeReposo, FabricaDetectoresReposo, DetectorReposo

### Community 58 - "ComponentesSimulacion"
Cohesion: 0.13
Nodes (5): ComponentesSimulacion, ControlCamara, Efectos, Integrador, Previsualizador

### Community 65 - "Documentation Generation"
Cohesion: 0.50
Nodes (4): graphify, graphify-out/graph.json, graphify-out/GRAPH_REPORT.md, graphify-out/wiki/index.md

### Community 74 - "FabricaMotorFisico"
Cohesion: 0.28
Nodes (3): FabricaMotorFisico, FabricaMotorFisicoPredeterminada, PrevisualizadorTrayectoria

### Community 76 - "Renderable"
Cohesion: 0.25
Nodes (3): Hud, Renderable, Renderer

### Community 87 - "ResolutorColisiones.kt"
Cohesion: 0.36
Nodes (3): FabricaResolutor, FabricaResolutorPredeterminada, ResolutorFisico

## Knowledge Gaps
- **21 isolated node(s):** `CARTON`, `MADERA`, `LADRILLO`, `HORMIGON`, `ACERO` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 414 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **84 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Input and Coordinates` to `Ball Customization`, `Main Application Entry`, `Pared`, `Canvas Rendering Engine`, `Simulacion`, `AppTest`, `ContextoJuego`, `Editor Mode Manager`, `MenusJuego`, `Launch Input Handler`, `Proyectil`, `Simulation Scene States`, `Pelota`, `SimulacionTest`, `ComandosPartida`, `GestionSprites`, `ModoSimulacion`, `Physics Data Readout`, `.componentes`, `DibujanteParedes`, `Placement Mode Logic`, `Menu and Idle States`, `Simulation State Interface`, `Ambiente`, `Free Fall Tests`, `PresentadorConsola`, `Coordinate Transformation Tools`, `Drawing Context Wrapper`, `Physics Vector Visualization`, `Static Image Sprites`, `Free Fall Mode`, `Drag Input Logic`, `SesionJuego`, `Game Presenter Logic`, `Projectile Launch State`, `Transformaciones`, `DibujanteObjetos`, `FormaCaja`, `FormaCapsula`, `Parabolic Motion Mode`, `MenusJuego.kt`, `DetectorReposo`, `FormaPoligono`, `Sprite Lifecycle Manager`, `Mundo`, `ComponentesSimulacion`, `Unit Testing Framework`, `Sprite Creation Factory`, `DibujanteFlechaDireccion`, `.componentes`, `HerramientaParedes`, `ResolutorColisiones`, `DibujanteEntorno`, `FabricaMotorFisico`, `Obstaculo`, `EditorEscenario`, `MecanicaTiro`, `.crear`, `.estado`, `Mecanica`, `ResolutorColisiones.kt`, `EnSuelo`, `UbicadorPelota`, `CalculadoraTrayectoria`, `TransformadorCoordenadasSimulacion`, `.previsualizarTrayectoria`, `DbgTest.kt`, `Diag.kt`?**
  _High betweenness centrality (0.552) - this node is a cross-community bridge._
- **What connects `CARTON`, `MADERA`, `LADRILLO` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Ball Customization` be split into smaller, more focused modules?**
  _Cohesion score 0.051228070175438595 - nodes in this community are weakly interconnected._
- **Why does `ContextoJuego` connect `ContextoJuego` to `Ball Customization`, `TipoPared`, `MetricasTiro`, `Pared`, `Simulacion`, `EstadoPanel`, `Launch Input Handler`, `Simulation Scene States`, `Pelota`, `ModoSimulacion`, `Physics Data Readout`, `GestorMenus`, `Menu and Idle States`, `Simulation State Interface`, `PresentadorConsola`, `Wall Destruction Effects`, `Game Presenter Logic`, `Projectile Launch State`, `EnSuelo`, `EnVuelo`?**
  _High betweenness centrality (0.146) - this node is a cross-community bridge._
- **Should `TipoPared` be split into smaller, more focused modules?**
  _Cohesion score 0.09956709956709957 - nodes in this community are weakly interconnected._
- **Why does `Simulacion` connect `Simulacion` to `Ball Customization`, `TipoPared`, `Main Application Entry`, `MetricasTiro`, `Pared`, `Input and Coordinates`, `ContextoJuego`, `Launch Input Handler`, `PhysicsEngine`, `SimulacionTest`, `.componentes`, `Free Fall Tests`, `Wall Destruction Effects`, `SesionJuego`, `Game Presenter Logic`, `ComponentesSimulacion`, `Unit Testing Framework`, `.componentes`, `Metricas`, `EditorEscenario`, `CreadorParedes`, `ComandosEditor`, `.estado`, `Mecanica`, `UbicadorPelota`, `Gravedades`, `ComandosJuego`, `FabricaModos`, `SuperficiesApoyo`, `.reiniciar`, `.pelota`, `.previsualizarTrayectoria`, `DbgTest.kt`, `Diag.kt`?**
  _High betweenness centrality (0.122) - this node is a cross-community bridge._
- **Should `Main Application Entry` be split into smaller, more focused modules?**
  _Cohesion score 0.08695652173913043 - nodes in this community are weakly interconnected._