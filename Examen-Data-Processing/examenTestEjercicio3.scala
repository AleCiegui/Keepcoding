package Examen

import org.apache.spark.sql.{DataFrame, SparkSession}
import utils.TestInit

class examenTestEjercicio3 extends TestInit {

  "Ejercicio 3" should "calcular el promedio de calificaciones por estudiante tras un join" in {
    // Crear SparkSession
    val spark: SparkSession = SparkSession.builder()
      .appName("Ejercicio3Test")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Datos de prueba: estudiantes
    val estudiantes: DataFrame = Seq(
      (1, "Juan"),
      (2, "Maria"),
      (3, "Pedro"),
      (4, "Ana")
    ).toDF("id", "nombre")

    // Datos de prueba: calificaciones
    val calificaciones: DataFrame = Seq(
      (1, "Matematicas", 8.5),
      (1, "Lengua", 7.0),
      (2, "Matematicas", 9.0),
      (2, "Lengua", 8.5),
      (3, "Matematicas", 6.5),
      (4, "Lengua", 7.8)
    ).toDF("id_estudiante", "asignatura", "calificacion")

    // Llamar a ejercicio3
    val resultado = examen.ejercicio3(estudiantes, calificaciones)(spark)

    // DataFrame esperado
    val esperado: DataFrame = Seq(
      ("Maria", 8.75),
      ("Juan", 7.75),
      ("Ana", 7.8),
      ("Pedro", 6.5)
    ).toDF("nombre", "promedio_calificacion")

    // Validar los datos: comparar los DataFrames
    val sonIguales = resultado.except(esperado).isEmpty &&
      esperado.except(resultado).isEmpty

    // Aserción
    assert(sonIguales, "Los DataFrames no coinciden")

    // Detener SparkSession
    spark.stop()
  }
}
