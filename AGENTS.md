# AGENTS.md

## Project

Build a simple, working college project:

"Machine Learning Based Smart Farming and Plant Disease Detection System"

The priority is:
1. Working functionality
2. Correct ML integration
3. End-to-end testing
4. Simple UI
5. Avoid unnecessary complexity

This is NOT a production system.

## Stack

- Frontend: React + Vite
- Backend: Java + Spring Boot
- Database: PostgreSQL
- ML service: Python + FastAPI
- Weather: OpenWeather API

## Architecture

React
→ Spring Boot
→ PostgreSQL

Spring Boot
→ FastAPI ML Service
→ ML Models

React must communicate with Spring Boot.
React must NOT directly call the ML service.

Spring Boot handles authentication, database operations, external APIs, and communication with FastAPI.

FastAPI handles ML inference only.

## Required Features

- Farmer registration/login
- JWT authentication
- FARMER role only
- Farmer profile/farm details
- Crop recommendation
- Crop yield prediction
- Plant disease detection
- Weather information
- Crop market prices
- Government schemes
- Agriculture chatbot
- Simple dashboard

Do NOT add:
- Admin panel
- Community/forum
- Expert/moderator roles
- Redis
- Kafka
- Kubernetes
- Docker
- CI/CD
- Complex microservices
- Unnecessary infrastructure

## Existing ML Models

Three model resources are already present in this repository.

Before implementing ML integration, inspect the actual files/code/notebooks and determine:

- model type
- model file
- framework/dependencies
- input features and order
- preprocessing/scalers
- output format
- image requirements for disease detection
- class labels where applicable

Do NOT guess model inputs or preprocessing.

Reuse the provided trained models whenever possible.

Do NOT retrain or replace them unless they genuinely cannot be used for inference.

Do NOT copy their original Flask/web UI. Extract only the inference logic needed by FastAPI.

## FastAPI

Create one FastAPI service for all ML models.

Use appropriate endpoints for:

- crop recommendation
- yield prediction
- disease detection

The exact request/response formats must be based on the actual models.

Disease detection should accept an image and return the predicted disease and confidence when available.

Keep treatment/prevention information as a simple maintained mapping.

## Backend

Use:

- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL

Passwords must be hashed.

Secrets/API keys must not be hardcoded or committed.

Use environment variables/local configuration for secrets.

## Weather

Use the OpenWeather API through Spring Boot.

Never expose the weather API key in React.

Keep weather functionality simple.

## Frontend

Use React + Vite.

Create simple pages/components for:

- Login
- Register
- Dashboard
- Profile
- Crop Recommendation
- Yield Prediction
- Disease Detection
- Weather
- Market Prices
- Government Schemes
- Chatbot

Prioritize usability over visual complexity.

## Development Rules

Before modifying the project:

1. Inspect the existing repository.
2. Read this file.
3. Inspect all three ML model folders.
4. Understand existing code before integrating anything.

Do not unnecessarily rewrite working code.

Do not add technologies that are not required.

When a reasonable implementation decision is needed, choose the simplest working solution.

Do not stop after creating a skeleton. Continue toward a complete working application.

## Testing

After significant implementation:

- compile/build
- run the relevant service
- test APIs
- fix errors
- continue

ML features must be tested end-to-end:

React
→ Spring Boot
→ FastAPI
→ Model
→ FastAPI
→ Spring Boot
→ React

Verify real/sample predictions for all three ML models.

## Priority

If time becomes limited, prioritize:

1. Application builds/runs
2. PostgreSQL
3. Authentication
4. Crop recommendation
5. Yield prediction
6. Disease detection
7. React ↔ Spring Boot ↔ FastAPI integration
8. Dashboard
9. Weather
10. Government schemes
11. Chatbot
12. UI polish

A simple working feature is better than an incomplete advanced feature.

## Git

Preserve working code.

Make commits at stable milestones.

Never commit API keys, passwords, or other secrets.