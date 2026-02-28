Desarrollo de un pipeline de datos distribuido diseñado para la curaduría inteligente de artículos y foros. Utilizando Apache Spark, el sistema paraleliza el análisis de entidades mediante heurísticas personalizadas, permitiendo transformar grandes volúmenes de suscripciones estáticas en un flujo de información relevante y categorizada.

### Funcionamiento general
Se lee a partir de un archivo las suscripciones a articulos y posteos en foros, se las 
transforma en un Dataset Distribuido Resiliente (RDD), y por medio de 
workers se los analiza utilizando heurísticas para extraer entidades de interés, 
que luego son agrupadas para presentarlas en formato de feed.

## Instrucciones
- Instalar Java 11
- Instalar Gradle y ejecutar `make setup`
- Desde la carpeta root del programa, ejecutar `make build`
- Para calculo de Entidades nombradas ejecutar `make run-ne`
- El archivo de salida "ne_stats.txt" contiene el resultado obtenido

