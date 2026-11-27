# Graph Report - Tiro_Parab-lico  (2026-11-27)

## Corpus Check
- 153 files · ~33,021 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 7 file(s) not represented in the graph (top: (none) 2, .properties 2, .toml 1)

## Summary
- 2569 nodes · 5788 edges · 155 communities (53 shown, 102 thin omitted)
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
- DatosMotorFisico
- ContextoJuego.kt
- GestorEdicion
- DatosSesionPartida
- Vector2D
- DibujanteEntorno
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
- DatosContactoPlano
- DatosLecturaModo
- Drag Input Logic
- ComponentesPartida
- PresentadorJuego
- ContextoJuego
- ComponentesFisica
- SimulacionEdicion
- FormaCaja
- MenuParedes
- VistaPelota
- ProyectilFisico
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
- DatosTipoPelota
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
- DibujanteObjetos
- SpriteEstatico
- DatosTipoPared
- EstadoSprite
- EnVuelo
- TransformadorCoordenadas
- CatalogoModos
- Renderable
- LineaDibujo
- CondicionPrevia
- ResultadoSimulacion
- PanelControl
- ContextoComandos
- DibujanteFlechaDireccion
- CalculadoraTrayectoria
- DbgTest.kt
- Diag.kt
- PintorInformacion
- PresentadorConsola
- CreadorParedes
- FabricaComponentesUiPredeterminada
- MenuFin
- .rejillaTipos
- ModoSimulacion.kt
- AccionesJuego
- MenusJuego
- .combo
- ArquitecturaAplicacion
- PhysicsEngine
- CaidaLibreTest.kt
- ConfiguracionPanelSimulacion
- AnimationTimer
- SimulacionAcciones
- ConsultaEntradaArrastre
- EstadoProyectil
- ConstructorParedes
- TrazoEditorEscenario
- ModoSimulacion
- FabricaSpriteEstatica
- Architecture
- PanelSimulacion
- DatosVistaPelota
- DibujantePantallaInicio
- ConstructorPanel
- PanelPelotas
- MedidasMenu
- Pelota
- DatosFormaCapsula

## God Nodes (most connected - your core abstractions)
1. `Vector2D` - 458 edges
2. `Pared` - 155 edges
3. `Simulacion` - 155 edges
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

## Communities (155 total, 102 thin omitted)

### Community 0 - "TipoPelota"
Cohesion: 0.08
Nodes (9): TipoPelota, BEISBOL, BLOQUE, BOLICHE, FUTBOL, GOMA, POLIEDRO, RUGBY (+1 more)

### Community 1 - "TipoPared"
Cohesion: 0.12
Nodes (6): TipoPared, ACERO, CARTON, HORMIGON, LADRILLO, MADERA

### Community 4 - "Pared"
Cohesion: 0.09
Nodes (3): GestionParedesFisicas, Pared, ParedesTest

### Community 5 - "DatosCanvasRenderer"
Cohesion: 0.08
Nodes (3): CanvasRenderer, DatosCanvasRenderer, Renderer

### Community 7 - "PresentadorConsola.kt"
Cohesion: 0.09
Nodes (5): ControlConsolaJuego, ControlVueloJuego, EntradaJuego, LecturaMetricasJuego, LecturaProyectilJuego

### Community 10 - "DatosMotorFisico"
Cohesion: 0.10
Nodes (3): DatosMotorFisico, MotorFisico, Transformaciones

### Community 11 - "ContextoJuego.kt"
Cohesion: 0.09
Nodes (7): AccionesEscenarioJuego, AccionesPartidaJuego, AccionesSeleccionJuego, ControlEntradaJuego, LecturaEstadoJuego, LecturaFisicaJuego, NavegacionSesionJuego

### Community 12 - "GestorEdicion"
Cohesion: 0.10
Nodes (5): ActivacionHerramientaParedes, EstadoHerramientaParedes, HerramientaParedes, TrazoHerramientaParedes, GestorEdicion

### Community 14 - "Vector2D"
Cohesion: 0.05
Nodes (6): DatosCamaraSimulacion, InputListener, Obstaculo, ProyectilControlable, Vector2D, ApuntadoMecanica

### Community 15 - "DibujanteEntorno"
Cohesion: 0.05
Nodes (12): DibujanteEntorno, Arbol, Brizna, Meta, Nube, GeneradorArboles, GeneradorEntorno, GeneradorMetas (+4 more)

