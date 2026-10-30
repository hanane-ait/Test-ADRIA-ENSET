# ADRIA Banking Platform

## 1. Présentation du projet

**ADRIA Banking Platform** est une plateforme bancaire basée sur une architecture microservices. Elle permet de gérer des comptes bancaires, d'effectuer et de consulter des transactions, et de préparer l'intégration d'un assistant conversationnel intelligent.

Le projet est réalisé dans le cadre du test technique de sélection ADRIA – ENSET.

Les objectifs principaux sont :
- Développer des microservices indépendants.
- Exposer les fonctionnalités bancaires via des API REST et GraphQL.
- Mettre en place la communication entre microservices.
- Utiliser Spring Cloud pour la découverte et la configuration des services.
- Intégrer un assistant intelligent basé sur Spring AI et MCP.
- Préparer une interface utilisateur moderne.

## 2. Architecture du projet

Le projet est organisé autour des modules suivants :

```text
Test-ADRIA-ENSET/
│
├── account-service1/
│   └── Gestion des comptes bancaires
│
├── transaction-service/
│   └── Gestion des transactions bancaires
│
├── discovery-service/
│   └── Découverte des microservices avec Eureka
│
├── config-service/
│   └── Configuration centralisée
│
├── chatbot-service/
│   └── Assistant conversationnel avec Spring AI
│
├── README.md
└── pom.xml
```

## 3. Technologies utilisées

| Technologie | Utilisation |
|---|---|
| Java 21 | Langage de programmation |
| Spring Boot | Développement des microservices |
| Spring Cloud | Infrastructure distribuée |
| Spring Data JPA | Persistance des données |
| H2 Database | Base de données de développement |
| Spring GraphQL | API GraphQL |
| OpenFeign | Communication entre microservices |
| Eureka | Découverte des services |
| Spring Cloud Config | Configuration centralisée |
| Resilience4J | Tolérance aux pannes |
| Spring AI | Intégration de l'intelligence artificielle |
| MCP | Exposition et utilisation d'outils par l'assistant |
| Ollama | Exécution locale du modèle LLM |
| Maven | Gestion des dépendances et compilation |
| React / Vite | Interface frontend |

## 4. Microservices développés

### 4.1 Account-Service

**Port : 8081**

Ce microservice assure la gestion des comptes bancaires.

Fonctionnalités :
- Création d'un compte bancaire.
- Consultation d'un compte par identifiant.
- Consultation de la liste des comptes.
- Consultation du solde.
- Modification du statut d'un compte.
- Crédit et débit d'un compte.
- Exposition des fonctionnalités via REST et GraphQL.
- Préparation des outils MCP pour l'assistant intelligent.

Structure principale :

```text
account-service1/
└── src/main/java/ma/enset/adria/accountservice1/
    ├── Entities/
    ├── DTO/
    ├── Repository/
    ├── Service/
    ├── Controllers/
    ├── AItools/
    └── Exception/
```

### 4.2 Transaction-Service

**Port : 8082**

Ce microservice assure la gestion des transactions bancaires.

Les principales fonctionnalités prévues et développées comprennent :
- Création et consultation des transactions.
- Association d'une transaction à un compte bancaire.
- Vérification du compte et de son statut.
- Vérification de la devise et du montant.
- Consultation du solde avant une transaction.
- Mise à jour du solde après l'opération.
- Gestion des transferts entre comptes.
- Communication avec Account-Service à travers OpenFeign.
- Préparation des mécanismes de tolérance aux pannes avec Resilience4J.
- Exposition des fonctionnalités via REST, GraphQL et MCP.

Structure principale :

```text
transaction-service/
└── src/main/java/ma/enset/adria/transactionservice/
    ├── Entities/
    ├── DTO/
    ├── Repository/
    ├── Service/
    ├── Controllers/
    ├── Exception/
    ├── AItools/
    └── Client/
```

### 4.3 Discovery-Service

**Port : 8761**

Ce service utilise Netflix Eureka pour la découverte dynamique des microservices.

Il permet :
- L'enregistrement des microservices.
- La découverte des services disponibles.
- Le suivi de leur état.

