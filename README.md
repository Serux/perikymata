# Perikymata 
######Análisis paleontológico de piezas dentales
Proyecto en colaboración con el departamento de investigación del Museo de la Evolución Humana en Burgos.

El objetivo de este Proyecto consistió en realizar un marcado semi-automático de una serie de surcos en piezas dentales, llamados perikymata, observadas en las imágenes tomadas mediante microscopio electrónico.

Para realizar este proyecto, se desarrolló una aplicación en Java, con una interfaz gráfica usando JavaFX y la librería de operaciones sobre imágenes ImageJ. También se realizó una parte de OpenCV desarrollada en el lenguaje C/C++.

La aplicación desarrollada se divide en tres etapas principales:
Primero se realiza la unión automática de varias imágenes para formar una imagen de pieza dental completa usando OpenCV.
La segunda fase es una edición de la imagen, aplicando filtros Prewitt-Gauss hacen que las periymata sean más fáciles de ver.
La tercera fase es el dibujado, por parte del usuario, de una línea que atraviese la zona donde se quieren contar perikymata. La aplicación dibuja un punto en cada perikyma detectada y guarda los datos sobre las posiciones y distancias de las perikyma en forma de CSV.

La aplicación está basada en la creación de un proyecto por cada imagen, por lo que se puede recuperar el trabajo realizado en cualquier momento.

Esta aplicación está en proceso de ser registrada a través de la Universidad de Burgos.

######Paleontological analisys of dental pieces

This applications tries to help to detect Perikymata in a tooth image taken by a electronic microscope.

This application Is divided in three stages:
* Stage1:
  * Panorama union of tooth images (or giving the full image from the start).
* Stage2:
  * Prewitt and Gauss filtering for helping the visualization of perikymata.
* Stage3:
  * Marking the regions of interest of the image (start and end of the perikymata zone to calculate the deciles, and start-end of the "measure" to get the real distance between perikymata on the image).
  * Drawing a line and try to auto-mark real perikymata over that line, corrections can be made on errors.
  * Export of the image data on a CSV with the following format

```
"Project name"
Measure unit,"measure unit"
Decile size,"measure size, calculated in the measure unit"

Decile,Coordinates,Distance to previous
"number of decile","X Y coordinates","Distance in the measure unit to previous"

Perikymata per decile
1,2,3,4,5,6,7,8,9,10
"perikymata in decile 1","perikymata in decile 2",...
```

