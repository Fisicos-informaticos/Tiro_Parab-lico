# Graph Report - Tiro_Parab-lico  (2026-11-13)

## Corpus Check
- 133 files · ~27,298 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: (none) 2, .properties 2, .toml 1)

## Summary
- 1590 nodes · 3481 edges · 102 communities (26 shown, 76 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 59 edges (avg confidence: 0.87)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `6baa8b97`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- TipoPelota
- TipoPared
- Main Application Entry
- MetricasTiro
- Pared
- Canvas Rendering Engine
- Simulacion
- Input and Coordinates
- Pelota
- EstadoPanel
- MotorFisico
- ContextoJuego
- Editor Mode Manager
- MenusJuego
- InputHandler
- Brizna
- PhysicsEngine
- Proyectil
- Simulation Scene States
- ConstructorMenu.kt
- SimulacionTest
- ComandosPartida
- Sprite
- LecturaModo
- ModoTiroParabolico
- GestorMenus
- .componentes
- DibujanteParedes
- Placement Mode Logic
- Menu and Idle States
- Simulation State Interface
- Ambiente
- Free Fall Tests
- PresentadorConsola
- TransformadorCoordenadas
- LineaDibujo
- Physics Vector Visualization
- Wall Destruction Effects
- DecoracionesPelota.kt
- FormaColision
- Free Fall Mode
- Drag Input Logic
- SesionJuego
- Game Presenter Logic
- Projectile Launch State
- MotorFisico.kt
- DibujanteObjetos
- FormaCaja
- FormaCapsula
- VistaPelota
- DibujantePantallaInicio
- ComandosEdicion
- ConstructorPanel.kt
- DetectorReposo
- FormaPoligono
- DibujoPelota.kt
- CatalogoModos
- Mundo
- Puertos.kt
- Unit Testing Framework
- .decorar
- DibujanteFlechaDireccion
- FabricaSimulacion
- HerramientaParedes
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
- MenuFin
- .crear
- ComandosEditor
- SuperficiesLanzamiento
- Mecanica
- HerramientaCreacionParedes
- MenuFinJuego.kt
- MenuPausa
- EnVuelo
- Gravedades
- PanelSimulacion
- PintorInformacion
- ComponentesSimulacion
- SuperficiesApoyo
- PanelPelotas
- DbgTest.kt
- Diag.kt

## God Nodes (most connected - your core abstractions)
1. `Vector2D` - 345 edges
2. `Pared` - 120 edges
3. `ContextoJuego` - 115 edges
4. `Simulacion` - 104 edges
5. `TipoPelota` - 79 edges
6. `Proyectil` - 71 edges
7. `TipoPared` - 51 edges
8. `ComponentesSimulacion` - 45 edges
9. `MainApp` - 44 edges
10. `LecturaModo` - 41 edges

## Surprising Connections (you probably didn't know these)
- `Architecture` --references--> `PhysicsEngine`  [INFERRED]
  .github/copilot-instructions.md → app/src/main/kotlin/org/example/fisica/PhysicsEngine.kt
- `Architecture` --references--> `ContextoJuego`  [INFERRED]
  .github/copilot-instructions.md → app/src/main/kotlin/org/example/simulacion/ContextoJuego.kt
- `Architecture` --references--> `FabricaSimulacion`  [INFERRED]
  .github/copilot-instructions.md → app/src/main/kotlin/org/example/simulacion/FabricaSimulacion.kt
- `Codebase conventions` --references--> `FabricaSimulacion`  [INFERRED]
  .github/copilot-instructions.md → app/src/main/kotlin/org/example/simulacion/FabricaSimulacion.kt
- `Codebase conventions` --references--> `Simulacion`  [INFERRED]
  .github/copilot-instructions.md → app/src/main/kotlin/org/example/simulacion/Simulacion.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Graphify Tooling Suite** — gemini_graphify, gemini_graphify_out_graph_json, gemini_graphify_out_wiki_index_md, gemini_graphify_out_graph_report_md [EXTRACTED 1.00]

## Communities (102 total, 76 thin omitted)

### Community 0 - "TipoPelota"
Cohesion: 0.08
Nodes (9): TipoPelota, BEISBOL, BLOQUE, BOLICHE, FUTBOL, GOMA, POLIEDRO, RUGBY (+1 more)

### Community 1 - "TipoPared"
Cohesion: 0.09
Nodes (6): TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA

### Community 2 - "Main Application Entry"
Cohesion: 0.05
Nodes (10): BucleJuego, AnimationTimer, LanzadorJuego, MainApp, ConfiguracionPanelSimulacion, UIEventListener, Architecture, Build, test, and run (+2 more)

### Community 3 - "MetricasTiro"
Cohesion: 0.05
Nodes (6): MetricasTiro, ObservadorFinSimulacion, ObservadoresFin, RegistroObservadoresFin, ResultadoSimulacion, MenuFinJuego

### Community 7 - "Input and Coordinates"
Cohesion: 0.09
Nodes (4): InputListener, DibujoVectoresFisica, Penetracion, Vector2D

### Community 8 - "Pelota"
Cohesion: 0.06
Nodes (5): CalculadoraTrayectoria, Tierra, GeneradorEntorno, Pelota, AppTest

### Community 14 - "InputHandler"
Cohesion: 0.09
Nodes (3): InputHandler, CondicionPrevia, ModoArrastrable

### Community 15 - "Brizna"
Cohesion: 0.07
Nodes (9): Arbol, Brizna, Meta, Nube, GeneradorArboles, GeneradorMetas, GeneradorNubes, GeneradorPasto (+1 more)

### Community 17 - "Proyectil"
Cohesion: 0.07
Nodes (3): ResolutorColisiones, Proyectil, SesionPartida

### Community 19 - "ConstructorMenu.kt"
Cohesion: 0.10
Nodes (3): ConstructorMenu, MenuParedes, MenuPelotas

### Community 22 - "Sprite"
Cohesion: 0.06
Nodes (7): ContextoDibujo, FabricaSprite, FabricaSpriteEstatica, GestionSprites, Sprite, SpriteEstatico, SpriteManager

### Community 25 - "GestorMenus"
Cohesion: 0.05
Nodes (4): CoordinadorMenus, DetectorFinDeVuelo, GestorMenus, MenuJuego

### Community 26 - ".componentes"
Cohesion: 0.12
Nodes (3): Camara, CamaraSimulacion, PosicionPelota

### Community 31 - "Ambiente"
Cohesion: 0.13
Nodes (4): Ambiente, FabricaResolutor, FabricaResolutorPredeterminada, ResolutorFisico

### Community 33 - "PresentadorConsola"
Cohesion: 0.13
Nodes (3): App, EntradaConsola, PresentadorConsola

### Community 36 - "Physics Vector Visualization"
Cohesion: 0.37
Nodes (3): GeometriaFlecha, TrazoVector, VectorRenderer

### Community 38 - "DecoracionesPelota.kt"
Cohesion: 0.22
Nodes (8): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis

### Community 46 - "DibujanteObjetos"
Cohesion: 0.15
Nodes (3): ImagenEnMundo, TextoDibujo, DibujanteObjetos

### Community 48 - "FormaCapsula"
Cohesion: 0.09
Nodes (7): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO, FormaCapsula, FormaCirculo

### Community 53 - "DetectorReposo"
Cohesion: 0.20
Nodes (3): DetectorDeReposo, FabricaDetectoresReposo, DetectorReposo

### Community 58 - "Puertos.kt"
Cohesion: 0.20
Nodes (3): IntegradorVuelo, Efectos, Integrador

### Community 62 - "FabricaSimulacion"
Cohesion: 0.44
Nodes (5): ComandosCableados, FabricaSimulacion, FisicaCableada, PartidaCableada, ServiciosCableados

### Community 65 - "Documentation Generation"
Cohesion: 0.50
Nodes (4): graphify, graphify-out/graph.json, graphify-out/GRAPH_REPORT.md, graphify-out/wiki/index.md

### Community 74 - "FabricaMotorFisico"
Cohesion: 0.28
Nodes (3): FabricaMotorFisico, FabricaMotorFisicoPredeterminada, PrevisualizadorTrayectoria

### Community 76 - "Renderable"
Cohesion: 0.19
Nodes (3): Hud, PintorApuntado, Renderable

### Community 94 - "ComponentesSimulacion"
Cohesion: 0.14
Nodes (6): FabricaAmbiente, FabricaTierra, FabricaModos, ComponentesSimulacion, ControlCamara, Previsualizador

## Knowledge Gaps
- **21 isolated node(s):** `CARTON`, `MADERA`, `LADRILLO`, `HORMIGON`, `ACERO` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 417 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **76 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Input and Coordinates` to `Main Application Entry`, `Pared`, `Canvas Rendering Engine`, `Simulacion`, `Pelota`, `MotorFisico`, `ContextoJuego`, `Editor Mode Manager`, `InputHandler`, `Brizna`, `Proyectil`, `Simulation Scene States`, `ConstructorMenu.kt`, `SimulacionTest`, `ComandosPartida`, `Sprite`, `LecturaModo`, `ModoTiroParabolico`, `.componentes`, `DibujanteParedes`, `Placement Mode Logic`, `Menu and Idle States`, `Simulation State Interface`, `Ambiente`, `Free Fall Tests`, `PresentadorConsola`, `TransformadorCoordenadas`, `LineaDibujo`, `Physics Vector Visualization`, `DecoracionesPelota.kt`, `FormaColision`, `Free Fall Mode`, `Drag Input Logic`, `SesionJuego`, `Projectile Launch State`, `MotorFisico.kt`, `DibujanteObjetos`, `FormaCaja`, `FormaCapsula`, `VistaPelota`, `DetectorReposo`, `FormaPoligono`, `DibujoPelota.kt`, `Mundo`, `Puertos.kt`, `Unit Testing Framework`, `.decorar`, `DibujanteFlechaDireccion`, `FabricaSimulacion`, `HerramientaParedes`, `FormaColision.kt`, `DibujanteEntorno`, `FabricaMotorFisico`, `Obstaculo`, `Renderable`, `EditorEscenario`, `.crear`, `ComandosEditor`, `SuperficiesLanzamiento`, `.estado`, `Mecanica`, `HerramientaCreacionParedes`, `ComponentesSimulacion`, `.previsualizarTrayectoria`, `DbgTest.kt`, `Diag.kt`?**
  _High betweenness centrality (0.522) - this node is a cross-community bridge._
- **What connects `CARTON`, `MADERA`, `LADRILLO` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TipoPelota` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Why does `ContextoJuego` connect `ContextoJuego` to `TipoPelota`, `TipoPared`, `Main Application Entry`, `MetricasTiro`, `Pared`, `Simulacion`, `EstadoPanel`, `InputHandler`, `Proyectil`, `Simulation Scene States`, `LecturaModo`, `GestorMenus`, `Menu and Idle States`, `Simulation State Interface`, `PresentadorConsola`, `Wall Destruction Effects`, `Game Presenter Logic`, `Projectile Launch State`, `FormaColision.kt`, `Renderable`, `EnVuelo`, `PintorInformacion`?**
  _High betweenness centrality (0.180) - this node is a cross-community bridge._
- **Should `TipoPared` be split into smaller, more focused modules?**
  _Cohesion score 0.09486166007905138 - nodes in this community are weakly interconnected._
- **Why does `Pared` connect `Pared` to `TipoPelota`, `TipoPared`, `Canvas Rendering Engine`, `Input and Coordinates`, `Pelota`, `MotorFisico`, `Editor Mode Manager`, `PhysicsEngine`, `Proyectil`, `LecturaModo`, `DibujanteParedes`, `Ambiente`, `TransformadorCoordenadas`, `Wall Destruction Effects`, `MotorFisico.kt`, `ComandosEdicion`, `DetectorReposo`, `Mundo`, `Puertos.kt`, `Unit Testing Framework`, `HerramientaParedes`, `FabricaMotorFisico`, `Obstaculo`, `EditorEscenario`, `ConstructorParedes`, `CreadorParedes`, `.crear`, `ComandosEditor`, `SuperficiesLanzamiento`, `HerramientaCreacionParedes`, `ComponentesSimulacion`, `SuperficiesApoyo`?**
  _High betweenness centrality (0.117) - this node is a cross-community bridge._
- **Should `Main Application Entry` be split into smaller, more focused modules?**
  _Cohesion score 0.053821800090456805 - nodes in this community are weakly interconnected._