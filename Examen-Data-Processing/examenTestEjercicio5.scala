package Examen

import org.apache.spark.sql.{DataFrame, SparkSession}
import utils.TestInit

class examenTestEjercicio5 extends TestInit {

  "Ejercicio 5" should "calcular el ingreso total por producto desde un archivo CSV" in {
    // Crear SparkSession
    val spark: SparkSession = SparkSession.builder()
      .appName("Ejercicio5Test")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Ruta del archivo CSV
    val filePath = "C:\\Users\\acata\\Desktop\\Programacion\\Keepcoding\\Big data processing\\ventas.csv"

    // Llamar a ejercicio5
    val resultado: DataFrame = examen.ejercicio5(filePath)(spark)

    // DataFrame esperado
    val esperado: DataFrame = Seq(
      (101, 460.0), // Ingreso total del producto 101: (23 * 20.0) + (2 * 20.0)
      (102, 405.0),  // Ingreso total del producto 102: (15 * 15.0)
      (103, 280.0)   // Ingreso total del producto 103: (28 * 10.0)
    ).toDF("id_producto", "total_ingreso")

    // Comparar solo las primeras 3 filas del resultado
    val resultadoFiltrado = resultado.limit(3)


    // Validar los datos: comparar los DataFrames
    val sonIguales = resultadoFiltrado.except(esperado).isEmpty &&
      esperado.except(resultado).isEmpty

    // Aserción
    assert(sonIguales, "Los DataFrames no coinciden")

    // Detener SparkSession
    spark.stop()
  }
}
