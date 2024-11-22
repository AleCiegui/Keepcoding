package Examen

import org.apache.spark.sql.{DataFrame, SparkSession}
import utils.TestInit

class examenTestEjercicio1 extends TestInit {

  "Ejercicio 1" should "dado un DF me tienes que devolver un resultado" in {
    // Crear el SparkSession
    val spark: SparkSession = SparkSession.builder()
      .appName("Ejercicio1Test")
      .master("local[*]") // Usar todos los núcleos disponibles para ejecución local
      .getOrCreate()

    import spark.implicits._

    // Datos de prueba
    val in: DataFrame = Seq(
      ("Juan", 20, 8.5),
      ("Maria", 22, 9.0),
      ("Pedro", 21, 7.8),
      ("Ana", 23, 8.0),
      ("Luis", 20, 6.5)
    ).toDF("nombre", "edad", "calificacion")

    // Llamar ejercicio1
    val resultado = examen.ejercicio1(in)(spark)

    // Validar que el número de filas sea 2 (los estudiantes con calificación > 8)
    resultado.count() shouldBe 2

    // Detener el SparkSession al finalizar el test
    spark.stop()
  }
}
