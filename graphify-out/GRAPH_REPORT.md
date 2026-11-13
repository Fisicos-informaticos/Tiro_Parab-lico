# Graph Report - Tiro_Parab-lico  (2026-11-13)

## Corpus Check
- 133 files · ~27,286 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: (none) 2, .properties 2, .toml 1)

## Summary
- 1589 nodes · 3475 edges · 107 communities (23 shown, 84 thin omitted)
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
- ObservadorFinSimulacion
- Pared
- Canvas Rendering Engine
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
- ConstructorMenu.kt
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
- Ambiente
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
- MotorFisico.kt
- DibujanteObjetos
- FormaCaja
- FormaCapsula
- VistaPelota
- DibujantePantallaInicio
- ComandosEdicion
- ConstructorPanel.kt
- DetectorReposo
- MetricasTiro
- DibujoPelota.kt
- CatalogoModos
- Mundo
- Puertos.kt
- ResultadoSimulacion
- .decorar
- DibujanteFlechaDireccion
- FabricaSimulacion
- HerramientaParedes
- Documentation Generation
- ResolutorColisiones
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
- MenuFinJuego
- .crear
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
- ComponentesSimulacion
- SpriteManager
- PanelPelotas
- SeccionControl.kt
- ResolutorColisiones.kt
- DbgTest.kt
- Diag.kt
- EnSuelo
- MecanicaTiro
- FabricaSpriteEstatica
- DibujoVectoresFisica

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

## Communities (107 total, 84 thin omitted)

### Community 0 - "TipoPelota"
Cohesion: 0.08
Nodes (9): TipoPelota, BEISBOL, BLOQUE, BOLICHE, FUTBOL, GOMA, POLIEDRO, RUGBY (+1 more)

### Community 1 - "TipoPared"
Cohesion: 0.09
Nodes (6): TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA

### Community 2 - "Main Application Entry"
Cohesion: 0.05
Nodes (12): BucleJuego, AnimationTimer, EscenaJuego, LanzadorJuego, MainApp, ConfiguracionPanelSimulacion, MenusJuego, PanelControl (+4 more)

### Community 3 - "ObservadorFinSimulacion"
Cohesion: 0.15
Nodes (3): ObservadorFinSimulacion, ObservadoresFin, RegistroObservadoresFin

### Community 7 - "Input and Coordinates"
Cohesion: 0.09
Nodes (4): InputListener, Penetracion, Vector2D, CondicionPrevia

### Community 8 - "Pelota"
Cohesion: 0.06
Nodes (5): CalculadoraTrayectoria, Tierra, GeneradorEntorno, Pelota, AppTest

### Community 14 - "InputHandler"
Cohesion: 0.11
Nodes (3): InputHandler, CaidaLibre, TiroParabolico

### Community 15 - "Brizna"
Cohesion: 0.07
Nodes (9): Arbol, Brizna, Meta, Nube, GeneradorArboles, GeneradorMetas, GeneradorNubes, GeneradorPasto (+1 more)

### Community 19 - "ConstructorMenu.kt"
Cohesion: 0.05
Nodes (6): ConstructorMenu, MenuFin, MenuParedes, MenuPausa, MenuPelotas, UIEventListener

### Community 25 - "GestorMenus"
Cohesion: 0.05
Nodes (4): CoordinadorMenus, DetectorFinDeVuelo, GestorMenus, MenuJuego

### Community 26 - ".componentes"
Cohesion: 0.12
Nodes (3): Camara, CamaraSimulacion, PosicionPelota

### Community 31 - "Ambiente"
Cohesion: 0.11
Nodes (4): Ambiente, FabricaAmbiente, FabricaTierra, FabricaModos

### Community 33 - "PresentadorConsola"
Cohesion: 0.13
Nodes (3): App, EntradaConsola, PresentadorConsola

### Community 36 - "Physics Vector Visualization"
Cohesion: 0.37
Nodes (3): GeometriaFlecha, TrazoVector, VectorRenderer

### Community 38 - "DecoracionesPelota.kt"
Cohesion: 0.22
Nodes (8): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis

### Community 43 - "Game Presenter Logic"
Cohesion: 0.12
Nodes (4): Hud, PintorMundo, PresentadorJuego, Renderable

### Community 48 - "FormaCapsula"
Cohesion: 0.09
Nodes (7): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO, FormaCirculo, FormaPoligono

### Community 53 - "DetectorReposo"
Cohesion: 0.20
Nodes (3): DetectorDeReposo, FabricaDetectoresReposo, DetectorReposo

### Community 62 - "FabricaSimulacion"
Cohesion: 0.44
Nodes (5): ComandosCableados, FabricaSimulacion, FisicaCableada, PartidaCableada, ServiciosCableados

### Community 65 - "Documentation Generation"
Cohesion: 0.50
Nodes (4): graphify, graphify-out/graph.json, graphify-out/GRAPH_REPORT.md, graphify-out/wiki/index.md

