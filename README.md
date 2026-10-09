# TP1 - Java RMI : cabinet vétérinaire

**HAI704I — Architectures logicielles distribuées · TP 1 · 2026–2027**
Delhaye Martin — 22607812 — parcours GL

- Dépôt Git : https://github.com/MartinDelhaye/tp1-veterinaire

## 1. Environnement

- **JDK** : Java 17 minimum
- **Développement** : Visual Studio Code
- **Système** : Windows sous WSL
- **Port** : 1099
- **Mode de registre retenu** : interne (créé dans la JVM du serveur, option `--embedded`). Le mode externe (`rmiregistry`) fonctionne aussi

## 2. Le projet
Application distribuée en Java RMI natif : un cabinet vétérinaire dont les patients vivent dans la JVM du serveur et sont manipulés à distance par des clients en ligne de commande.

### Structure

```
common/   le CONTRAT partagé : interfaces distantes (Cabinet, Patient, PatientRecord,
          CabinetObserver) et types transmis par valeur (Species, Chien, Observation,
          AlerteSeuil, exceptions métier)
server/   les SERVANTS (CabinetImpl, PatientImpl, PatientRecordImpl) et le lanceur Server
client/   le client : Client, ClientService, ClientUI, CabinetObserverImpl, Color
```

`server` et `client` dépendent de `common` ; **`client` ne référence aucune classe de `server`**.

### Organisation du client (trois responsabilités)
| Classe | Rôle |
|---|---|
| `Client` | point d'entrée : lit les arguments, obtient le stub du cabinet |
| `ClientService` | logique client : opérations sur les interfaces distantes, traduction des erreurs réseau en `ClientException` |
| `ClientUI` | menu, lecture et validation des saisies, affichage |

`CabinetObserverImpl` est l'objet exporté par le client pour recevoir les alertes du serveur.

## 3. Compiler

Depuis la racine du dépôt :

```bash
./compilation
```

Le script compile `common`, puis `server` et `client` (qui ont besoin de `common` sur leur classpath).
Commandes équivalentes :

```bash
javac -d common/out  $(find common/src -name "*.java")
javac -cp common/out -d server/out $(find server/src -name "*.java")
javac -cp common/out -d client/out $(find client/src -name "*.java")
```

Sous Windows (PowerShell) :

```powershell
javac -d common\out  (Get-ChildItem -Recurse -Filter *.java common\src).FullName
javac -cp common\out -d server\out (Get-ChildItem -Recurse -Filter *.java server\src).FullName
javac -cp common\out -d client\out (Get-ChildItem -Recurse -Filter *.java client\src).FullName
```

Nettoyage : 
`./clean` ou `rm -rf common/out server/out client/out`

## 4. Exécuter

### Ordre de démarrage

1. (mode externe uniquement) le registre
2. le serveur
3. les clients (**Possible de lancer plusieurs clients pour les 2 méthodes**)


### Mode interne — registre dans le serveur

```bash
# terminal 1
./startServeurInterne

# terminal 2
./startClient
```

Commandes équivalentes :

```bash
# terminal 1
java -cp common/out:server/out vet.server.Server --embedded

# terminal 2
java -cp common/out:client/out vet.client.Client [hote] [port]
```

### Mode externe — `rmiregistry` séparé

```bash
# terminal 1
./startServeurExtern

# terminal 2
./startClient
```

Commandes équivalentes :

```bash
# terminal 1
(cd common/out && rmiregistry &)
java -cp common/out:server/out vet.server.Server

# terminal 2
java -cp common/out:client/out vet.client.Client
```
_Sous Windows, remplacer `:` par `;` dans les classpaths (et utiliser `start rmiregistry` depuis `common\out`)._



### Arguments du client

`./startClient [hote] [port]` — par défaut `localhost` et `1099`

### Scripts fournis
_Testé sous WSL_. Pour donner les droit aux scripts : 
```bash
chmod 777 [nomDuScript]
```
| Script | Rôle |
|---|---|
| `compilation` | compile les trois projets |
| `clean` | Nettoyer les projets |
| `startServeurIntern` | lance le serveur en mode interne (`--embedded`) |
| `startServeurExtern` | lance le registre externe puis le serveur |
| `startClient` | lance le client CLI (transmet ses arguments) |

## 5. Utiliser la CLI

Au démarrage, le client affiche son menu :

```
1. Consulter la liste des patients
2. Consulter les informations d'un patient   (ouvre ensuite le dossier de suivi)
3. Créer un patient
4. S'abonner / se désabonner des alertes du cabinet
5. Quitter
6. [demonstration] Créer 100 patients
```

Dans le dossier d'un patient : modifier l'état de santé, voir l'historique des observations,
ajouter une observation, retour.

**Conventions de saisie**

- Les menus se pilotent en tapant le **numéro** de l'option puis Entrée.
- Les noms et textes ne peuvent pas être vides ; les entiers (durée de vie) sont vérifiés.
  Une saisie invalide affiche un message en rouge et la question est reposée.
- Le **nom d'un patient est son identifiant** : créer deux patients de même nom est refusé.
- Les alertes (`[ALERTE] Seuil N franchi (HAUSSE)`, en jaune) s'affichent dès réception,
  seuils 100, 500 et 1000.

**Si le serveur est arrêté**, toute opération affiche « Serveur injoignable » sans fermer le client.

**Arrêt propre** : option `5` (le client se désabonne des alertes puis libère son objet exporté,
le processus se termine). `Ctrl+D` fait de même. Le serveur s'arrête avec `Ctrl+C`.