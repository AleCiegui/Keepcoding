package Examen

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession, functions}
import org.apache.spark.sql.functions._

object examen {

  /**Ejercicio 1: Crear un DataFrame y realizar operaciones básicas
   Pregunta: Crea un DataFrame a partir de una secuencia de tuplas que contenga información sobre
   estudiantes (nombre, edad, calificación).
   Realiza las siguientes operaciones:

   Muestra el esquema del DataFrame.
   Filtra los estudiantes con una calificación mayor a 8.
   Selecciona los nombres de los estudiantes y ordénalos por calificación de forma descendente.
   */
  def ejercicio1(estudiantes: DataFrame)(spark:SparkSession): DataFrame = {
    import spark.implicits._

    // Mostrar el esquema del DataFrame
    estudiantes.printSchema()

    // Filtrar los estudiantes con calificación mayor a 8
    val filtrados = estudiantes.filter($"calificacion" > 8)

    // Seleccionar nombres y ordenarlos por calificación de forma descendente
    val resultado = filtrados.select("nombre", "calificacion").orderBy($"calificacion".desc)

    // Retornar el DataFrame resultante
    resultado

  }

  /**Ejercicio 2: UDF (User Defined Function)
   Pregunta: Define una función que determine si un número es par o impar.
   Aplica esta función a una columna de un DataFrame que contenga una lista de números.
   */

  def ejercicio2(numeros: DataFrame)(spark:SparkSession): DataFrame =  {
    import spark.implicits._
    import org.apache.spark.sql.functions._

    // Definir una UDF para determinar si un número es par o impar
    val esParUDF = udf((num: Int) => if (num % 2 == 0) "Par" else "Impar")

    // Aplicar la UDF a la columna del DataFrame
    val resultado = numeros.withColumn("paridad", esParUDF($"numero"))

    resultado
  }
  /**Ejercicio 3: Joins y agregaciones
   Pregunta: Dado dos DataFrames,
   uno con información de estudiantes (id, nombre)
   y otro con calificaciones (id_estudiante, asignatura, calificacion),
   realiza un join entre ellos y calcula el promedio de calificaciones por estudiante.
   */
  def ejercicio3(estudiantes: DataFrame, calificaciones: DataFrame)(spark: SparkSession): DataFrame = {
    import spark.implicits._

    // Realizar el join entre los DataFrames en la columna id -> id_estudiante
    val joined = estudiantes.join(calificaciones, estudiantes("id") === calificaciones("id_estudiante"))

    // Calcular el promedio de calificaciones por estudiante
    val resultado = joined.groupBy("nombre")
      .agg(avg("calificacion").as("promedio_calificacion"))
      .orderBy($"promedio_calificacion".desc)

    resultado
  }
  /**Ejercicio 4: Uso de RDDs
   Pregunta: Crea un RDD a partir de una lista de palabras y cuenta la cantidad de ocurrencias de cada palabra.
   */
  def ejercicio4(palabras: List[String])(spark:SparkSession): RDD[(String, Int)] = {
    // Crear un RDD a partir de la lista de palabras
    val palabrasRDD: RDD[String] = spark.sparkContext.parallelize(palabras)

    // Contar las ocurrencias de cada palabra
    val conteoPalabrasRDD: RDD[(String, Int)] = palabrasRDD
      .map(palabra => (palabra, 1)) // Mapear cada palabra a un par clave-valor
      .reduceByKey(_ + _)           // Reducir sumando las ocurrencias

    conteoPalabrasRDD
  }

  /**
   Ejercicio 5: Procesamiento de archivos
   Pregunta: Carga un archivo CSV que contenga información sobre
   ventas (id_venta, id_producto, cantidad, precio_unitario)
   y calcula el ingreso total (cantidad * precio_unitario) por producto.
   */
  def ejercicio5(filePath: String)(spark: SparkSession): DataFrame = {
    import spark.implicits._
    import org.apache.spark.sql.functions._

    // Leer el archivo CSV
    val ventas = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(filePath)

    // Calcular el ingreso total por producto
    val resultado = ventas
      .withColumn("ingreso_total", $"cantidad" * $"precio_unitario") // Calcular ingreso total
      .groupBy("id_producto")                                        // Agrupar por producto
      .agg(sum("ingreso_total").as("total_ingreso"))                 // Sumar ingresos
      .orderBy($"id_producto")

    resultado
  }

}