### Community 74 - "FabricaMotorFisico"
Cohesion: 0.28
Nodes (3): FabricaMotorFisico, FabricaMotorFisicoPredeterminada, PrevisualizadorTrayectoria

### Community 94 - "ComponentesSimulacion"
Cohesion: 0.11
Nodes (6): ComponentesSimulacion, ControlCamara, Integrador, Previsualizador, SuperficiesApoyo, UbicadorPelota

### Community 100 - "ResolutorColisiones.kt"
Cohesion: 0.36
Nodes (3): FabricaResolutor, FabricaResolutorPredeterminada, ResolutorFisico

## Knowledge Gaps
- **21 isolated node(s):** `CARTON`, `MADERA`, `LADRILLO`, `HORMIGON`, `ACERO` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 416 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **84 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Input and Coordinates` to `Main Application Entry`, `Pared`, `Canvas Rendering Engine`, `Simulacion`, `Pelota`, `MotorFisico`, `ContextoJuego`, `Editor Mode Manager`, `SesionPartida`, `InputHandler`, `Brizna`, `Proyectil`, `Simulation Scene States`, `ConstructorMenu.kt`, `SimulacionTest`, `ComandosPartida`, `Sprite`, `ModoSimulacion`, `ModoTiroParabolico`, `.componentes`, `DibujanteParedes`, `Placement Mode Logic`, `Menu and Idle States`, `Simulation State Interface`, `Ambiente`, `Free Fall Tests`, `PresentadorConsola`, `TransformadorCoordenadas`, `ContextoDibujo`, `Physics Vector Visualization`, `DecoracionesPelota.kt`, `FormaColision`, `Free Fall Mode`, `Drag Input Logic`, `SesionJuego`, `Projectile Launch State`, `MotorFisico.kt`, `DibujanteObjetos`, `FormaCaja`, `FormaCapsula`, `VistaPelota`, `DetectorReposo`, `DibujoPelota.kt`, `Mundo`, `Puertos.kt`, `.decorar`, `DibujanteFlechaDireccion`, `FabricaSimulacion`, `HerramientaParedes`, `FormaColision.kt`, `ResolutorColisiones`, `DibujanteEntorno`, `FabricaMotorFisico`, `Obstaculo`, `Renderable`, `EditorEscenario`, `.crear`, `ComandosEditor`, `SuperficiesLanzamiento`, `.estado`, `Mecanica`, `SpriteEstatico`, `FormaCapsula`, `ComponentesSimulacion`, `SpriteManager`, `.pelota`, `ResolutorColisiones.kt`, `DbgTest.kt`, `Diag.kt`, `EnSuelo`, `MecanicaTiro`, `FabricaSpriteEstatica`, `DibujoVectoresFisica`?**
  _High betweenness centrality (0.525) - this node is a cross-community bridge._
- **What connects `CARTON`, `MADERA`, `LADRILLO` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TipoPelota` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._
- **Why does `ContextoJuego` connect `ContextoJuego` to `TipoPelota`, `TipoPared`, `Main Application Entry`, `ObservadorFinSimulacion`, `Pared`, `Simulacion`, `EstadoPanel`, `InputHandler`, `Proyectil`, `Simulation Scene States`, `ModoSimulacion`, `ModoTiroParabolico`, `GestorMenus`, `Menu and Idle States`, `Simulation State Interface`, `PresentadorConsola`, `Wall Destruction Effects`, `Game Presenter Logic`, `Projectile Launch State`, `ResultadoSimulacion`, `FormaColision.kt`, `Renderable`, `EnVuelo`, `PintorInformacion`, `EnSuelo`?**
  _High betweenness centrality (0.180) - this node is a cross-community bridge._
- **Should `TipoPared` be split into smaller, more focused modules?**
  _Cohesion score 0.09486166007905138 - nodes in this community are weakly interconnected._
- **Why does `Pared` connect `Pared` to `TipoPelota`, `TipoPared`, `Canvas Rendering Engine`, `Input and Coordinates`, `Pelota`, `MotorFisico`, `Editor Mode Manager`, `PhysicsEngine`, `ModoSimulacion`, `DibujanteParedes`, `TransformadorCoordenadas`, `Wall Destruction Effects`, `Drag Input Logic`, `MotorFisico.kt`, `ComandosEdicion`, `DetectorReposo`, `Mundo`, `Puertos.kt`, `HerramientaParedes`, `ResolutorColisiones`, `FabricaMotorFisico`, `Obstaculo`, `EditorEscenario`, `ConstructorParedes`, `CreadorParedes`, `.crear`, `ComandosEditor`, `SuperficiesLanzamiento`, `ComponentesSimulacion`, `ResolutorColisiones.kt`?**
  _High betweenness centrality (0.117) - this node is a cross-community bridge._
- **Should `Main Application Entry` be split into smaller, more focused modules?**
  _Cohesion score 0.05220288781932617 - nodes in this community are weakly interconnected._