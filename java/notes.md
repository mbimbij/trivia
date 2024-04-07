# sprint
- Déployer le backend dans AWS

# backlog

- [F9] écrire un test pour vérifier mon hypothèse sur nécessité de remettre à zéro le compteur de mauvaise réponse à la fin
  du tour d'un joueur
  - si 2 mauvaises réponses consécutives -> prison
  - au prochain tour, si le joueur sort de prison et donne une mauvaise réponse, il est envoyé directement en prison,
    sans 2e chance, car son compteur n'est pas réinitialisé
- [F1] Version Web
  - Backend API - V0 : Vérifier que je peux jouer une partie complète
  - Frontend - V0: le strict minimum pour 
- [F7] NFR guidance
- [F1] [F7] Setup Prod - walking skeleton
- [F4] Setup environnements : "prod" & "dev"
- [F4] Setup CI basique
- [F4] Setup CD et utilisation des environnements
- [F2] Gestion d'identité
  - Création et gestion de compte utilisateur
  - Social login
  - Gestion des utilisateurs / joueurs non-authentifié
- [F8] [design] où et comment produire des events liés à une inner entity ?
  - les inner entities les produisent eux-mêmes
  - l'aggregate root les produit
  - problématique: conservation de l'ordre des events
- [F8] [design] comment reconstituer un aggrégat, et particulièrement des (deep) nested entities à partir d'events ?
- [F8] [design] Jouer avec différentes implémentations de "maintien de l'ordre" des events dans l'aggégat Game
- [F1] Envoi d'invitation à une partie
- [F1] pause & resume game
- [F1] grant & revoke permissions sur une partie
- [F8] property-based testing pour vérifier qu'il n'y a pas de joueurs avec un même nom
- [F8] [refacto] rollDice -> "extract class" Dice ?
- [F8] [refacto] isPair -> "extract class" Roll ?
- [F8] [refacto] isAnsweringCorrectly -> "extract class" AnswerVerifier ?

# actor goal list

- Actors
  - User / Player
  - Admin
  - Tech People
  - Biz People
  - Community Management

| Actors             | Goals                                                | Importance |
|--------------------|------------------------------------------------------|------------|
| [A] User           | [A.1] play game                                      |            |
|                    | [A.2] submit issue (bug report, improvement)         |            |
|                    | [A.3] contact support (by mail)                      |            |
|                    | [A.4] direct chat with support                       |            |
|                    | [A.5] post on forum / discord / etc.                 |            |
| [B] Support        | [B.1] submit issue (on behalf of user, etc.)         |            |
|                    | [B.2] Act on user account (ban, reset pwd.)          |            |
|                    | [B.3] Act on game (end, delete, etc.)                |            |
| [C] Tech           | [C.1] Observability                                  |            |
|                    | [C.2] Debug                                          |            |
|                    | [C.3] Delivery (feature, fix, etc.)                  |            |
| [D] Biz            | [D.1] Get usage statistics                           |            |
| [E] Community Mgmt | [E.1] (1-way) Post about events, announcements, etc. |            |
|                    | [E.2] (2-way) Interact with the community otherwise  |            |
|                    | [E.3] Moderate forum / discord / etc.                |            |

# features list
- [F1] Play Trivia
- [F2] Gestion de compte (utilisateur)
- [F3] Interaction avec les utilisateurs
  - Initiées par l'extérieur
    - contact par mail
    - contact par chat
    - post sur un / le forum
    - ouvrir un ticket dans le système de ticketing
    - post / chat discord
    - note et commentaire sur l'application
    - Modération
  - Initiées par "nous"
    - mailing list
    - forum
    - discord
    - site web
    - stream & vidéos
- [F4] Delivery
- [F5] Run
  - Observability
  - Alerting & Incident Response
- [F6] (Analyse de) Données d'utilisation
- [F7] Architecture & Cross-Functional Concerns
  - Documentation
    - Technical
      - Deployment Architecture
      - API & Contracts
    - Documentation Utilisateur
      - Règles d'utilisation du forum / discord / etc.
  - Performance
  - Security
  - Reliability & QoS
  - Utilisabilité
  - Testabilité
  - Maintenabilité
  - Other Architectural Concerns
- [F8] Interne
- [F9] Bugs

# Mapping Feature List / Actor Goals List



# difficultés rencontrées

- gestion de l'augmentation des attributs et params du constructeur de `Game`, `Players`, `Player`
  - pas réussi à résoudre ça avec une factory, comment faire mieux ?
- Design des factories et construction d'objets par défaut
- Introduction d'un mock dans l'arbo profonde d'une grappe d'objets issue d'une méthode de factory par défaut
- obtenir un code ergonomique à tester
  - setups bien plus complexes que nécessaire
- introduction de nouvelles fonctionnalités
- répartition des responsabilités entre `Game` et `Player`
  - duplication implicite de logique `answerIncorrectly` rendant mes tests pas pertinents
  - difficultés à tester
- ennui, burnout et "timeout de refacto" -> j'ai épuisé mes réserves d'idées et d'énergie mais le code est toujours en
  vrac et je n'ai pas d'idée claire: du design en cours, du design désiré, sinon de comment passer de l'un à l'autre.
  C'est un problème, c'est mon problème, et c'est encore plus un problème dans le contexte de travail d'équipe, et
  encore encore plus si on attend de moi que je le lead à terme, ou que je sois un "senior".

# breakdown

main

- créer un jeu
- ajouter des joueurs
- tant qu'il n'y a pas de gagnant:
  - lancer un dé
  - une fois sur 9 au hasard -> mauvaise réponse
  - sinon bonne réponse

---

lancer un dé

- si le joueur est en prison
  - si lancé de dé est pair
    - le joueur sort de prison
    - le joueur joue
  - sinon
    le joueur reste en prison
- sinon
  - le joueur joue

le joueur joue

- le joueur avance de `roll` cases (plateau borné à 12 cases on dirait)
- poser une question au joueur (la catégorie dépend de la case)

--- 

mauvaise réponse

- le joueur va en prison
- passer au joueur suivant

bonne réponse

- si le joueur est en prison
  - si le joueur sort de prison
    - récompenser le joueur
    - retourner si le joueur n'a pas encore gagné
  - sinon (le joueur reste en prison)
    - on passe au joueur suivant
    - retourner si le joueur n'a pas encore gagné
- sinon (le joueur n'est pas en prison)
  - récompenser le joueur
  - retourner si le joueur n'a pas encore gagné

récompenser le joueur

- "Answer was correct"
- rajouter une pièce au joueur
- "$playerName now has $coins gold coins"
- passer au joueur suivant
