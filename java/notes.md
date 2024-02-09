- j'ai envie de déplacer tout le contenu de `GameRunner.doRun()` dans Game, genre `Game.play()`
  - et déplacer `notAWinner` dedans. Ce n'est pas au client de gérer le cycle de vie d'une partie.
- on ne devrait pas pouvoir avoir la possibilité de rajouter un joueur au cours de la partie -> les joueurs devraient p-e être rajoutés dans le constructeur du jeu, ou en params d'une méthode de Factory
- Cycle de vie d'un objet `Game`: une partie. On ne devrait pas pouvoir jouer plusieurs partie avec un seul objet -> on crée un nouvel objet pour chaque partie
- `Game.currentPlayer` semble servir d'index pour référencer des éléments dans des tableaux
- la logique de gestion du jeu et celle de la gestion de l'état des joueurs semble devrait être séparée
  - logique de gestion du jeu: 
    - est-ce que la partie est terminée ?
    - est-ce que le joueur va ou sort de prison ? Orchest
  - logique de gestion du joueur
    - est-ce qu'il est en prison
- si en prison, est-ce qu'on pose une question pour de vrai ?

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