# Introduction #

Java DataBase Connectivity (JDBC) est une spécification Java permettant l'inter action
d'un programme Java avec un Système de Gestion de Base de Données (SGBD).

## L'API ##

L'API JDBC normalise les classes suivantes afin d'interagir avec une base de données relationnelle.

### java.sql.DriverManager ###

Le [DriverManager](http://docs.oracle.com/javase/6/docs/api/java/sql/DriverManager.html) est la classe utilitaire permettant d'obtenir une connexion avec une base de données au moyen des méthodes `getConnection(...)`.

Une URL de connexion est nécessaire afin de localiser le serveur, la base de données, ... Cette URL, semblable aux URL utilisées sur internet pour localiser les pages, est spécifique au type de la base de données, elle commence toujours par `jdbc:....`,.

Exemple pour MySQL:

`jdbc:mysql://[host][,failoverhost...][:port]/[database]`


### java.sql.Connection ###

Une [Connection](http://docs.oracle.com/javase/6/docs/api/java/sql/Connection.html) représente une sorte de session en cours avec la base de données. Cette session est le point de départ de toutes interaction avec la base de données.

### java.sql.Statement ###

Un [Statement](http://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html) représente le début d'une interaction avec la base de données. Il sont de 3 types :

<dl>
<dt><a href='http://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html'>java.sql.Statement</a></dt>
<dd>Un requête simple avec la base de données</dd>
<dt><a href='http://docs.oracle.com/javase/6/docs/api/java/sql/CallableStatement.html'>java.sql.CallableStatement</a></dt>
<dd>Un requête effectuées au moyen d'une procédure stockée</dd>
<dt><a href='http://docs.oracle.com/javase/6/docs/api/java/sql/PreparedStatement.html'>java.sql.PreparedStatement</a></dt>
<dd>Un requête complexe à compiler sur le SGBD avant d'en fournir les paramètre pendant l'exécution</dd>
</dl>

### java.sql.ResultSet ###

Un [ResultSet](http://docs.oracle.com/javase/6/docs/api/java/sql/ResultSet.html) représente un curseur permettant d'accéder aux resultats d'une requête exécutée avec un Statement.

Les résultats sont lisibles ...
  * ... ligne après ligne avec la méthode `#next()`
  * ... colonne après colonne avec les méthodes `#getXxx(int)` et `#getXxx(java.lang.String)` où Xxx représente le type de l'information retournée (int, double, String, ...)

## Les Drivers ##

JDBC est une norme que les fournisseurs de bases de données doivent respecter pour permettre l'utilisation de leurs systèmes par des programmes Java (et donc des applications JEE).

### Pour MySQL ###

MySQL fournit [par ici](http://dev.mysql.com/doc/index-connectors.html) une liste de connecteur pour différentes technologie, y compris Java.

### H2 ###

[H2](http://www.h2database.com/) est un SGBD libre et OpenSource totalement Java. Le jar comprend le moteur ainsi que le driver.

### JavaDB ###

Depuis la version 1.6, le JDK fournit par Sun (maintenant Oracle) l'est conjointement à une base de données dénommée JavaDB. Il s'agit d'une version de la base de données Java [Derby](http://db.apache.org/derby/) maintenue par la communauté de la fondation Apache.

### Et les autres ###

Tous les grands nom fournissent également des connecteurs:
  * Oracle
  * Microsoft SQL Server
  * IBM DB2
  * ...

# Mise en œuvre #

## Créer une base de données ##

Téléchargez l'archive sur [le site de H2](http://www.h2database.com/).
Décompressez le contenu de l'archive, `<H2_HOME>` désigne maintenant le chemin complet vers le dossier h2 décompressé précédemment.

### Démarrer le serveur ###

Lancer l'exécutable `<H2_HOME>/bin/h2.sh`

Dans l'interface Web qui s'affiche, choisir dans la liste `Generic H2 (Server)`

Sans rien changer aux informations pré renseignées, appuyez sur le bouton Connecter.

## Créer une table ##

### La Structure ###

Pour créer une table, saisissez puis exécutez le code suivant :
```
CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR(255));
```
Ces instructions créerons une table `TEST` ayant 2 colonnes, `ID` de type entier et `NAME` contenant des chaines de caractères, 255 au maximum.

### Le contenu ###

Pour insérer le contenu dans la table précédemment créée, saisissez puis exécutez le code suivant :
```
INSERT INTO TEST VALUES(1, 'Hello');
INSERT INTO TEST VALUES(2, 'World');
```

## Pleine puissance JDBC! ##

Le code suivant permet de se connecter à la base précédemment créée:
```
Class.forName("org.h2.Driver");
String url = "jdbc:h2:tcp://localhost/~/test";
String username = "sa";
String password = "";
connection = DriverManager.getConnection(url, username, password);
Statement statement = connection.createStatement();
String query = "SELECT ID, NAME FROM TEST";
ResultSet results = statement.executeQuery(query);
while (results.next()) {
  int id = results.getInt("ID");
  String name = results.getString("NAME");
  System.out.printf("%d - %s%n", id, name);
}
results.close();
statement.close();
connection.close();
```