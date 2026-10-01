🌾 Smart Farming & Plant Disease Detection System

A full-stack smart agriculture web application that helps farmers make better decisions using Machine Learning, Computer Vision, and AI.

The platform provides crop recommendations, yield prediction, plant disease detection, weather information, market prices, government schemes, and an AI farming chatbot — all in one place.

✨ Features

🔐 Farmer Authentication

Register and login

JWT-based authentication

Secure password hashing with BCrypt

👨‍🌾 Farmer Profile

Farm location

Farm size

Soil type

Irrigation type

Personal details

🌱 Crop Recommendation

Uses soil and weather-related inputs

Recommends a suitable crop using Machine Learning

📈 Crop Yield Prediction

Predicts expected crop yield

Uses agricultural and environmental parameters

🍃 Plant Disease Detection

Upload a plant leaf image

Detects supported plant diseases

Shows prediction confidence and basic guidance

🌦️ Weather

Current agricultural weather information

Powered by OpenWeather API

💰 Market Prices

Agricultural crop price information

🏛️ Government Schemes

Agriculture-related government schemes

Benefits and eligibility information

🤖 AI Farming Chatbot

Agriculture-focused chatbot

Powered by Google Gemini

Supports English and Hindi

Maintains conversation context

📊 Modern Dashboard

Clean and responsive UI

Easy access to all major features

🏗️ Project Architecture

                    ┌─────────────────────┐
                    │   React Frontend    │
                    │       :5173         │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │ Spring Boot Backend │
                    │       :8080         │
                    └──────┬───────┬──────┘
                           │       │
                ┌──────────┘       └──────────┐
                ▼                             ▼
       ┌─────────────────┐          ┌─────────────────┐
       │   PostgreSQL    │          │ FastAPI ML      │
       │     :1144       │          │     :8000       │
       └─────────────────┘          └────────┬────────┘
                                             │
                              ┌──────────────┼──────────────┐
                              ▼              ▼              ▼
                           Crop ML        Yield ML      Disease CNN

Request Flow

React → Spring Boot → FastAPI → ML Model

The React frontend communicates with Spring Boot. The frontend does not directly call the FastAPI ML service.

🛠️ Tech Stack

Frontend

React

Vite

JavaScript

HTML5

CSS3

Backend

Java

Spring Boot

Spring Security

Spring Data JPA

REST APIs

JWT

BCrypt

Database

PostgreSQL

Machine Learning

Python

FastAPI

Uvicorn

Scikit-learn

TensorFlow

NumPy

Pandas

AI & APIs

Google Gemini API

OpenWeather API

🤖 Machine Learning

Crop Recommendation

Algorithm: Random Forest Classifier

Inputs:

Nitrogen
Phosphorus
Potassium
Temperature
Humidity
pH
Rainfall

The model recommends a suitable crop based on the provided soil and climate conditions.

Crop Yield Prediction

Algorithm: Decision Tree Regressor

Inputs include:

Year
Rainfall
Pesticide usage
Average temperature
Area
Crop type

Output:

Predicted Yield (hg/ha)

Plant Disease Detection

Algorithm: Convolutional Neural Network (CNN)

Pipeline:

Leaf Image
    ↓
Image Preprocessing
    ↓
128 × 128 RGB Image
    ↓
CNN Model
    ↓
Disease Class + Confidence

The current model supports 38 disease classes.

📁 Project Structure

smart farming/
│
├── backend/                    # Spring Boot backend
│   ├── src/
│   ├── .env
│   └── pom.xml
│
├── frontend/                   # React frontend
│   ├── src/
│   └── package.json
│
├── ml-service/                 # FastAPI ML service
│   ├── app/
│   │   ├── main.py
│   │   ├── services/
│   │   ├── schemas/
│   │   └── models/
│   └── requirements.txt
│
├── Crop-Recommendation/        # Crop ML resources
├── Crop-Yields-Prediction/     # Yield ML resources
├── Plant_Disease_Prediction/   # Disease ML resources
│
├── AGENTS.md
└── .gitignore

⚙️ Requirements

Make sure you have:

Git

Java

Maven

Node.js

npm

Python

PostgreSQL

🚀 Run the Project

The application uses three main processes.

1. Start PostgreSQL

PostgreSQL runs as a Windows service.

Check whether the project database port is active:

netstat -ano | findstr :1144

The project uses:

Host: 127.0.0.1
Port: 1144
Database: smart_farming
User: postgres

2. Start ML Service

Open a terminal:

cd "C:\Users\devan\Downloads\smart farming\ml-service"
python -m uvicorn app.main:app --reload --port 8000

FastAPI documentation:

http://localhost:8000/docs

Keep this terminal running.

3. Start Spring Boot

Open another terminal:

cd "C:\Users\devan\Downloads\smart farming\backend"
mvn spring-boot:run

Backend:

http://localhost:8080

Keep this terminal running.

4. Start React

Open another terminal:

cd "C:\Users\devan\Downloads\smart farming\frontend"
npm run dev

Open:

http://localhost:5173

🔑 Environment Variables

Create a local:

backend/.env

Example:

SPRING_PROFILES_ACTIVE=postgres

DB_URL=jdbc:postgresql://localhost:1144/smart_farming
DB_USERNAME=postgres
DB_PASSWORD=YOUR_POSTGRES_PASSWORD

JWT_SECRET=YOUR_JWT_SECRET

OPENWEATHER_API_KEY=YOUR_OPENWEATHER_KEY

GEMINI_API_KEY=YOUR_GEMINI_KEY
GEMINI_MODEL=gemini-2.0-flash

⚠️ Never commit your real .env file, passwords, or API keys to GitHub.

🔌 Main Services

Service

Port

React Frontend

5173

Spring Boot

8080

FastAPI ML

8000

PostgreSQL

1144

🧪 Testing

The main application flows have been tested, including:

Farmer registration and login

JWT authentication

Farmer profile

Crop recommendation

Crop yield prediction

Plant disease detection

Weather

Market prices

Government schemes

Gemini chatbot

Hindi/English chatbot interaction

Frontend production build

Spring Boot build

FastAPI integration

Example verified ML outputs include crop recommendations such as Rice, Maize, and Mothbeans, and yield predictions such as 36613.0 hg/ha for a tested Albania/Maize input.

⚠️ Notes

ML predictions depend on the training data and input quality.

Plant disease detection works for the disease classes supported by the trained model.

Weather and Gemini features require valid API keys and internet access.

The yield value returned by the model is a prediction and not a guaranteed real-world yield.

Keep all three application terminals running while using the complete local application.

🔮 Future Improvements

Possible future additions:

📱 Mobile application

🌡️ IoT soil and weather sensors

🛰️ Satellite/remote sensing

📍 Location-based agricultural recommendations

📊 More detailed analytics

🌱 More crops and disease classes

👨‍🌾 Personalized farmer recommendations

☁️ Cloud deployment

