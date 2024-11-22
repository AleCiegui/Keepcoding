package Examen

import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD
import utils.TestInit

class examenTestEjercicio4 extends TestInit {

  "Ejercicio 4" should "contar las ocurrencias de cada palabra en una lista" in {
    // Crear SparkSession
    val spark: SparkSession = SparkSession.builder()
      .appName("Ejercicio4Test")
      .master("local[*]")
      .getOrCreate()

    // Datos de prueba: lista de palabras
    val palabras: List[String] = List("manzana", "pera", "manzana", "uva", "pera", "pera")

    // Llamar al método ejercicio4
    val resultado: RDD[(String, Int)] = examen.ejercicio4(palabras)(spark)

    // Resultado esperado
    val esperado: Map[String, Int] = Map(
      "manzana" -> 2,
      "pera" -> 3,
      "uva" -> 1
    )

    // Validar los datos
    val resultadoMap = resultado.collect().toMap
    assert(resultadoMap == esperado, "El conteo de palabras no coincide con el esperado")

    // Detener SparkSession
    spark.stop()
  }
}
