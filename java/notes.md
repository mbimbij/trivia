# sprint
- noter les sources d'illisibilité dans mon code, potentiellement liées à la violation de la Loi de Déméter (ou autre)
  - des events purement liés au `Player` sont créés et produits dans `Game`, et d'autres dans `Player` ce qui est incohérent. Où devraient-ils être créés, il ya de bonnes raisons de le faire dans l'un ou l'autre 
- revenir au modèle de la collection interne pour stocker les events de l'aggrégat
- refacto factory de question initialization strategy et voir si je peux faire des trucs avec les sealed classes

# backlog
- property-based testing pour vérifier qu'il n'y a pas de joueurs avec un même nom
- où et comment produire des events liés à une inner entity ?
  - les inner entities les produisent eux-mêmes
  - l'aggregate root les produit
  - problématique: conservation de l'ordre des events
- comment reconstituer un aggrégat, et particulièrement des (deep) nested entities à partir d'events ?
- rollDice -> "extract class" Dice ?
- isPair -> "extract class" Roll ?
- isAnsweringCorrectly -> "extract class" AnswerVerifier ?
- écrire un test pour vérifier mon hypothèse sur nécessité de remettre à zéro le compteur de mauvaise réponse à la fun du tour d'un joueur
  - si 2 mauvaises réponses consécutives -> prison
  - au prochain tour, si le joueur sort de prison et donne une mauvaise réponse, il est envoyé directement en prison, sans 2e chance, car son compteur n'est pas réinitialisé
- Jouer avec différentes implémentations de "maintien de l'ordre" des events dans l'aggégat Game

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
- ennui, burnout et "timeout de refacto" -> j'ai épuisé mes réserves d'idées et d'énergie mais le code est toujours en vrac et je n'ai pas d'idée claire: du design en cours, du design désiré, sinon de comment passer de l'un à l'autre. C'est un problème, c'est mon problème, et c'est encore plus un problème dans le contexte de travail d'équipe, et encore encore plus si on attend de moi que je le lead à terme, ou que je sois un "senior". 

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
