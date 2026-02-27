# Paradigmas - Laboratorio 2
## Grupo 44
### Integrantes
- Filippa, Renzo
- Mourelle, Facundo
- Verzini, Máximo

## Instrucciones
- Instalar Gradle
- Desde la carpeta root del programa, ejecutar make run
- Para calculo de Entidades nombradas ejecutar make run-ne

### Detalles sobre subparser
no se usaron las subscripciones dadas por la catedra. Esto se dio ya que 
utilizaban otro formato que afectaba fuertemente el funcionamiento de la
aplicación y nos resultó más trabajo hacer ese fix que el laboratorio en
si. Igualmente se agregaron más subscripciones a la lista para mantener 
el espíritu del laboratorio. Algunas implementaciones del aprser de formato
ATOM se pueden observer en los commits anteriores a la primera label.

### funcionamiento general
Se tomo como base el laboratorio anterior en donde procesabamos subscripciones 
e imprimiamos cierto output en pantalla. Esta base fue mantenida y mejorada.
primero que nada se traslado el proyecto al paradigma distribuido a través
del framework "spark". Además se modularizó la clase principal mejorando su 
legibilidad y escalado. Nuevas estadísticas útiles tanto para procesamiento
de datos como para debug sobre las entidades nombradas se decidió que su display
no sea por stdout sino por el archivo ne_stats.txt para una lectura más comprensible.

