# FW - Final War

The arcade game based on concept is to clean galaxy from enemies. 
You are pilot of the spaceship the best in the World.
The goal is to survive.
The game consists of 5 levels, 5 types of enemies and 5 elements which you should collect.
Enjoy the game:) 

## Technical description

The main stack of technologies is Angular 6.0 + Three.js. 

NodeJS & NPM 
```
    "node": "8.9.4",
    "npm": "5.6.0"
```

## Run application

Install all dependencies `npm i`. Than run `npm start` for the dev server.
Navigate to `http://localhost:4200/fw`.

For production usage see PWA section.

## Build

Run `npm run build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Documentation

Run `npm run doc:build` for generating project documentation. All files are stored in the `documentation/` directory 

## Linting

Run `npm run lint` for checking code style guide.

## PWA

Run `npm run build` than go to `dist/` directory and run `http-server -p 8080`.
Navigate to `http://localhost:8080/fw`

Check have you already installed `http-server`, if not install it `npm i -g http-server`.