### Community 16 - "EstadoMenus"
Cohesion: 0.10
Nodes (6): ControladorMenusRoles, ControlCicloMenus, CoordinadorMenus, EstadoMenus, SeleccionMenus, DetectorFinDeVuelo

### Community 18 - ".crear"
Cohesion: 0.25
Nodes (3): DibujantesCanvas, DibujantePanelInfo, FabricaRenderer

### Community 19 - "ServiciosCableados"
Cohesion: 0.05
Nodes (8): DatosServiciosCableados, ServiciosCableados, ControlCamara, Mecanica, Previsualizador, ReinicioMecanica, UbicadorPelota, VueloMecanica

### Community 21 - "DatosComandosPartida"
Cohesion: 0.14
Nodes (3): ComandosPartida, DatosComandosPartida, Gravedades

### Community 22 - "Sprite"
Cohesion: 0.20
Nodes (3): GestionSprites, Sprite, SpriteManager

### Community 23 - "LecturaModo"
Cohesion: 0.10
Nodes (4): CaidaLibre, ModoCaidaLibre, LecturaModo, PresentacionModo

### Community 27 - "DatosDibujanteParedes"
Cohesion: 0.08
Nodes (5): DatosDibujanteParedes, DibujanteParedes, EstiloDibujoPared, GeometriaGrietas, SuperficiesLanzamiento

### Community 34 - "ObservadorFinSimulacion"
Cohesion: 0.13
Nodes (3): ObservadorFinSimulacion, ObservadoresFin, RegistroObservadoresFin

### Community 35 - "LecturaEscenarioJuego"
Cohesion: 0.13
Nodes (4): ConstructorEstadoPanel, LecturaConfiguracionJuego, LecturaEscenarioJuego, LecturaPanelJuego

### Community 37 - "Proyectil"
Cohesion: 0.20
Nodes (4): FabricaProyectiles, FabricaProyectilesPredeterminada, Proyectil, FabricaProyectiles

### Community 38 - "FormaColision"
Cohesion: 0.14
Nodes (11): DecoracionBeisbol, DecoracionBloque, DecoracionBoliche, DecoracionFutbol, DecoracionPelota, DecoracionPoliedro, DecoracionRugby, DecoracionTenis (+3 more)

### Community 39 - "DatosContactoPlano"
Cohesion: 0.14
Nodes (3): DatosContacto, DatosContactoPlano, ColisionFormasTest

### Community 43 - "PresentadorJuego"
Cohesion: 0.12
Nodes (5): DatosPresentadorJuego, PintorApuntadoJuego, PintorInformacionJuego, PintorMundoJuego, PresentadorJuego

### Community 44 - "ContextoJuego"
Cohesion: 0.08
Nodes (5): ContextoJuego, EscenaEstado, Arrojando, EnSuelo, Inactivo

### Community 45 - "ComponentesFisica"
Cohesion: 0.12
Nodes (6): ControlEntradaArrastre, InputHandler, ComponentesControl, ComponentesPartida, ComponentesSimulacion, ContextoSimulacion

### Community 47 - "FormaCaja"
Cohesion: 0.04
Nodes (11): TipoColision, CAJA, CAPSULA, CIRCULO, POLIGONO, DatosFormaCaja, DatosFormaPoligono, FormaCaja (+3 more)

### Community 50 - "ProyectilFisico"
Cohesion: 0.21
Nodes (3): DatosResolutorColisiones, Penetracion, ProyectilFisico

### Community 51 - "Preparacion"
Cohesion: 0.10
Nodes (3): DatosPreparacion, Preparacion, PreparacionModo

### Community 53 - "Ambiente"
Cohesion: 0.13
Nodes (5): Ambiente, FabricaResolutor, FabricaResolutorPredeterminada, ResolutorColisiones, ResolutorFisico

### Community 55 - "Efectos"
Cohesion: 0.14
Nodes (5): CicloFisico, IntegradorVuelo, PasoIntegracion, AvanceMetricas, Efectos

### Community 58 - "DatosDetectorReposo"
Cohesion: 0.11
Nodes (6): DetectorDeReposo, FabricaDetectoresReposo, DatosDetectorReposo, DetectorReposo, CuerpoCinematico, ProyectilVisual

### Community 61 - "MenuPelotas.kt"
Cohesion: 0.21
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

### Community 73 - "ConstructorMenu"
Cohesion: 0.16
Nodes (3): ConstructorMenu, DatosEspecificacionMenu, EspecificacionMenu

### Community 74 - "PosicionPelota"
Cohesion: 0.13
Nodes (3): LimitesFisicos, PosicionPelota, ProyectilSesionJuego

