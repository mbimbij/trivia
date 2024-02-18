# sprint
- feature #3
- refacto factory de question initialization strategy et voir si je peux faire des trucs avec les sealed classes

# backlog
- property-based testing pour vérifier qu'il n'y a pas de joueurs avec un même nom

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
