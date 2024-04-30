import json, os
from flask import Flask, request

script_path = os.path.realpath(__file__).strip("api.py")


def read_products():
    filename = f"{script_path}/products.json"
    with open(filename, "r", encoding='utf-8') as file:
        return json.load(file)
    
app = Flask(__name__)

@app.route('/products', methods=['GET'])
def get_product():
    id = request.args.get('id', type=str)
    if not id:
        return {"error": "El parámetro id es obligatorio"}, 400
    if id not in products:
        return {"error": "El producto no existe"}, 404

    return products[id], 200
        
    
if __name__ == '__main__':
    products = read_products()
    app.run(port=5000)