Interface Eureka :

http://localhost:8761

### 4.4 Config-Service

**Port : 8888**

Ce service est destiné à centraliser les configurations des microservices.

Il permet de partager les propriétés communes, notamment les paramètres métier comme la limite maximale autorisée pour une transaction.

Exemple :

```properties
bank.transaction.max-amount=1000
```

### 4.5 Chatbot-Service

**Port : 8083**

Ce microservice est consacré à l'assistant conversationnel bancaire.

Technologies prévues :
- Spring AI pour la communication avec le modèle de langage.
- Ollama pour exécuter un modèle local.
- MCP Client pour accéder aux outils exposés par les autres microservices.

Fonctionnalités prévues :
- Répondre aux questions des utilisateurs.
- Consulter les informations des comptes.
- Consulter les soldes.
- Rechercher et expliquer les transactions.
- Exploiter les outils MCP d'Account-Service et Transaction-Service.

Endpoint REST prévu :

```http
POST /api/assistant/chat
```

Exemple de requête :

```json
{
  "message": "Donne-moi la liste de mes comptes."
}
```

Exemple de réponse :

```json
{
  "reply": "Voici les informations disponibles sur vos comptes..."
}
```

L'intégration complète du modèle IA et des connexions MCP reste à vérifier par des tests d'exécution.

## 5. Configuration des ports

| Service | Port |
|---|---:|
| Discovery-Service | 8761 |
| Config-Service | 8888 |
| Account-Service | 8081 |
| Transaction-Service | 8082 |
| Chatbot-Service | 8083 |

## 6. Installation et démarrage

### Prérequis

- JDK 21
- Maven
- IntelliJ IDEA ou un autre IDE Java
- Ollama (pour l'assistant IA)
- Node.js et npm (pour le frontend)

### Étape 1 : Cloner le projet

```bash
git clone <URL_DU_DEPOT_GITHUB>
cd Test-ADRIA-ENSET
```

### Étape 2 : Compiler le projet

Depuis le répertoire racine :

```bash
mvn clean install
```

### Étape 3 : Démarrer les microservices

Respecter l'ordre de démarrage suivant :

1. Discovery-Service.
2. Config-Service.
3. Account-Service.
4. Transaction-Service.
5. Chatbot-Service.

### Étape 4 : Démarrer Ollama

Vérifier les modèles disponibles :

```bash
ollama list
```

Télécharger le modèle si nécessaire :

```bash
ollama pull llama3.2:3b
```

Lancer le modèle :

```bash
ollama run llama3.2:3b
```

## 7. Tests des API

Les API peuvent être testées avec Postman.

### Account-Service

Créer un compte :

```http
POST http://localhost:8081/api/accounts
```

Consulter tous les comptes :

```http
GET http://localhost:8081/api/accounts
```

Consulter un compte :

```http
GET http://localhost:8081/api/accounts/{id}
```

### Transaction-Service

Les endpoints de transaction sont exposés par Transaction-Service sur le port 8082. Consulter son contrôleur REST pour les routes exactes.

### Chatbot-Service

```http
POST http://localhost:8083/api/assistant/chat
Content-Type: application/json
```

```json
{
  "message": "Bonjour, présente-toi."
}
```

## 8. Interface utilisateur

Une interface frontend React avec Vite est prévue pour permettre aux utilisateurs d'interagir avec la plateforme.

Les écrans prévus comprennent :
- Tableau de bord.
- Gestion des comptes.
- Historique des transactions.
- Assistant conversationnel.
- Écran de connexion.

L'interface doit communiquer avec les API backend. L'authentification Keycloak et la connexion réelle du chatbot devront être intégrées lorsque les services correspondants seront opérationnels.

## 9. Améliorations et fonctionnalités restantes

Les prochaines étapes du projet sont :

- Finaliser et tester les connexions MCP.
- Valider l'intégration Spring AI avec Ollama.
- Finaliser la communication via API Gateway.
- Finaliser la publication et la consommation des événements Kafka.
- Intégrer l'authentification et les
