import json, os
from flask import Flask, request, g

script_path = os.path.realpath(__file__).strip("app.py")
def open_products():
    filename = f"{script_path}/products.json"
    with open(filename, "r", encoding='utf-8') as file:
        g.products = json.load(file)


def read_products():
    if 'products' not in g:
        open_products()
    
app = Flask(__name__)

@app.route('/products', methods=['GET'])
def get_product():
    read_products()
    id = request.args.get('id', type=str)
    if not id:
        return {"error": "El parámetro id es obligatorio"}, 400
    if id not in g.products:
        return {"error": "El producto no existe"}, 404

    return g.products[id], 200
        
    
