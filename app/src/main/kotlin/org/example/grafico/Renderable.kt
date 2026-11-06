package org.example.grafico

/**
 * Superficie completa de dibujo. Reune los contratos segregados de contexto,
 * mundo y HUD, de modo que el presentador depende solo de esta abstraccion y
 * nunca de una implementacion concreta como JavaFX (DIP).
 */
interface Renderable : ContextoDibujo, Mundo, Hud
