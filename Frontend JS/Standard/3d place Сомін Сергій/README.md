# \<dev-challenge-12-app\>

## Install the bower

Install bower
```
$ npm install -g bower
```

## Install the Polymer-CLI

Make sure you have the [Polymer CLI](https://www.npmjs.com/package/polymer-cli) installed. Then run `polymer serve` to serve your application locally.

## Install all the dependencies

```
$ bower install
```

## Viewing Your Application

```
$ polymer serve
```

## Building Your Application

```
$ polymer build
```

This will create builds of your application in the `build/` directory, optimized to be served in production. You can then serve the built versions by giving `polymer serve` a folder to serve from:

```
$ polymer serve build/default -o
```