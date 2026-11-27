# Graph Report - Tiro_Parab-lico  (2026-11-27)

## Corpus Check
- 153 files · ~33,081 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: (none) 2, .properties 2, .toml 1)

## Summary
- 2574 nodes · 5813 edges · 170 communities (47 shown, 123 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 60 edges (avg confidence: 0.86)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `85e0a329`
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
- MotorFisico
- ContextoJuego.kt
- HerramientaParedes
- DatosSesionPartida
- Vector2D
- Brizna
- EstadoMenus
- EfectoRotura
- .crear
- ServiciosCableados
- SimulacionTest
- DatosComandosPartida
- Sprite
- LecturaModo
- LecturaProyectilSesionJuego
- GestorMenus
- ConfiguracionJuego
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
- ProyectilFisico
- DatosLecturaModo
- Drag Input Logic
- ContextoConsultas
- PresentadorJuego
- ContextoJuego
- ComponentesFisica
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
- Puertos.kt
- MainAppSupport.kt
- FabricaSimulacion.kt
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
- Pelota
- ResultadoSimulacion
- PanelControl
- ContextoComandos
- DibujanteFlechaDireccion
- CalculadoraTrayectoria
- DbgTest.kt
- Diag.kt
- PintorInformacion
- PresentadorConsola
- GestorEdicion
- FabricaComponentesUiPredeterminada
- MenuFin
- .celdas
- IdentidadModo
- AccionesJuego
- MenusJuego
- .combo
- ArquitecturaAplicacion
- FabricaMotorFisico
- CaidaLibreTest.kt
- ConfiguracionPanelSimulacion
- AnimationTimer
- SimulacionAcciones
- ConsultaEntradaArrastre
- EstadoProyectil
- ModoTiroParabolico
- TrazoEditorEscenario
- ModoSimulacion
- FabricaSpriteEstatica
- VectorRenderer
- PanelSimulacion
- DatosVistaPelota
- DibujantePantallaInicio
- ConstructorPanel
- ModoCaidaLibre
- PanelPelotas
- MedidasMenu
- DatosPelota
- DibujanteEntorno
- Obstaculo
- DatosCamaraSimulacion
- PresentadorConsola
- ComandosJuego
- AccionesEscenarioJuego
- SuperficiesLanzamiento
- ParedesTest.kt
- ControlVueloJuego
- LecturaConfiguracionJuego
- LecturaMetricasJuego
- .crearPanelControl
- ProyectilControlable
- DatosContextoServicios
- AccionesSeleccionJuego
- Inactivo
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

## Communities (170 total, 123 thin omitted)

### Community 0 - "TipoPelota"
Cohesion: 0.06
Nodes (10): DatosTipoPelota, TipoPelota, BEISBOL, BLOQUE, BOLICHE, FUTBOL, GOMA, POLIEDRO (+2 more)

### Community 1 - "TipoPared"
Cohesion: 0.07
Nodes (8): ConstructorParedes, CreadorParedes, TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA

### Community 7 - "PresentadorConsola.kt"
Cohesion: 0.18
Nodes (3): ControlConsolaJuego, EntradaJuego, LecturaProyectilJuego

### Community 10 - "MotorFisico"
Cohesion: 0.27
Nodes (3): MotorFisico, FabricaResolutorPredeterminada, PelotaCatalogoTest

### Community 11 - "ContextoJuego.kt"
Cohesion: 0.21
Nodes (4): AccionesPartidaJuego, ControlEntradaJuego, LecturaEstadoJuego, NavegacionSesionJuego

### Community 12 - "HerramientaParedes"
Cohesion: 0.18
Nodes (4): ActivacionHerramientaParedes, EstadoHerramientaParedes, HerramientaParedes, TrazoHerramientaParedes

### Community 14 - "Vector2D"
Cohesion: 0.05
Nodes (6): InputListener, DatosFormaCaja, DatosFormaCapsula, DatosFormaPoligono, Vector2D, ApuntadoMecanica

### Community 15 - "Brizna"
Cohesion: 0.10
Nodes (5): Arbol, Brizna, Meta, Nube, GeneradorMetas

### Community 16 - "EstadoMenus"
Cohesion: 0.10
Nodes (5): ControlCicloMenus, CoordinadorMenus, EstadoMenus, SeleccionMenus, DetectorFinDeVuelo

### Community 18 - ".crear"
Cohesion: 0.17
Nodes (4): DibujantesCanvas, DibujanteObjetos, DibujantePanelInfo, FabricaRenderer

### Community 19 - "ServiciosCableados"
Cohesion: 0.07
Nodes (5): DatosServiciosCableados, ServiciosCableados, ControladorCamara, ControlCamara, Previsualizador

### Community 22 - "Sprite"
Cohesion: 0.18
Nodes (3): GestionSprites, Sprite, SpriteManager

### Community 27 - "DatosDibujanteParedes"
Cohesion: 0.11
Nodes (4): DatosDibujanteParedes, DibujanteParedes, EstiloDibujoPared, GeometriaGrietas

### Community 32 - "Free Fall Tests"
Cohesion: 0.17
Nodes (5): CaidaLibreTest, Architecture, Build, test, and run, Codebase conventions, Copilot instructions

### Community 34 - "ObservadorFinSimulacion"
Cohesion: 0.13
Nodes (3): ObservadorFinSimulacion, ObservadoresFin, RegistroObservadoresFin

### Community 35 - "LecturaEscenarioJuego"
Cohesion: 0.24
Nodes (3): ConstructorEstadoPanel, LecturaEscenarioJuego, LecturaPanelJuego

### Community 36 - "ContextoDibujo"
Cohesion: 0.15
Nodes (3): ContextoDibujo, ControlSprite, DibujoSprite

### Community 37 - "Proyectil"
Cohesion: 0.18
Nodes (4): FabricaProyectiles, FabricaProyectilesPredeterminada, Proyectil, FabricaProyectiles

### Community 38 - "FormaColision"
Cohesion: 0.16
Nodes (10): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis (+2 more)

### Community 39 - "ProyectilFisico"
Cohesion: 0.05
Nodes (10): DatosMotorFisico, DatosResolutorColisiones, ResolutorColisiones, ResolutorFisico, DatosContacto, DatosContactoPlano, Penetracion, Transformaciones (+2 more)

### Community 42 - "ContextoConsultas"
Cohesion: 0.08
Nodes (8): ConfiguracionFisica, FabricaModos, ContextoComandosPartida, DatosComponentesPartida, SesionJuego, SuperficiesApoyo, ConsultaSesionJuego, ContextoConsultas

### Community 43 - "PresentadorJuego"
Cohesion: 0.11
Nodes (5): DatosPresentadorJuego, PintorApuntadoJuego, PintorInformacionJuego, PintorMundoJuego, PresentadorJuego

### Community 44 - "ContextoJuego"
Cohesion: 0.10
Nodes (4): ContextoJuego, EscenaEstado, Arrojando, EnSuelo

### Community 45 - "ComponentesFisica"
Cohesion: 0.13
Nodes (6): InputHandler, ComandosCableados, ComponentesControl, ComponentesPartida, ComponentesSimulacion, ContextoSimulacion

### Community 47 - "FormaCaja"
Cohesion: 0.06
Nodes (9): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO, FormaCaja, FormaCapsula, FormaCirculo (+1 more)

### Community 51 - "Preparacion"
Cohesion: 0.07
Nodes (5): CondicionPrevia, DatosPreparacion, ModoArrastrable, Preparacion, PreparacionModo

### Community 55 - "Efectos"
Cohesion: 0.13
Nodes (5): CicloFisico, IntegradorVuelo, PasoIntegracion, AvanceMetricas, Efectos

### Community 58 - "DatosDetectorReposo"
Cohesion: 0.11
Nodes (6): DetectorDeReposo, FabricaDetectoresReposo, DatosDetectorReposo, DetectorReposo, CuerpoCinematico, ProyectilVisual

### Community 59 - "ControlEventosUI"
Cohesion: 0.26
Nodes (3): ControladorMenusRoles, ControladorEventosUI, ControlEventosUI

### Community 61 - "MenuPelotas.kt"
Cohesion: 0.21
Nodes (3): MenuPelotas, VistaMenuPelotas, VistaMenuPelotasJavaFx

### Community 63 - "FlechaGeometria"
Cohesion: 0.13
Nodes (3): DibujadorFlecha, DatosFlechaGeometria, FlechaGeometria

### Community 65 - "Documentation Generation"
Cohesion: 0.50
Nodes (4): graphify, graphify-out/graph.json, graphify-out/GRAPH_REPORT.md, graphify-out/wiki/index.md

### Community 74 - "PosicionPelota"
Cohesion: 0.13
Nodes (3): LimitesFisicos, PosicionPelota, ProyectilSesionJuego

### Community 75 - "ConstructorMenu"
Cohesion: 0.16
Nodes (3): ConstructorMenu, DatosEspecificacionBotonMenu, EspecificacionBotonMenu

### Community 76 - "Mundo.kt"
Cohesion: 0.18
Nodes (4): Mundo, MundoBasico, MundoProyectil, MundoVectores

### Community 78 - "Puertos.kt"
Cohesion: 0.24
Nodes (3): MecanicaSesionJuego, MecanicaTiro, Integrador

### Community 79 - "MainAppSupport.kt"
Cohesion: 0.15
Nodes (7): ConfiguracionInterfaz, EnsambladorInterfaz, FabricaComponentesUi, FabricaEscenaPrincipal, NucleoUi, NucleoUiPredeterminada, UiRefs

### Community 80 - "FabricaSimulacion.kt"
Cohesion: 0.07
Nodes (13): Camara, CamaraSimulacion, TransformadorCoordenadasSimulacion, FabricaAmbiente, FabricaTierra, ComponentesFisica, DatosComponentesFisica, BaseFisica (+5 more)

### Community 82 - "FormaColision.kt"
Cohesion: 0.10
Nodes (4): GeometriaPelota, ConsultaContactoColision, GeometriaColision, OrientacionColision

### Community 83 - "Puertos.kt"
Cohesion: 0.06
Nodes (12): ComandosEditor, ComandosParedesEditor, ComandosSuperficiesEditor, ControlModosEditor, ControlPausaEditor, EditorEscenario, EstadoEditorEscenario, Mecanica (+4 more)

### Community 86 - "Metricas"
Cohesion: 0.14
Nodes (4): LecturaMetricasTiro, Metricas, ReinicioMetricas, ResultadoMetricas

### Community 93 - "Renderable"
Cohesion: 0.13
Nodes (5): Hud, PintorApuntado, PintorMundo, Renderable, Renderer

### Community 98 - "ContextoComandos"
Cohesion: 0.14
Nodes (6): PhysicsEngine, ContextoComandos, ContextoServicios, EnsambladorServicios, FabricasServicios, FabricasServiciosPredeterminada

### Community 108 - ".celdas"
Cohesion: 0.19
Nodes (4): GeneradorArboles, GeneradorNubes, GeneradorPasto, RepartoEntorno

### Community 112 - "AccionesJuego"
Cohesion: 0.15
Nodes (3): MenuFin, MenuPausa, AccionesJuego

### Community 115 - "ArquitecturaAplicacion"
Cohesion: 0.15
Nodes (4): ArquitecturaAplicacion, CicloSimulacion, EjecutorAcciones, SincronizadorControles

### Community 116 - "FabricaMotorFisico"
Cohesion: 0.32
Nodes (3): FabricaMotorFisico, FabricaMotorFisicoPredeterminada, PrevisualizadorTrayectoria

## Knowledge Gaps
- **21 isolated node(s):** `CARTON`, `MADERA`, `LADRILLO`, `HORMIGON`, `ACERO` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 527 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **123 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Vector2D` to `Pared`, `DatosCanvasRenderer`, `PresentadorConsola.kt`, `DatosSimulacion`, `MotorFisico`, `ContextoJuego.kt`, `HerramientaParedes`, `DatosSesionPartida`, `Brizna`, `.crear`, `ServiciosCableados`, `SimulacionTest`, `DatosComandosPartida`, `Sprite`, `LecturaModo`, `LecturaProyectilSesionJuego`, `DatosDibujanteParedes`, `PantallaInicio`, `Simulation State Interface`, `Free Fall Tests`, `TextoDibujo`, `ContextoDibujo`, `Proyectil`, `FormaColision`, `ProyectilFisico`, `DatosLecturaModo`, `Drag Input Logic`, `ContextoConsultas`, `ContextoJuego`, `ComponentesFisica`, `FormaCaja`, `VistaPelota`, `Tierra`, `Preparacion`, `Ambiente`, `Efectos`, `SimulacionPartidaTest.kt`, `DatosDetectorReposo`, `ControlEventosUI`, `Simulacion`, `MenuPelotas.kt`, `ContextoVuelo`, `FlechaGeometria`, `ImagenEnMundo`, `FlechaGeometria`, `DatosSimulacionConsultas`, `PosicionPelota`, `Mundo.kt`, `EstadoSesionJuego`, `Puertos.kt`, `FabricaSimulacion.kt`, `FormaColision.kt`, `Puertos.kt`, `SimulacionEstadoTest`, `SpriteEstatico`, `EstadoSprite`, `TransformadorCoordenadas`, `Renderable`, `LineaDibujo`, `Pelota`, `ContextoComandos`, `DibujanteFlechaDireccion`, `CalculadoraTrayectoria`, `DbgTest.kt`, `Diag.kt`, `GestorEdicion`, `FabricaMotorFisico`, `CaidaLibreTest.kt`, `ConsultaEntradaArrastre`, `ModoTiroParabolico`, `TrazoEditorEscenario`, `ModoSimulacion`, `FabricaSpriteEstatica`, `VectorRenderer`, `DatosVistaPelota`, `ModoCaidaLibre`, `DatosPelota`, `DibujanteEntorno`, `Obstaculo`, `DatosCamaraSimulacion`, `PresentadorConsola`, `ParedesTest.kt`, `ControlVueloJuego`, `ProyectilControlable`, `DatosContextoServicios`, `Inactivo`, `SesionPartidaTest.kt`, `LecturaFisicaJuego`?**
  _High betweenness centrality (0.521) - this node is a cross-community bridge._
- **What connects `CARTON`, `MADERA`, `LADRILLO` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TipoPelota` be split into smaller, more focused modules?**
  _Cohesion score 0.06342780026990553 - nodes in this community are weakly interconnected._
- **Why does `Simulacion` connect `Simulacion` to `TipoPelota`, `TipoPared`, `Pared`, `PresentadorConsola.kt`, `DatosSimulacion`, `.alturaMaxima`, `.limpiarParedes`, `.paredesDestruidas`, `Vector2D`, `.modoSuperficies`, `EstadoMenus`, `.alternarPausa`, `.distanciaX`, `EfectoRotura`, `.etiquetaColision`, `.factorGravedad`, `SimulacionTest`, `LecturaModo`, `.lanzarPelota`, `.tiempoVuelo`, `.nuevaPartida`, `Simulation State Interface`, `.rompeParedSeleccionada`, `Free Fall Tests`, `.crearPanelControl`, `ObservadorFinSimulacion`, `Proyectil`, `.modoParedes`, `.seleccionarModo`, `ContextoJuego`, `ComponentesFisica`, `SimulacionPartidaTest.kt`, `MainAppSupport.kt`, `FabricaSimulacion.kt`, `SimulacionEstadoTest`, `Renderable`, `ResultadoSimulacion`, `PanelControl`, `DbgTest.kt`, `Diag.kt`, `PresentadorConsola`, `FabricaComponentesUiPredeterminada`, `ArquitecturaAplicacion`, `CaidaLibreTest.kt`, `.limpiarSuperficiesLanzamiento`, `ModoSimulacion`?**
  _High betweenness centrality (0.146) - this node is a cross-community bridge._
- **Should `TipoPared` be split into smaller, more focused modules?**
  _Cohesion score 0.07130124777183601 - nodes in this community are weakly interconnected._
- **Why does `TipoPelota` connect `TipoPelota` to `TipoPared`, `DatosVistaPelota`, `MainApp`, `DatosSimulacion`, `DatosPelota`, `ContextoJuego.kt`, `MotorFisico`, `DatosSesionPartida`, `Obstaculo`, `ServiciosCableados`, `DatosComandosPartida`, `ComandosJuego`, `ConfiguracionJuego`, `ParedesTest.kt`, `LecturaConfiguracionJuego`, `AccionesSeleccionJuego`, `Proyectil`, `FormaColision`, `SesionPartidaTest.kt`, `ContextoConsultas`, `ComponentesFisica`, `FormaCaja`, `VistaPelota`, `MenuPelotas.kt`, `DatosSimulacionConsultas`, `Puertos.kt`, `FabricaSimulacion.kt`, `Puertos.kt`, `DatosTipoPared`, `Pelota`, `PresentadorConsola`, `MenusJuego`, `FabricaMotorFisico`, `CaidaLibreTest.kt`, `SimulacionAcciones`, `EstadoProyectil`, `ModoSimulacion`?**
  _High betweenness centrality (0.132) - this node is a cross-community bridge._
- **Should `MainApp` be split into smaller, more focused modules?**
  _Cohesion score 0.11052631578947368 - nodes in this community are weakly interconnected._