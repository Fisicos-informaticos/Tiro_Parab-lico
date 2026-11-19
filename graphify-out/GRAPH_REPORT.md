# Graph Report - Tiro_Parab-lico  (2026-11-13)

## Corpus Check
- 138 files · ~27,438 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: (none) 2, .properties 2, .toml 1)

## Summary
- 1611 nodes · 3531 edges · 119 communities (24 shown, 95 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 66 edges (avg confidence: 0.87)
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
- CoordinadorMenus
- Proyectil
- Simulation Scene States
- UIEventListener
- SimulacionTest
- ComandosPartida
- Sprite
- ModoSimulacion
- ModoTiroParabolico
- GestorMenus
- .componentes
- DibujanteParedes
- Placement Mode Logic
- Menu and Idle States
- Simulation State Interface
- MenuJuego
- Free Fall Tests
- PresentadorConsola
- TransformadorCoordenadas
- ContextoDibujo
- Physics Vector Visualization
- Wall Destruction Effects
- DecoracionesPelota.kt
- FormaColision
- LecturaModo
- Drag Input Logic
- SesionJuego
- Game Presenter Logic
- Projectile Launch State
- MenuParedes
- DibujanteObjetos
- FormaCaja
- FormaPoligono
- VistaPelota
- Pelota
- ComandosEdicion
- ConstructorPanel.kt
- DetectorReposo
- ConstructorMenu.kt
- ComponentesSimulacion.kt
- CatalogoModos
- GeneradorEntorno
- Puertos.kt
- ControladorEventosUI
- .decorar
- MenuPelotas
- FabricaSimulacion
- HerramientaParedes
- FormaColision.kt
- Documentation Generation
- ResolutorColisiones
- UI Style Definitions
- Project Name
- MenusJuego
- DibujantePantallaInicio
- Obstaculo
- Renderable
- Metricas
- EditorEscenario
- MainApp.kt
- HerramientaCreacionParedes
- MenuFin
- CalculadoraTrayectoria
- ComandosEditor
- SuperficiesLanzamiento
- Mecanica
- SpriteEstatico
- MenuFinJuego.kt
- FormaCapsula
- EnVuelo
- Gravedades
- PanelSimulacion
- PintorInformacion
- Mundo
- .crear
- PanelPelotas
- SeccionControl.kt
- Ambiente
- DbgTest.kt
- Diag.kt
- EnSuelo
- ContextoServicios
- TransformadorCoordenadasSimulacion
- FabricaMotorFisico
- DibujanteEntorno
- .decorar
- DibujanteFlechaDireccion
- MecanicaTiro
- MenuPausa
- ComandosJuego
- FabricaAmbiente
- CatalogoModos
- PanelControl

## God Nodes (most connected - your core abstractions)
1. `Vector2D` - 351 edges
2. `Pared` - 120 edges
3. `ContextoJuego` - 117 edges
4. `Simulacion` - 104 edges
5. `TipoPelota` - 84 edges
6. `Proyectil` - 74 edges
7. `TipoPared` - 51 edges
8. `LecturaModo` - 41 edges
9. `MainApp` - 40 edges
10. `InputHandler` - 40 edges

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

## Communities (119 total, 95 thin omitted)

### Community 0 - "TipoPelota"
Cohesion: 0.09
Nodes (9): TipoPelota, BEISBOL, BLOQUE, BOLICHE, FUTBOL, GOMA, POLIEDRO, RUGBY (+1 more)

### Community 1 - "TipoPared"
Cohesion: 0.07
Nodes (8): ConstructorParedes, CreadorParedes, TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA

### Community 2 - "MainApp"
Cohesion: 0.08
Nodes (9): BucleJuego, AnimationTimer, LanzadorJuego, MainApp, ConfiguracionPanelSimulacion, Architecture, Build, test, and run, Codebase conventions (+1 more)

### Community 3 - "ResultadoSimulacion"
Cohesion: 0.05
Nodes (6): MetricasTiro, ObservadorFinSimulacion, ObservadoresFin, RegistroObservadoresFin, ResultadoSimulacion, MenuFinJuego

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

### Community 22 - "Sprite"
Cohesion: 0.09
Nodes (5): FabricaSprite, FabricaSpriteEstatica, GestionSprites, Sprite, SpriteManager

### Community 26 - ".componentes"
Cohesion: 0.11
Nodes (4): Camara, CamaraSimulacion, ControladorCamara, ControlCamara

### Community 27 - "DibujanteParedes"
Cohesion: 0.11
Nodes (4): DibujanteParedes, EstiloDibujoPared, GeometriaGrietas, PresentadorConsola

### Community 35 - "ContextoDibujo"
Cohesion: 0.13
Nodes (3): ContextoDibujo, ImagenEnMundo, TextoDibujo

### Community 36 - "Physics Vector Visualization"
Cohesion: 0.37
Nodes (3): GeometriaFlecha, TrazoVector, VectorRenderer

### Community 37 - "Wall Destruction Effects"
Cohesion: 0.11
Nodes (3): EfectoRotura, Efectos, RegistroEfectos

### Community 38 - "DecoracionesPelota.kt"
Cohesion: 0.25
Nodes (8): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis

### Community 48 - "FormaPoligono"
Cohesion: 0.11
Nodes (6): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO, FormaPoligono

### Community 53 - "DetectorReposo"
Cohesion: 0.20
Nodes (3): DetectorDeReposo, FabricaDetectoresReposo, DetectorReposo

### Community 54 - "ConstructorMenu.kt"
Cohesion: 0.19
Nodes (4): ConstructorMenu, EspecificacionBotonMenu, EspecificacionMenu, EspecificacionTextos

### Community 55 - "ComponentesSimulacion.kt"
Cohesion: 0.32
Nodes (7): ComandosCableados, ComponentesControl, ComponentesFisica, ComponentesPartida, ComponentesSimulacion, ServiciosCableados, ContextoComandos

### Community 58 - "Puertos.kt"
Cohesion: 0.33
Nodes (4): IntegradorVuelo, PasoIntegracion, ContextoVuelo, Integrador

### Community 65 - "Documentation Generation"
Cohesion: 0.50
Nodes (4): graphify, graphify-out/graph.json, graphify-out/GRAPH_REPORT.md, graphify-out/wiki/index.md

### Community 76 - "Renderable"
Cohesion: 0.19
Nodes (3): Hud, PintorApuntado, Renderable

### Community 100 - "Ambiente"
Cohesion: 0.13
Nodes (4): Ambiente, FabricaResolutor, FabricaResolutorPredeterminada, ResolutorFisico

### Community 106 - "FabricaMotorFisico"
Cohesion: 0.24
Nodes (4): FabricaMotorFisico, FabricaMotorFisicoPredeterminada, PrevisualizadorTrayectoria, Previsualizador

## Knowledge Gaps
- **21 isolated node(s):** `CARTON`, `MADERA`, `LADRILLO`, `HORMIGON`, `ACERO` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 417 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **95 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Input and Coordinates` to `Pared`, `CanvasRenderer`, `Simulacion`, `Pelota`, `MotorFisico`, `ContextoJuego`, `Editor Mode Manager`, `SesionPartida`, `InputHandler`, `Brizna`, `Proyectil`, `Simulation Scene States`, `SimulacionTest`, `ComandosPartida`, `Sprite`, `ModoSimulacion`, `ModoTiroParabolico`, `.componentes`, `DibujanteParedes`, `Placement Mode Logic`, `Menu and Idle States`, `Simulation State Interface`, `Free Fall Tests`, `TransformadorCoordenadas`, `ContextoDibujo`, `Physics Vector Visualization`, `DecoracionesPelota.kt`, `FormaColision`, `LecturaModo`, `Drag Input Logic`, `SesionJuego`, `Projectile Launch State`, `DibujanteObjetos`, `FormaCaja`, `FormaPoligono`, `VistaPelota`, `Pelota`, `DetectorReposo`, `CatalogoModos`, `Puertos.kt`, `ControladorEventosUI`, `MenuPelotas`, `FabricaSimulacion`, `HerramientaParedes`, `FormaColision.kt`, `ResolutorColisiones`, `AppTest.kt`, `Obstaculo`, `Renderable`, `EditorEscenario`, `HerramientaCreacionParedes`, `CalculadoraTrayectoria`, `ComandosEditor`, `.estado`, `Mecanica`, `SpriteEstatico`, `FormaCapsula`, `Mundo`, `Ambiente`, `DbgTest.kt`, `Diag.kt`, `EnSuelo`, `ContextoServicios`, `TransformadorCoordenadasSimulacion`, `FabricaMotorFisico`, `DibujanteEntorno`, `.decorar`, `DibujanteFlechaDireccion`, `MecanicaTiro`, `FabricaAmbiente`, `.previsualizarTrayectoria`?**
  _High betweenness centrality (0.522) - this node is a cross-community bridge._
- **What connects `CARTON`, `MADERA`, `LADRILLO` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TipoPelota` be split into smaller, more focused modules?**
  _Cohesion score 0.08695652173913043 - nodes in this community are weakly interconnected._
- **Why does `ContextoJuego` connect `ContextoJuego` to `TipoPelota`, `TipoPared`, `MainApp`, `ResultadoSimulacion`, `Pared`, `Simulacion`, `EstadoPanel`, `InputHandler`, `Proyectil`, `Simulation Scene States`, `ModoSimulacion`, `GestorMenus`, `DibujanteParedes`, `Menu and Idle States`, `Simulation State Interface`, `MenuJuego`, `Wall Destruction Effects`, `LecturaModo`, `Game Presenter Logic`, `Projectile Launch State`, `ControladorEventosUI`, `Renderable`, `EnVuelo`, `PintorInformacion`, `EnSuelo`?**
  _High betweenness centrality (0.176) - this node is a cross-community bridge._
- **Should `TipoPared` be split into smaller, more focused modules?**
  _Cohesion score 0.06984126984126984 - nodes in this community are weakly interconnected._
- **Why does `TipoPelota` connect `TipoPelota` to `TipoPared`, `MainApp`, `Input and Coordinates`, `SesionPartida`, `Proyectil`, `UIEventListener`, `ComandosPartida`, `ModoSimulacion`, `Placement Mode Logic`, `PresentadorConsola`, `DecoracionesPelota.kt`, `FormaColision`, `SesionJuego`, `VistaPelota`, `Pelota`, `CatalogoModos`, `Puertos.kt`, `.decorar`, `MenuPelotas`, `FabricaSimulacion`, `AppTest.kt`, `MenusJuego`, `Obstaculo`, `MainApp.kt`, `Mecanica`, `FormaCapsula`, `FabricaMotorFisico`, `.decorar`, `MecanicaTiro`, `ComandosJuego`, `.previsualizarTrayectoria`?**
  _High betweenness centrality (0.096) - this node is a cross-community bridge._
- **Should `MainApp` be split into smaller, more focused modules?**
  _Cohesion score 0.07575757575757576 - nodes in this community are weakly interconnected._