### Community 76 - "Mundo.kt"
Cohesion: 0.18
Nodes (4): Mundo, MundoBasico, MundoProyectil, MundoVectores

### Community 78 - "Puertos.kt"
Cohesion: 0.11
Nodes (5): MecanicaSesionJuego, MecanicaTiro, ConfiguracionSesionJuego, Integrador, SesionJuego

### Community 79 - "MainAppSupport.kt"
Cohesion: 0.15
Nodes (6): ConfiguracionInterfaz, EnsambladorInterfaz, FabricaComponentesUi, NucleoUi, NucleoUiPredeterminada, UiRefs

### Community 80 - "FabricaSimulacion.kt"
Cohesion: 0.11
Nodes (9): CamaraSimulacion, TransformadorCoordenadasSimulacion, FabricaTierra, BaseFisica, FabricaComponentesFisica, FabricaComponentesFisicaPredeterminada, FabricaComponentesPartida, FabricaComponentesPartidaPredeterminada (+1 more)

### Community 82 - "FormaColision.kt"
Cohesion: 0.12
Nodes (3): ConsultaContactoColision, GeometriaColision, OrientacionColision

### Community 83 - "Puertos.kt"
Cohesion: 0.05
Nodes (14): ComandosCableados, ComandosEditor, ComandosJuego, ComandosParedesEditor, ComandosSuperficiesEditor, ControlModosEditor, ControlPausaEditor, EditorEscenario (+6 more)

### Community 89 - "EstadoSprite"
Cohesion: 0.17
Nodes (3): ControlSprite, DibujoSprite, EstadoSprite

### Community 93 - "Renderable"
Cohesion: 0.15
Nodes (4): Hud, PintorApuntado, PintorMundo, Renderable

### Community 98 - "ContextoComandos"
Cohesion: 0.12
Nodes (6): ContextoComandos, ContextoServicios, DatosContextoServicios, EnsambladorServicios, FabricasServicios, FabricasServiciosPredeterminada

### Community 104 - "PresentadorConsola"
Cohesion: 0.13
Nodes (3): App, EntradaConsola, PresentadorConsola

### Community 105 - "CreadorParedes"
Cohesion: 0.08
Nodes (6): Camara, CreadorParedes, FabricaModos, ControladorCamara, ConsultaSesionJuego, ContextoConsultas

### Community 112 - "AccionesJuego"
Cohesion: 0.15
Nodes (3): MenuFin, MenuPausa, AccionesJuego

### Community 115 - "ArquitecturaAplicacion"
Cohesion: 0.15
Nodes (4): ArquitecturaAplicacion, CicloSimulacion, EjecutorAcciones, SincronizadorControles

### Community 116 - "PhysicsEngine"
Cohesion: 0.11
Nodes (8): FabricaAmbiente, FabricaMotorFisico, FabricaMotorFisicoPredeterminada, ConfiguracionFisica, PhysicsEngine, ContextoComandosPartida, ComponentesFisica, DatosComponentesFisica

### Community 128 - "Architecture"
Cohesion: 0.40
Nodes (4): Architecture, Build, test, and run, Codebase conventions, Copilot instructions

### Community 138 - "Pelota"
Cohesion: 0.05
Nodes (6): DatosTierra, Tierra, DatosPelota, Pelota, MotorColisionesTest, PelotaCatalogoTest

## Knowledge Gaps
- **21 isolated node(s):** `CARTON`, `MADERA`, `LADRILLO`, `HORMIGON`, `ACERO` (+16 more)
  These have ≤1 connection - possible missing edges or undocumented components. (Counts symbols only; 527 node(s) total have ≤1 connection when file, concept and rationale nodes are included.)
