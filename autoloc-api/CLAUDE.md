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
- `service` : interface `I<Entite>Service` + `<Entite>ServiceImpl` (`@Service`, `@RequiredArgsConstructor`).
- `web.controller` : `@RestController` sous `/api/...`, qui appelle le service (jamais le repository).
- `web.dto` : vide pour l'instant (prévu Séance 9).

## Conventions du code existant
- Entités : `@Entity @Table @Getter @Setter @NoArgsConstructor @AllArgsConstructor`, jamais `@Data`. Id `Long` avec `GenerationType.IDENTITY`.
- Erreurs métier : `ResponseStatusException` (404 introuvable, 409 conflit, 400 requête invalide).
- Côté inverse des associations : `@JsonIgnore` pour éviter les boucles JSON.
- Opérations qui modifient plusieurs entités : `@Transactional`.

## Modèle (associations)
- Agence 1→N Vehicule, Agence 1→N Employe (LAZY, sans cascade de suppression sur Vehicule).
- Vehicule N↔N Equipement : table `vehicule_equipement`, **Vehicule est le côté propriétaire**.
- Client 1→N Reservation (cascade PERSIST).
- Reservation N→1 Client, N→1 Vehicule, 1→1 Contrat.
- Contrat 1→N Paiement (cascade ALL). Maintenance N→1 Vehicule (cascade PERSIST).
- `Contrat.valide` est un `Boolean` (et non `boolean`).

## État d'avancement
Fait : Séances 2, 3, 5-6, 7 (entités, associations, repositories, services, contrôleurs, Swagger, règles métier de réservation/contrat/maintenance).
Étape en cours : **Séance 8, affectation / désaffectation** (voir ci-dessous).
À faire ensuite : DTO + validation + `@RestControllerAdvice` (Séance 9), requêtes JPQL et pagination (10-11), tarification avec le patron Strategy, tâches planifiées `@Scheduled`, AOP (12), tests et SonarQube (13).

## Tâche en cours : étape 1A, affectation d'un équipement à un véhicule
1. `Vehicule` : ajouter les méthodes d'aide `addEquipement(Equipement)` et `removeEquipement(Equipement)` qui mettent à jour les DEUX listes (`equipements` et `equipement.getVehicules()`).
2. `IVehiculeService` + `VehiculeServiceImpl` : ajouter `findEquipements(Long)`, `affecterEquipement(Long, Long)`, `desaffecterEquipement(Long, Long)`, avec `@Transactional` (pas de `save()`, dirty checking). Injecter `IEquipementRepository`.
    - Équipement déjà affecté : 409. Équipement non affecté lors du retrait : 404. Équipement ou véhicule introuvable : 404.
3. `VehiculeController` : `GET /api/vehicules/{id}/equipements`, `PUT /api/vehicules/{id}/equipements/{idEquipement}`, `DELETE` au même chemin.
4. Ensuite (partie B) : changer un véhicule d'agence et affecter un employé à une agence.