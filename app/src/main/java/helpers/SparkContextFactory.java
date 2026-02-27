package helpers;

import org.apache.spark.sql.SparkSession;

public class SparkContextFactory {
	public static SparkSession createSession(String appName) {
		// nueva sesión de spark
		// Nota importante, todavía no se distribuyo nada
		// recien luego de realizar una accion se distribuye
		// todas las acciones que se le realicen al DDR son
		// lazy y solo va a esperar hasta que hay un llamado
		// de una accion par ejecutarse
		// ej .map es un cambio al DDR .collect es una accion
		return SparkSession.builder().appName(appName).master("local[*]").getOrCreate();
	}
}
