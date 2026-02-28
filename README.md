### Funcionamiento general
Se lee a partir de un archivo las suscripciones a articulos y posteos en foros, se las 
transforma en un Dataframe Distribuido Resiliente (RDD), y por medio de 
workers se los analiza utilizando heurísticas para extraer entidades de interés, 
que luego son agrupadas para presentarlas en formato de feed.

## Instrucciones
- Instalar Gradle
- Desde la carpeta root del programa, ejecutar make run
- Para calculo de Entidades nombradas ejecutar make run-ne

