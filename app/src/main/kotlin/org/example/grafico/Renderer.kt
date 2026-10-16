package org.example.grafico

/**
 * Pantalla completa: reune los tres papeles de dibujo (contexto, mundo y HUD).
 * Existe para quien necesita pintar a la vez, como [PresentadorJuego].
 */
interface Renderer : ContextoDibujo, Mundo, Hud