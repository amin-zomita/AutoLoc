# AutoLoc : contexte du projet

Projet d'étude (ESPRIT, ASI 26-27) : API REST de location de véhicules multi-agences.
Stack : Java 17, Spring Boot 4.1.1, Maven, Spring Data JPA, MySQL (XAMPP, base `autoloc_db`), Lombok, springdoc-openapi (Swagger UI : http://localhost:8081/swagger-ui/index.html).
Port : 8081. Package racine : `tn.esprit.autoloc`.

## Règles de travail (importantes)
- Avancer étape par étape : expliquer la notion, proposer la modification, attendre mon accord avant de passer à la suite.
- Ne rien modifier sans me l'avoir expliqué avant. Ne pas réécrire du code qui fonctionne.
- Expliquer en français, de façon pédagogique : je suis étudiant et je dois pouvoir justifier chaque choix en cours.
- Ne jamais écrire de mot de passe dans le dépôt (`spring.datasource.password=${DB_PASSWORD:}`).
- Ne pas faire de `git commit` ni de `git push` sans que je le demande.

## Architecture (en couches)
- `domain` : 9 entités JPA + 5 enums (diagramme de l'énoncé).
- `repository` : interfaces `I<Entite>Repository extends JpaRepository`.
- `service` : interface `I<Entite>Service` + `<Entite>ServiceImpl` (`@Service`, `@RequiredArgsConstructor`). Sous-packages `validation` (`VehiculeValidator`) et `tarification` (patron Strategy).
- `web.controller` : `@RestController` sous `/api/...`, qui appelle le service (jamais le repository).
- `web.dto` : vide pour l'instant (prévu Séance 9).

## Conventions du code existant
- Entités : `@Entity @Table @Getter @Setter @NoArgsConstructor @AllArgsConstructor`, jamais `@Data`. Id `Long` avec `GenerationType.IDENTITY`.
- Erreurs métier : `ResponseStatusException` (404 introuvable, 409 conflit, 400 requête invalide).
- Côté inverse des associations : `@JsonIgnore` pour éviter les boucles JSON.
- Opérations qui modifient plusieurs entités : `@Transactional`, toujours `org.springframework.transaction.annotation.Transactional` (jamais `jakarta.transaction`), sans `save()` inutile (dirty checking).
- L'agence d'un véhicule ou d'un employé se change uniquement via les endpoints d'affectation (`/api/agences/{id}/vehicules/...`, `/api/agences/{id}/employes/...`) : `update()` n'y touche pas.

## Modèle (associations)
- Agence 1→N Vehicule, Agence 1→N Employe (LAZY, sans cascade de suppression sur Vehicule).
- Vehicule N↔N Equipement : table `vehicule_equipement`, **Vehicule est le côté propriétaire**.
- Client 1→N Reservation (cascade PERSIST).
- Reservation N→1 Client, N→1 Vehicule, 1→1 Contrat.
- Contrat 1→N Paiement (cascade ALL). Maintenance N→1 Vehicule (cascade PERSIST).
- `Contrat.valide` est un `Boolean` (et non `boolean`).

## État d'avancement
Fait :
- Séances 2, 3, 5-6, 7 : entités, associations, repositories, services, contrôleurs, Swagger, règles métier réservation/contrat/maintenance.
- Séance 8 : affectations Equipement↔Vehicule et Vehicule/Employe↔Agence (transfert entre agences, 409 si déjà affecté).
- Atelier GoF/SOLID : SRP (`VehiculeValidator`, `JournalisationService`) ; Strategy + OCP (`service.tarification`, `GET /api/tarification?idVehicule=&type=&jours=`, types STANDARD, WEEKEND, LONGUE_DUREE, ENTREPRISE).
- Corrections de cohérence : `@Transactional` de Spring partout ; `update()` ne touche plus l'agence ; `PUT /api/maintenances/{id}/terminer` (en cours ⇔ `dateFin` null, 400 si `dateFin` fournie à la création, véhicule remis DISPONIBLE s'il n'a plus de maintenance en cours, recalcul aussi au `delete`).

À faire, dans l'ordre :
1. Séance 9 : DTO, Bean Validation, `@RestControllerAdvice` ; le PUT véhicule ne doit plus modifier le statut.
2. Relier la tarification au montant du contrat.
3. Séances 10-11 : JPQL, pagination, recherche de véhicules disponibles, `@Scheduled`.
4. Séance 12 : AOP (remplacera `JournalisationService`).
5. Séance 13 : tests, SonarQube.

Branches : `atelier-gof-solid` et `corrections-coherence` sont fusionnées dans `main`. Séance 9 en cours sur `seance-9-dto-validation`, partie de `main`.
