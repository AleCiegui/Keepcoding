package Examen

import org.apache.spark.sql.{DataFrame, SparkSession}
import utils.TestInit

class examenTestEjercicio2 extends TestInit {

  "Ejercicio 2" should "dado un DF de números, agregar una columna que indique si el número es par o impar" in {
    // Crear SparkSession
    val spark: SparkSession = SparkSession.builder()
      .appName("Ejercicio2Test")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Datos de prueba: un DataFrame con números
    val in: DataFrame = Seq(1, 2, 3, 4, 5).toDF("numero")

    // Llamar a ejercicio2
    val resultado = examen.ejercicio2(in)(spark)

    // DataFrame esperado
    val esperado: DataFrame = Seq(
      (1, "Impar"),
      (2, "Par"),
      (3, "Impar"),
      (4, "Par"),
      (5, "Impar")
    ).toDF("numero", "paridad")

    // Validar los datos: comparar los DataFrames
    val sonIguales = resultado.except(esperado).isEmpty &&
      esperado.except(resultado).isEmpty

    // Aserción
    assert(sonIguales, "Los DataFrames no coinciden")

    // Detener SparkSession
    spark.stop()
  }
}
