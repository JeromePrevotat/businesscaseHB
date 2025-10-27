Stage build = Node (bullseye recommandé pour Angular).

Stage run = Nginx (alpine).

Environnements = fichiers src/environments/* + angular.json (fileReplacements).

Choix de l’environnement = --configuration=xxx (production, preprod…).

Pipeline CI/CD = passe un --build-arg au docker build pour activer le bon environnement.