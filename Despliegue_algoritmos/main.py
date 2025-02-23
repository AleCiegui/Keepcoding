import funciones as func

def main():
    print("Entramos en el main")
    args_values = func.argumentos()
    df = func.load_dataset()
    x_train, x_test, y_train, y_test = func.data_treatment(df)
    func.mlflow_tracking(args_values.nombre_job, x_train, x_test, y_train, y_test, args_values.n_estimators_list)

if __name__ == "__main__":
    main()