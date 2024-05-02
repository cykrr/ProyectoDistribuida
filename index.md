# Proyecto Computación paralela y distribuida.

## Guía de uso

### Levantamiento de la Base de Datos

La base de datos a utilizar será MySQL por medio del kit XAMPP.
```
winget install "XAMPP 8.2"
```

Luego ejecutar la herramienta e inicializar tanto Apache como MySQL.

### Levantamiento de la API

Para ejecutar la API se debe instalar flask.

```
pip install -r requirements.txt
```

Luego:

```
.\run_api_dev.cmd
```

### Levantamiento del servidor

Para levantar el servidor ejecute el archivo `src/Server/RunServidor.java`

### Cliente

...