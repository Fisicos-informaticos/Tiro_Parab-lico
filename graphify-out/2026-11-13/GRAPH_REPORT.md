# Graph Report - Tiro_Parab-lico  (2026-11-13)

## Corpus Check
- 135 files · ~27,534 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: (none) 2, .properties 2, .toml 1)

## Summary
- 1623 nodes · 3561 edges · 112 communities (26 shown, 86 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 61 edges (avg confidence: 0.87)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `11780513`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- TipoPelota
- TipoPared
- MainApp
- ResultadoSimulacion
- Pared
- CanvasRenderer
- Simulacion
- Input and Coordinates
- Pelota
- EstadoPanel
- MotorFisico
- ContextoJuego
- Editor Mode Manager
- SesionPartida
- InputHandler
- Brizna
- PhysicsEngine
- Proyectil
- Simulation Scene States
- UIEventListener
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
- FabricaModos
- Free Fall Tests
- PresentadorConsola
- TransformadorCoordenadas
- ContextoDibujo
- Physics Vector Visualization
- Wall Destruction Effects
- DecoracionesPelota.kt
- FormaColision
- Free Fall Mode
- Drag Input Logic
- SesionJuego
- Game Presenter Logic
- Projectile Launch State
- DibujoPelota.kt
- DibujanteObjetos
- FormaCaja
- FormaCapsula
- VistaPelota
- Pelota
- ResolutorColisiones.kt
- ConstructorPanel.kt
- DetectorReposo
- ConstructorMenu.kt
- DatosContactoPlano
- CatalogoModos
- GeneradorEntorno
- Puertos.kt
- .decorar
- MenuPelotas
- FabricaSimulacion
- HerramientaParedes
- Documentation Generation
- ResolutorColisiones
- UI Style Definitions
- Project Name
- AppTest.kt
- MenusJuego
- Obstaculo
- Renderable
- Metricas
- EditorEscenario
- EscenaJuego.kt
- HerramientaCreacionParedes
- MenuFin
- ComponentesSimulacion
- ComandosEditor
- SuperficiesLanzamiento
- Mecanica
- SpriteEstatico
- MenuFinJuego.kt
- TipoColision
- EnVuelo
- Gravedades
- PanelSimulacion
- PintorInformacion
- Mundo
- SpriteManager
- PanelPelotas
- SeccionControl.kt
- Ambiente
- DbgTest.kt
- Diag.kt
- EnSuelo
- TransformadorCoordenadasSimulacion
- FabricaMotorFisico
- Architecture
- PresentadorConsola
- AnimationTimer
- CatalogoModos
- FabricaSpriteEstatica

## God Nodes (most connected - your core abstractions)
1. `Vector2D` - 350 edges
2. `Pared` - 120 edges
3. `ContextoJuego` - 115 edges
4. `Simulacion` - 104 edges
5. `TipoPelota` - 84 edges
6. `Proyectil` - 74 edges
7. `TipoPared` - 51 edges
8. `MainApp` - 44 edges
9. `LecturaModo` - 41 edges
10. `FormaColision` - 40 edges

## Surprising Connections (you probably didn't know these)
- `Architecture` --references--> `MainApp`  [INFERRED]
  .github/copilot-instructions.md → app/src/main/kotlin/org/example/MainApp.kt
- `Codebase conventions` --references--> `MainApp`  [INFERRED]
  .github/copilot-instructions.md → app/src/main/kotlin/org/example/MainApp.kt
- `Architecture` --references--> `PhysicsEngine`  [INFERRED]
  .github/copilot-instructions.md → app/src/main/kotlin/org/example/fisica/PhysicsEngine.kt
- `Architecture` --references--> `ContextoJuego`  [INFERRED]
  .github/copilot-instructions.md → app/src/main/kotlin/org/example/simulacion/ContextoJuego.kt
- `Architecture` --references--> `FabricaSimulacion`  [INFERRED]
  .github/copilot-instructions.md → app/src/main/kotlin/org/example/simulacion/FabricaSimulacion.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Graphify Tooling Suite** — gemini_graphify, gemini_graphify_out_graph_json, gemini_graphify_out_wiki_index_md, gemini_graphify_out_graph_report_md [EXTRACTED 1.00]

## Communities (112 total, 86 thin omitted)

### Community 0 - "TipoPelota"
Cohesion: 0.08
Nodes (9): TipoPelota, BEISBOL, BLOQUE, BOLICHE, FUTBOL, GOMA, POLIEDRO, RUGBY (+1 more)

### Community 1 - "TipoPared"
Cohesion: 0.07
Nodes (7): CreadorParedes, TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA

### Community 3 - "ResultadoSimulacion"
Cohesion: 0.07
Nodes (5): ObservadorFinSimulacion, ObservadoresFin, RegistroObservadoresFin, ResultadoSimulacion, MenuFinJuego

### Community 5 - "CanvasRenderer"
Cohesion: 0.05
Nodes (7): CanvasRenderer, DibujanteEntorno, DibujanteFlechaDireccion, DibujantePanelInfo, DibujantePantallaInicio, FabricaRenderer, Renderer

### Community 7 - "Input and Coordinates"
Cohesion: 0.11
Nodes (3): InputListener, DibujoVectoresFisica, Vector2D

### Community 13 - "SesionPartida"
Cohesion: 0.09
Nodes (3): FabricaProyectiles, FabricaProyectilesPredeterminada, SesionPartida

### Community 14 - "InputHandler"
Cohesion: 0.09
Nodes (4): InputHandler, CaidaLibre, CondicionPrevia, TiroParabolico

### Community 15 - "Brizna"
Cohesion: 0.07
Nodes (9): Arbol, Brizna, Meta, Nube, GeneradorArboles, GeneradorMetas, GeneradorNubes, GeneradorPasto (+1 more)

### Community 25 - "GestorMenus"
Cohesion: 0.05
Nodes (4): CoordinadorMenus, DetectorFinDeVuelo, GestorMenus, MenuJuego

### Community 26 - ".componentes"
Cohesion: 0.13
Nodes (3): Camara, CamaraSimulacion, ControladorCamara

### Community 27 - "DibujanteParedes"
Cohesion: 0.10
Nodes (4): DibujanteParedes, EstiloDibujoPared, GeometriaGrietas, MetricasTiro

### Community 36 - "Physics Vector Visualization"
Cohesion: 0.37
Nodes (3): GeometriaFlecha, TrazoVector, VectorRenderer

### Community 37 - "Wall Destruction Effects"
Cohesion: 0.11
Nodes (3): EfectoRotura, Efectos, RegistroEfectos

### Community 38 - "DecoracionesPelota.kt"
Cohesion: 0.22
Nodes (8): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis

### Community 43 - "Game Presenter Logic"
Cohesion: 0.12
Nodes (4): Hud, PintorMundo, PresentadorJuego, Renderable

### Community 51 - "ResolutorColisiones.kt"
Cohesion: 0.36
Nodes (3): FabricaResolutor, FabricaResolutorPredeterminada, ResolutorFisico

### Community 53 - "DetectorReposo"
Cohesion: 0.20
Nodes (3): DetectorDeReposo, FabricaDetectoresReposo, DetectorReposo

### Community 54 - "ConstructorMenu.kt"
Cohesion: 0.11
Nodes (6): ConstructorMenu, EspecificacionBotonMenu, EspecificacionMenu, EspecificacionTextos, MenuParedes, MenuPausa

### Community 58 - "Puertos.kt"
Cohesion: 0.39
Nodes (4): IntegradorVuelo, PasoIntegracion, ContextoVuelo, Integrador

### Community 62 - "FabricaSimulacion"
Cohesion: 0.22
Nodes (8): ComandosCableados, ContextoComandos, ContextoComponentes, ContextoServicios, FabricaSimulacion, FisicaCableada, PartidaCableada, ServiciosCableados

### Community 65 - "Documentation Generation"
Cohesion: 0.50
Nodes (4): graphify, graphify-out/graph.json, graphify-out/GRAPH_REPORT.md, graphify-out/wiki/index.md

### Community 79 - "EscenaJuego.kt"
Cohesion: 0.15
Nodes (3): EscenaJuego, ConfiguracionPanelSimulacion, PanelControl

### Community 89 - "TipoColision"
Cohesion: 0.20
Nodes (5): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO

### Community 100 - "Ambiente"
Cohesion: 0.14
Nodes (3): Ambiente, FabricaAmbiente, FabricaTierra

### Community 106 - "FabricaMotorFisico"
Cohesion: 0.20
Nodes (4): FabricaMotorFisico, FabricaMotorFisicoPredeterminada, PrevisualizadorTrayectoria, Previsualizador

### Community 111 - "Architecture"
Cohesion: 0.25
Nodes (5): LanzadorJuego, Architecture, Build, test, and run, Codebase conventions, Copilot instructions

## Knowledge Gaps
- **21 isolated node(s):** `CARTON`, `MADERA`, `LADRILLO`, `HORMIGON`, `ACERO` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 418 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **86 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Input and Coordinates` to `Pared`, `CanvasRenderer`, `Simulacion`, `Pelota`, `MotorFisico`, `ContextoJuego`, `Editor Mode Manager`, `SesionPartida`, `InputHandler`, `Brizna`, `Proyectil`, `Simulation Scene States`, `SimulacionTest`, `ComandosPartida`, `Sprite`, `LecturaModo`, `ModoTiroParabolico`, `.componentes`, `DibujanteParedes`, `Placement Mode Logic`, `Menu and Idle States`, `Simulation State Interface`, `Free Fall Tests`, `TransformadorCoordenadas`, `ContextoDibujo`, `Physics Vector Visualization`, `DecoracionesPelota.kt`, `FormaColision`, `Free Fall Mode`, `Drag Input Logic`, `SesionJuego`, `Projectile Launch State`, `DibujoPelota.kt`, `DibujanteObjetos`, `FormaCaja`, `FormaCapsula`, `VistaPelota`, `Pelota`, `ResolutorColisiones.kt`, `DetectorReposo`, `DatosContactoPlano`, `CatalogoModos`, `Puertos.kt`, `.decorar`, `MenuPelotas`, `FabricaSimulacion`, `HerramientaParedes`, `FormaColision.kt`, `ResolutorColisiones`, `AppTest.kt`, `Obstaculo`, `Renderable`, `EditorEscenario`, `EscenaJuego.kt`, `HerramientaCreacionParedes`, `ComandosEditor`, `.estado`, `Mecanica`, `SpriteEstatico`, `Mundo`, `SpriteManager`, `.pelota`, `Ambiente`, `DbgTest.kt`, `Diag.kt`, `EnSuelo`, `TransformadorCoordenadasSimulacion`, `FabricaMotorFisico`, `.start`, `PresentadorConsola`, `FabricaSpriteEstatica`?**
  _High betweenness centrality (0.506) - this node is a cross-community bridge._
- **What connects `CARTON`, `MADERA`, `LADRILLO` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TipoPelota` be split into smaller, more focused modules?**
  _Cohesion score 0.08333333333333333 - nodes in this community are weakly interconnected._
- **Why does `ContextoJuego` connect `ContextoJuego` to `TipoPelota`, `TipoPared`, `ResultadoSimulacion`, `Pared`, `Simulacion`, `EstadoPanel`, `InputHandler`, `Proyectil`, `Simulation Scene States`, `LecturaModo`, `GestorMenus`, `Menu and Idle States`, `Simulation State Interface`, `Wall Destruction Effects`, `Game Presenter Logic`, `Projectile Launch State`, `FormaColision.kt`, `Renderable`, `EnVuelo`, `PintorInformacion`, `EnSuelo`, `Architecture`, `PresentadorConsola`?**
  _High betweenness centrality (0.151) - this node is a cross-community bridge._
- **Should `TipoPared` be split into smaller, more focused modules?**
  _Cohesion score 0.07258064516129033 - nodes in this community are weakly interconnected._
- **Why does `Pared` connect `Pared` to `TipoPelota`, `TipoPared`, `CanvasRenderer`, `Input and Coordinates`, `Pelota`, `MotorFisico`, `Editor Mode Manager`, `PhysicsEngine`, `LecturaModo`, `DibujanteParedes`, `TransformadorCoordenadas`, `Wall Destruction Effects`, `SesionJuego`, `Pelota`, `ResolutorColisiones.kt`, `DetectorReposo`, `Puertos.kt`, `HerramientaParedes`, `ResolutorColisiones`, `AppTest.kt`, `Obstaculo`, `EditorEscenario`, `HerramientaCreacionParedes`, `ComandosEditor`, `SuperficiesLanzamiento`, `Mundo`, `FabricaMotorFisico`?**
  _High betweenness centrality (0.123) - this node is a cross-community bridge._
- **Should `ResultadoSimulacion` be split into smaller, more focused modules?**
  _Cohesion score 0.07357357357357357 - nodes in this community are weakly interconnected._