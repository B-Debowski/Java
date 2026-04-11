package com.a14.view;

/**
 *
 * @param minX keeps minimal X parameter of screen
 * @param minY keeps minimal Y parameter of screen
 * @param maxX keeps maximal X parameter of screen
 * @param maxY keeps maximal Y parameter of screen
 */
public record CameraBounds(int minX, int minY, int maxX, int maxY) {}
