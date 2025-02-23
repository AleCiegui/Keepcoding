from fastapi import FastAPI
from pydantic import BaseModel
from typing import Optional
import pandas as pd 
from transformers import pipeline 


app = FastAPI()

class Identity(BaseModel): 
    name: str 
    surname: Optional[str] = None

@app.get('/saluda')
def root(name: str): 
    return {'Message': f'Hola Soy {name}!'}

@app.post('/pydanticTest')
def testing(id: Identity): 
    if id.surname is None: 
        message = f'Bienvenido {id.name}  al modulo del bootcamp!'
    else: 
        message = f'Bienvenido {id.name} {id.surname}'
    return {"message": message}

@app.post('/calculadora')
def calcladora(num1: float, num2: float, operador):
    print("Operaciones disponibles: +, -")
    if operador == "+":
        resultado = num1 + num2
    elif operador == "-":
        resultado = num1 - num2
    return {"Resultado": resultado}
    

@app.get('/classification')
def text_classification(query): 
    pipe = pipeline("text-classification")
    return pipe(query)[0]['label']

@app.get('/traductor_en_to_fr')
def traductor(query): 
    translator = pipeline("translation_en_to_fr")
    return translator(query)