- **102 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `Vector2D` connect `Vector2D` to `Pared`, `DatosCanvasRenderer`, `PresentadorConsola.kt`, `DatosSimulacion`, `DatosMotorFisico`, `ContextoJuego.kt`, `GestorEdicion`, `DatosSesionPartida`, `DibujanteEntorno`, `ServiciosCableados`, `SimulacionTest`, `DatosComandosPartida`, `Sprite`, `LecturaModo`, `LecturaProyectilSesionJuego`, `DatosDibujanteParedes`, `PantallaInicio`, `Simulation State Interface`, `Free Fall Tests`, `TextoDibujo`, `ContextoDibujo`, `Proyectil`, `FormaColision`, `DatosContactoPlano`, `DatosLecturaModo`, `Drag Input Logic`, `ContextoJuego`, `ComponentesFisica`, `FormaCaja`, `VistaPelota`, `ProyectilFisico`, `Preparacion`, `Ambiente`, `Efectos`, `SimulacionPartidaTest.kt`, `DatosDetectorReposo`, `ControlEventosUI`, `Simulacion`, `MenuPelotas.kt`, `ContextoVuelo`, `FlechaGeometria`, `ImagenEnMundo`, `FlechaGeometria`, `DatosSimulacionConsultas`, `PosicionPelota`, `Mundo.kt`, `EstadoSesionJuego`, `Puertos.kt`, `FabricaSimulacion.kt`, `FormaColision.kt`, `Puertos.kt`, `SimulacionEstadoTest`, `DibujanteObjetos`, `SpriteEstatico`, `EstadoSprite`, `TransformadorCoordenadas`, `CatalogoModos`, `Renderable`, `LineaDibujo`, `CondicionPrevia`, `ContextoComandos`, `DibujanteFlechaDireccion`, `CalculadoraTrayectoria`, `DbgTest.kt`, `Diag.kt`, `PresentadorConsola`, `CreadorParedes`, `ModoSimulacion.kt`, `PhysicsEngine`, `CaidaLibreTest.kt`, `ConsultaEntradaArrastre`, `TrazoEditorEscenario`, `ModoSimulacion`, `FabricaSpriteEstatica`, `DatosVistaPelota`, `Pelota`, `DatosFormaCapsula`?**
  _High betweenness centrality (0.508) - this node is a cross-community bridge._
- **What connects `CARTON`, `MADERA`, `LADRILLO` to the rest of the system?**
  _21 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `TipoPelota` be split into smaller, more focused modules?**
  _Cohesion score 0.07816091954022988 - nodes in this community are weakly interconnected._
- **Why does `TipoPelota` connect `TipoPelota` to `TipoPared`, `DatosVistaPelota`, `MainApp`, `Pared`, `DatosSimulacion`, `Pelota`, `ContextoJuego.kt`, `DatosSesionPartida`, `Vector2D`, `ServiciosCableados`, `DatosComandosPartida`, `ConfiguracionJuego`, `LecturaEscenarioJuego`, `Proyectil`, `FormaColision`, `FormaCaja`, `VistaPelota`, `MenuPelotas.kt`, `DatosTipoPelota`, `DatosSimulacionConsultas`, `Puertos.kt`, `FabricaSimulacion.kt`, `Puertos.kt`, `DatosTipoPared`, `CatalogoModos`, `CondicionPrevia`, `PresentadorConsola`, `CreadorParedes`, `MenusJuego`, `PhysicsEngine`, `CaidaLibreTest.kt`, `SimulacionAcciones`, `EstadoProyectil`?**
  _High betweenness centrality (0.129) - this node is a cross-community bridge._
- **Should `TipoPared` be split into smaller, more focused modules?**
  _Cohesion score 0.11764705882352941 - nodes in this community are weakly interconnected._
- **Why does `Simulacion` connect `Simulacion` to `TipoPelota`, `TipoPared`, `Architecture`, `Pared`, `.avanzarVuelo`, `PresentadorConsola.kt`, `DatosSimulacion`, `.alturaMaxima`, `.limpiarParedes`, `.paredesDestruidas`, `Vector2D`, `.aplicarGravedad`, `EstadoMenus`, `.alternarPausa`, `.distanciaX`, `EfectoRotura`, `.etiquetaColision`, `.factorGravedad`, `.modoSuperficies`, `LecturaModo`, `.nombresModos`, `.lanzarPelota`, `.tiempoVuelo`, `SimulacionTest`, `.nuevaPartida`, `Simulation State Interface`, `.rompeParedSeleccionada`, `Free Fall Tests`, `.sueloY`, `ObservadorFinSimulacion`, `Proyectil`, `ContextoJuego`, `ComponentesFisica`, `SimulacionPartidaTest.kt`, `MainAppSupport.kt`, `FabricaSimulacion.kt`, `SimulacionEstadoTest`, `Renderable`, `DbgTest.kt`, `Diag.kt`, `PresentadorConsola`, `FabricaComponentesUiPredeterminada`, `ArquitecturaAplicacion`, `CaidaLibreTest.kt`, `.limpiarSuperficiesLanzamiento`, `ModoSimulacion`?**
  _High betweenness centrality (0.126) - this node is a cross-community bridge._
- **Should `MainApp` be split into smaller, more focused modules?**
  _Cohesion score 0.11695906432748537 - nodes in this community are weakly interconnected._