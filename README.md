# Openclassrooms - Projet 5 - Testez une application full-stack

1. [Installer et lancer le projet](#installer-et-lancer-le-projet)
2. [Installer la base de données](#installer-la-base-de-données)
2. [Éxecuter les tests](#exécuter-les-tests)

## Installer et lancer le projet
### Cloner le repo
Faites la commande :
``` bash
git clone git@github.com:lisebaron/oc-p5-test-app-full-stack.git
```

### Lancer le back
1. Ouvrir le projet avec un IDE tel que Intellij ou Eclipse
2. Modifier les informations concernant la base de données dans l'`application.properties`
3. Run le projet

### Lancer le front
1. Faites la commande :
```bash
npm install
```

2. Puis lancez le projet :
```bash
npm start
```

## Installer la base de données
Importez le fichier ``script.sql`` dans MySQL, avec des outils tel que phpmyadmin ou MySQL Workbench, ou encore la commande :
```sql
mysql -u username -p database_name < file.sql
```

## Exécuter les tests
Ouvrez dans votre navigateur `http://localhost:4200/`, puis créer un nouveau compte utilisateur.

### Tests Jest
Dans votre terminal, assurez-vous que vous êtes bien dans le dossier du front, et faites la commande :
```bash
npm run test
```

### Tests Cypress
Dans votre terminal, assurez-vous que vous êtes bien dans le dossier du front, et faites la commande:
```bash
npm run e2e
```
Assurez-vous également que vous n'avez pas de serveur npm actuellement en éxecution sur le port `4200`, étant donné que cette commande lancera le serveur pour vous.

### Tests JUnit
1. Allez dans votre IDE, ouvrez le dossier `src > test > java > com.openclassrooms.starterjwt`
2. Faites un clique droit dessus, puis selectionnez `'Run Tests in 'starterjwt''`