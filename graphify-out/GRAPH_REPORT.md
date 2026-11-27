# Graph Report - Tiro_Parab-lico  (2026-11-27)

## Corpus Check
- 153 files · ~33,089 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: (none) 2, .properties 2, .toml 1)

## Summary
- 2576 nodes · 5815 edges · 176 communities (52 shown, 124 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 60 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `455e3627`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- TipoPelota
- TipoPared
- MainApp
- DatosResultadoSimulacion
- Pared
- DatosCanvasRenderer
- DatosContextoComandos
- PresentadorConsola.kt
- DatosSimulacion
- EstadoPanel
- DatosMotorFisico
- ContextoJuego.kt
- GestorEdicion
- DatosSesionPartida
- Vector2D
- Brizna
- EstadoMenus
- ProyectilFisico
- .crear
- ServiciosCableados
- SimulacionTest
- DatosComandosPartida
- Sprite
- LecturaModo
- LecturaProyectilSesionJuego
- GestorMenus
- AccionesJuego
- DatosDibujanteParedes
- DatosMetricasTiro
- PantallaInicio
- Simulation State Interface
- MenuJuego
- Free Fall Tests
- TextoDibujo
- ObservadorFinSimulacion
- LecturaEscenarioJuego
- ContextoDibujo
- Proyectil
- FormaColision
- DatosContactoPlano
- DatosLecturaModo
- Drag Input Logic
- ContextoConsultas
- PresentadorJuego
- ContextoJuego
- FormaCaja
- SimulacionEdicion
- FormaCaja
- MenuParedes
- VistaPelota
- Tierra
- Preparacion
- DatosMenuFinJuego
- Ambiente
- ConstructorMenu.kt
- Efectos
- SimulacionPartidaTest.kt
- ConstructorPanel.kt
- DatosDetectorReposo
- ControlEventosUI
- Simulacion
- MenuPelotas.kt
- ContextoVuelo
- FlechaGeometria
- GeneradorEntorno
- Documentation Generation
- ImagenEnMundo
- FlechaGeometria
- Project Name
- DatosSimulacionConsultas
- ConstructorMenu
- PosicionPelota
- ConstructorMenu
- Mundo.kt
- EstadoSesionJuego
- MecanicaTiro
- MainAppSupport.kt
- InputHandler
- ComandosEdicion
- FormaColision.kt
- Puertos.kt
- .textos
- SimulacionEstadoTest
- Metricas
- SpriteEstatico
- DatosTipoPared
- EstadoSprite
- EnVuelo
- TransformadorCoordenadas
- CatalogoModos
- Renderable
- LineaDibujo
- CreadorParedes
- ResultadoSimulacion
- PanelControl
- ContextoComandos
- DibujanteFlechaDireccion
- CalculadoraTrayectoria
- DbgTest.kt
- Diag.kt
- PintorInformacion
- PresentadorConsola
- DatosTipoPelota
- FabricaComponentesUiPredeterminada
- MenuFin
- .celdas
- ModoSimulacion.kt
- MenuPausa
- MenusJuego
- .combo
- ArquitecturaAplicacion
- GeometriaColision
- CaidaLibreTest.kt
- ConfiguracionPanelSimulacion
- AnimationTimer
- SimulacionAcciones
- DibujanteObjetos
- EstadoProyectil
- ModoTiroParabolico
- TrazoEditorEscenario
- ModoSimulacion
- FabricaSpriteEstatica
- Brizna
- PanelSimulacion
- DatosVistaPelota
- DibujantePantallaInicio
- MedidasPanel
- Meta
- ModoCaidaLibre
- PanelPelotas
- MedidasMenu
- DatosPelota
- DibujanteEntorno
- EntradaJuego
- Camara
- PresentadorConsola
- EstilosUI.kt
- AccionesEscenarioJuego
- ConfiguracionJuego
- ParedesTest.kt
- LecturaConfiguracionJuego
- ProyectilControlable
- DatosContextoServicios
- AccionesSeleccionJuego
- SesionPartidaTest.kt
- LecturaFisicaJuego

## God Nodes (most connected - your core abstractions)
1. `Vector2D` - 458 edges
2. `Simulacion` - 157 edges
3. `Pared` - 155 edges
4. `TipoPelota` - 104 edges
5. `ContextoJuego` - 87 edges
6. `TipoPared` - 62 edges
7. `DatosSimulacion` - 59 edges
8. `LecturaModo` - 46 edges
9. `EstadoSimulacion` - 44 edges
10. `InputHandler` - 42 edges

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

## Communities (176 total, 124 thin omitted)

### Community 0 - "TipoPelota"
Cohesion: 0.08
Nodes (9): TipoPelota, BEISBOL, BLOQUE, BOLICHE, FUTBOL, GOMA, POLIEDRO, RUGBY (+1 more)

### Community 1 - "TipoPared"
Cohesion: 0.12
Nodes (6): TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA

### Community 2 - "MainApp"
Cohesion: 0.08
Nodes (5): MainApp, Architecture, Build, test, and run, Codebase conventions, Copilot instructions

### Community 4 - "Pared"
Cohesion: 0.06
Nodes (5): GestionParedesFisicas, MundoBasico, Pared, EfectoRotura, RegistroEfectos

### Community 5 - "DatosCanvasRenderer"
Cohesion: 0.08
Nodes (3): CanvasRenderer, DatosCanvasRenderer, Renderer

### Community 7 - "PresentadorConsola.kt"
Cohesion: 0.11
Nodes (4): ControlConsolaJuego, ControlVueloJuego, LecturaMetricasJuego, LecturaProyectilJuego

### Community 10 - "DatosMotorFisico"
Cohesion: 0.10
Nodes (3): DatosMotorFisico, MotorFisico, Transformaciones

### Community 11 - "ContextoJuego.kt"
Cohesion: 0.16
Nodes (6): ConstructorEstadoPanel, AccionesPartidaJuego, ControlEntradaJuego, LecturaEstadoJuego, LecturaPanelJuego, NavegacionSesionJuego

### Community 12 - "GestorEdicion"
Cohesion: 0.10
Nodes (5): ActivacionHerramientaParedes, EstadoHerramientaParedes, HerramientaParedes, TrazoHerramientaParedes, GestorEdicion

### Community 14 - "Vector2D"
Cohesion: 0.05
Nodes (7): ConsultaEntradaArrastre, ControlEntradaArrastre, InputListener, Obstaculo, Vector2D, CondicionPrevia, ApuntadoMecanica

### Community 16 - "EstadoMenus"
Cohesion: 0.12
Nodes (4): ControlCicloMenus, CoordinadorMenus, EstadoMenus, SeleccionMenus

### Community 17 - "ProyectilFisico"
Cohesion: 0.21
Nodes (3): DatosResolutorColisiones, Penetracion, ProyectilFisico

### Community 18 - ".crear"
Cohesion: 0.25
Nodes (3): DibujantesCanvas, DibujantePanelInfo, FabricaRenderer

### Community 19 - "ServiciosCableados"
Cohesion: 0.08
Nodes (4): DatosServiciosCableados, ServiciosCableados, ControlCamara, Previsualizador

### Community 22 - "Sprite"
Cohesion: 0.18
Nodes (3): GestionSprites, Sprite, SpriteManager

### Community 26 - "AccionesJuego"
Cohesion: 0.14
Nodes (3): PanelObstaculos, AccionesJuego, EdicionEscenario

### Community 27 - "DatosDibujanteParedes"
Cohesion: 0.08
Nodes (5): DatosDibujanteParedes, DibujanteParedes, EstiloDibujoPared, GeometriaGrietas, SuperficiesLanzamiento

### Community 34 - "ObservadorFinSimulacion"
Cohesion: 0.13
Nodes (3): ObservadorFinSimulacion, ObservadoresFin, RegistroObservadoresFin

### Community 37 - "Proyectil"
Cohesion: 0.21
Nodes (3): FabricaProyectiles, FabricaProyectilesPredeterminada, Proyectil

### Community 38 - "FormaColision"
Cohesion: 0.20
Nodes (9): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis (+1 more)

### Community 39 - "DatosContactoPlano"
Cohesion: 0.14
Nodes (3): DatosContacto, DatosContactoPlano, ColisionFormasTest

### Community 42 - "ContextoConsultas"
Cohesion: 0.08
Nodes (11): FabricaAmbiente, ConfiguracionFisica, PhysicsEngine, FabricaModos, ContextoComandosPartida, ComponentesPartida, DatosComponentesPartida, SesionJuego (+3 more)

### Community 43 - "PresentadorJuego"
Cohesion: 0.11
Nodes (5): DatosPresentadorJuego, PintorApuntadoJuego, PintorInformacionJuego, PintorMundoJuego, PresentadorJuego

### Community 44 - "ContextoJuego"
Cohesion: 0.10
Nodes (4): ContextoJuego, EscenaEstado, Arrojando, EnSuelo

### Community 47 - "FormaCaja"
Cohesion: 0.05
Nodes (10): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO, DatosFormaCapsula, DatosFormaPoligono, FormaCapsula (+2 more)

### Community 50 - "Tierra"
Cohesion: 0.11
Nodes (3): DatosTierra, Tierra, PelotaCatalogoTest

### Community 51 - "Preparacion"
Cohesion: 0.10
Nodes (3): DatosPreparacion, Preparacion, PreparacionModo

### Community 53 - "Ambiente"
Cohesion: 0.08
Nodes (9): Ambiente, FabricaMotorFisico, FabricaMotorFisicoPredeterminada, FabricaResolutor, FabricaResolutorPredeterminada, ResolutorColisiones, ResolutorFisico, DatosComponentesFisica (+1 more)

### Community 55 - "Efectos"
Cohesion: 0.15
Nodes (4): IntegradorVuelo, PasoIntegracion, AvanceMetricas, Efectos

### Community 57 - "ConstructorPanel.kt"
Cohesion: 0.16
Nodes (3): ConstructorPanel, NodosPanel, NodosPanelJavaFx

### Community 58 - "DatosDetectorReposo"
Cohesion: 0.09
Nodes (7): DetectorDeReposo, FabricaDetectoresReposo, DatosDetectorReposo, DetectorReposo, CicloFisico, CuerpoCinematico, ProyectilVisual

### Community 59 - "ControlEventosUI"
Cohesion: 0.29
Nodes (3): ControladorMenusRoles, ControladorEventosUI, ControlEventosUI

### Community 61 - "MenuPelotas.kt"
Cohesion: 0.19
Nodes (3): MenuPelotas, VistaMenuPelotas, VistaMenuPelotasJavaFx

### Community 63 - "FlechaGeometria"
Cohesion: 0.13
Nodes (3): DibujadorFlecha, DatosFlechaGeometria, FlechaGeometria

### Community 65 - "Documentation Generation"
Cohesion: 0.50
Nodes (4): graphify, graphify-out/graph.json, graphify-out/GRAPH_REPORT.md, graphify-out/wiki/index.md

### Community 68 - "FlechaGeometria"
Cohesion: 0.12
Nodes (4): DibujoVectoresFisica, DatosTrazoVector, TrazoVector, VectorRenderer

### Community 74 - "PosicionPelota"
Cohesion: 0.13
Nodes (3): LimitesFisicos, PosicionPelota, ProyectilSesionJuego

### Community 75 - "ConstructorMenu"
Cohesion: 0.20
Nodes (3): ConstructorMenu, DatosEspecificacionBotonMenu, EspecificacionBotonMenu

### Community 76 - "Mundo.kt"
Cohesion: 0.32
Nodes (3): Mundo, MundoProyectil, MundoVectores

### Community 78 - "MecanicaTiro"
Cohesion: 0.08
Nodes (7): MecanicaSesionJuego, MecanicaTiro, Integrador, Mecanica, ReinicioMecanica, UbicadorPelota, VueloMecanica

### Community 79 - "MainAppSupport.kt"
Cohesion: 0.15
Nodes (8): ConfiguracionInterfaz, EnsambladorInterfaz, FabricaComponentesUi, FabricaControlesUi, FabricaEscenaPrincipal, NucleoUi, NucleoUiPredeterminada, UiRefs

### Community 80 - "InputHandler"
Cohesion: 0.08
Nodes (12): InputHandler, FabricaTierra, ComponentesControl, ComponentesFisica, ComponentesSimulacion, BaseFisica, FabricaComponentesFisica, FabricaComponentesFisicaPredeterminada (+4 more)

### Community 82 - "FormaColision.kt"
Cohesion: 0.13
Nodes (3): GeometriaPelota, PaletaPelota, OrientacionColision

### Community 83 - "Puertos.kt"
Cohesion: 0.07
Nodes (10): ComandosCableados, ComandosEditor, ComandosJuego, ComandosParedesEditor, ComandosSuperficiesEditor, ControlModosEditor, ControlPausaEditor, EditorEscenario (+2 more)

### Community 86 - "Metricas"
Cohesion: 0.17
Nodes (3): LecturaMetricasTiro, Metricas, ReinicioMetricas

### Community 89 - "EstadoSprite"
Cohesion: 0.17
Nodes (3): ControlSprite, DibujoSprite, EstadoSprite

### Community 93 - "Renderable"
Cohesion: 0.15
Nodes (4): Hud, PintorApuntado, PintorMundo, Renderable

### Community 98 - "ContextoComandos"
Cohesion: 0.14
Nodes (5): ContextoComandos, ContextoServicios, EnsambladorServicios, FabricasServicios, FabricasServiciosPredeterminada

### Community 108 - ".celdas"
Cohesion: 0.16
Nodes (4): GeneradorArboles, GeneradorNubes, GeneradorPasto, RepartoEntorno

### Community 115 - "ArquitecturaAplicacion"
Cohesion: 0.12
Nodes (4): ArquitecturaAplicacion, CicloSimulacion, EjecutorAcciones, SincronizadorControles

### Community 138 - "DatosPelota"
Cohesion: 0.09
Nodes (3): DatosPelota, Pelota, MotorColisionesTest

### Community 148 - "Camara"
Cohesion: 0.08
Nodes (5): Camara, CamaraSimulacion, DatosCamaraSimulacion, TransformadorCoordenadasSimulacion, ControladorCamara

## Knowledge Gaps
- **21 isolated node(s):** `CARTON`, `MADERA`, `LADRILLO`, `HORMIGON`, `ACERO` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 528 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **124 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Vector2D` to `Pared`, `DatosCanvasRenderer`, `PresentadorConsola.kt`, `DatosSimulacion`, `DatosMotorFisico`, `ContextoJuego.kt`, `GestorEdicion`, `DatosSesionPartida`, `Brizna`, `ProyectilFisico`, `ServiciosCableados`, `SimulacionTest`, `DatosComandosPartida`, `Sprite`, `LecturaModo`, `LecturaProyectilSesionJuego`, `DatosDibujanteParedes`, `PantallaInicio`, `Simulation State Interface`, `Free Fall Tests`, `TextoDibujo`, `ContextoDibujo`, `Proyectil`, `FormaColision`, `DatosContactoPlano`, `DatosLecturaModo`, `Drag Input Logic`, `ContextoConsultas`, `ContextoJuego`, `FormaCaja`, `FormaCaja`, `VistaPelota`, `Tierra`, `Preparacion`, `Ambiente`, `Efectos`, `SimulacionPartidaTest.kt`, `DatosDetectorReposo`, `ControlEventosUI`, `Simulacion`, `MenuPelotas.kt`, `ContextoVuelo`, `FlechaGeometria`, `ImagenEnMundo`, `FlechaGeometria`, `DatosSimulacionConsultas`, `PosicionPelota`, `Mundo.kt`, `EstadoSesionJuego`, `MecanicaTiro`, `InputHandler`, `FormaColision.kt`, `Puertos.kt`, `SimulacionEstadoTest`, `SpriteEstatico`, `EstadoSprite`, `TransformadorCoordenadas`, `Renderable`, `LineaDibujo`, `ContextoComandos`, `DibujanteFlechaDireccion`, `CalculadoraTrayectoria`, `DbgTest.kt`, `Diag.kt`, `ModoSimulacion.kt`, `GeometriaColision`, `CaidaLibreTest.kt`, `DibujanteObjetos`, `ModoTiroParabolico`, `TrazoEditorEscenario`, `ModoSimulacion`, `FabricaSpriteEstatica`, `DatosVistaPelota`, `ModoCaidaLibre`, `DatosPelota`, `DibujanteEntorno`, `EntradaJuego`, `Camara`, `PresentadorConsola`, `ParedesTest.kt`, `.colocarPelotaParaLanzar`, `ProyectilControlable`, `DatosContextoServicios`, `.gravedad`, `SesionPartidaTest.kt`, `LecturaFisicaJuego`, `.iniciarApuntado`, `.offsetCamara`, `.previsualizarTrayectoria`?**
  _High betweenness centrality (0.515) - this node is a cross-community bridge._
- **What connects `CARTON`, `MADERA`, `LADRILLO` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TipoPelota` be split into smaller, more focused modules?**
  _Cohesion score 0.082010582010582 - nodes in this community are weakly interconnected._
- **Why does `Simulacion` connect `Simulacion` to `TipoPelota`, `TipoPared`, `MainApp`, `Pared`, `PresentadorConsola.kt`, `DatosSimulacion`, `.alturaMaxima`, `ContextoJuego.kt`, `.paredesDestruidas`, `Vector2D`, `.modoSuperficies`, `.factorGravedad`, `.alternarPausa`, `.distanciaX`, `.etiquetaColision`, `SimulacionTest`, `LecturaModo`, `.avanzarVuelo`, `.tiempoVuelo`, `.nuevaPartida`, `Simulation State Interface`, `MenuJuego`, `.aplicarGravedad`, `.colocarPelotaParaLanzar`, `ObservadorFinSimulacion`, `.rompeParedSeleccionada`, `Free Fall Tests`, `.gravedad`, `Proyectil`, `.modoParedes`, `.seleccionarModo`, `.iniciarApuntado`, `.nombresModos`, `ContextoJuego`, `.offsetCamara`, `.previsualizarTrayectoria`, `.sueloY`, `.volverAlMenu`, `SimulacionPartidaTest.kt`, `MainAppSupport.kt`, `InputHandler`, `SimulacionEstadoTest`, `Renderable`, `DbgTest.kt`, `Diag.kt`, `PresentadorConsola`, `FabricaComponentesUiPredeterminada`, `ArquitecturaAplicacion`, `CaidaLibreTest.kt`, `.limpiarSuperficiesLanzamiento`, `ModoSimulacion`?**
  _High betweenness centrality (0.156) - this node is a cross-community bridge._
- **Should `TipoPared` be split into smaller, more focused modules?**
  _Cohesion score 0.11764705882352941 - nodes in this community are weakly interconnected._
- **Why does `TipoPelota` connect `TipoPelota` to `TipoPared`, `DatosVistaPelota`, `MainApp`, `DatosSimulacion`, `DatosPelota`, `ContextoJuego.kt`, `DatosSesionPartida`, `Vector2D`, `ServiciosCableados`, `DatosComandosPartida`, `ConfiguracionJuego`, `AccionesJuego`, `ParedesTest.kt`, `LecturaConfiguracionJuego`, `AccionesSeleccionJuego`, `Proyectil`, `FormaColision`, `SesionPartidaTest.kt`, `ContextoConsultas`, `FormaCaja`, `VistaPelota`, `Tierra`, `Ambiente`, `MenuPelotas.kt`, `DatosSimulacionConsultas`, `MecanicaTiro`, `InputHandler`, `FormaColision.kt`, `Puertos.kt`, `DatosTipoPared`, `PresentadorConsola`, `DatosTipoPelota`, `MenusJuego`, `CaidaLibreTest.kt`, `SimulacionAcciones`, `EstadoProyectil`, `ModoSimulacion`?**
  _High betweenness centrality (0.130) - this node is a cross-community bridge._
- **Should `MainApp` be split into smaller, more focused modules?**
  _Cohesion score 0.08262108262108261 - nodes in this community are weakly interconnected._