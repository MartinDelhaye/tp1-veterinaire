# TP1 - Java RMI

**HAI704I — Architectures logicielles distribuées · TP 1 · 2026–2027**

## Prérequis

- JDK 17 minimum (testé avec 17.0.10 et 21.0.5)
- Aucune dépendance externe
- Aucun outil de build requis
- Projet fourni en sources nues

## Structure du projet

- `common/` : interfaces distantes et types partagés
- `server/` : implémentation du serveur
- `client/` : client RMI

## Compiler

Chaque projet produit son propre dossier de classes.

`server` et `client` ont besoin de `common` dans leur classpath.

### Script Bash

```bash
./compilation
```

## Commandes manuelles

```bash 
javac -d common/out  $(find common/src -name "*.java")
javac -cp common/out -d server/out $(find server/src -name "*.java")
javac -cp common/out -d client/out $(find client/src -name "*.java")
```

## Sous windows 
```bash 
javac -d common\out  (Get-ChildItem -Recurse -Filter *.java common\src).FullName
javac -cp common\out -d server\out (Get-ChildItem -Recurse -Filter *.java server\src).FullName
javac -cp common\out -d client\out (Get-ChildItem -Recurse -Filter *.java client\src).FullName
```

## Exécuter

Deux modes sont possibles.

### Mode embarqué / registre interne
Le serveur démarre son propre registre RMI.

```bash
./startServeur
./startClient
```

Ou manuellement :

```bash
java -cp common/out:server/out hello.server.Server --embedded
java -cp common/out:client/out hello.client.Client
```

## Mode registre externe
Lancer d’abord le registre, puis le serveur, puis le client.
```bash
cd common/out && rmiregistry &
java -cp common/out:server/out hello.server.Server
java -cp common/out:client/out hello.client.Client
```
Sous Windows, remplacer : par ; dans les classpaths.

## Scripts fournis
compilation : compile les 3 modules
startServeur : lance le serveur en mode embarqué
startClient : lance le client

## Nettoyage
Supprimer les dossiers de compilation si besoin :

```bash
rm -rf common/out server/out client/out
```