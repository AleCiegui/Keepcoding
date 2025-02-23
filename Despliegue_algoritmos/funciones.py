import argparse
import subprocess
import pandas as pd
import mlflow
import time
import mlflow.sklearn
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.datasets import load_wine

def argumentos():
  print("aaa")
  parser= argparse.ArgumentParser(description='__main__de la aplicacion con argumentod de entrada.')
  parser.add_argument('--nombre_job', type=str, help='Valor para el parametro nombre_documento')
  parser.add_argument('--n_estimators_list', nargs='+', type=int, help='List of n_estimators values.')
  return parser.parse_args()

def load_dataset():
  wine = load_wine()
  df = pd.DataFrame(wine['data'], columns=wine['feature_names'])
  df['target'] = wine['target']
  return df

def data_treatment(df):
  x_train, x_test, y_train, y_test = train_test_split(df.drop(columns="target"), df["target"],
                                                      test_size=0.2,
                                                      random_state=42,
                                                      stratify=df["target"])
  return x_train, x_test, y_train, y_test

def mlflow_tracking(nombre_job, x_train, x_test, y_train, y_test, n_estimators):
  
  time.sleep(5)  
  mlflow.set_experiment(nombre_job)
  for i in n_estimators:
    with mlflow.start_run() as run:
      clf = RandomForestClassifier(n_estimators=i,
                                  min_samples_leaf=2,
                                  class_weight='balanced',
                                  random_state=123)

      preprocessor = Pipeline(steps=[('scaler', StandardScaler())])

      model = Pipeline(steps=[('preprocessor', preprocessor),
                                ('RandomForestClassifier', clf)])
      model.fit(x_train, y_train)
      accuracy_train = model.score(x_train, y_train)
      model.score(x_test, y_test)

      mlflow.log_metric('accuraty_train', accuracy_train)
      mlflow.log_param('n_stimators', i)
      mlflow.sklearn.log_model(model, 'clf-modellll')
  print("Se ha acabado de entrenar el modelo correctamente! \n")