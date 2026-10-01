Machine Learning Based Smart Farming and Plant Disease Detection System
An integrated smart agriculture platform that combines machine
learning, computer vision, and web technologies to support data-driven
agricultural decision-making.
Overview
The system brings multiple agricultural services into one web
application:
🌾 Crop Recommendation using a Random Forest Classifier
📈 Crop Yield Prediction using a Decision Tree Regressor
🍃 Plant Disease Detection using a CNN
🌦️ Weather Information
💰 Crop Market Prices
🏛️ Government Agriculture Schemes
🤖 Gemini-powered AI Farming Chatbot
🔐 Farmer Registration, Login, JWT Authentication, and Profile
🌐 English/Hindi farming assistance
The project is intentionally designed as a focused academic application
rather than a complex enterprise system.
Architecture
``` text
React Frontend (:5173)
        |
        v
Spring Boot REST API (:8080)
        |
        +--------------------> PostgreSQL (:1144)
        |
        +--------------------> FastAPI ML Service (:8000)
                                      |
                                      +--> Crop Recommendation
                                      |    Random Forest
                                      |
                                      +--> Yield Prediction
                                      |    Decision Tree
                                      |
                                      +--> Plant Disease Detection
                                           CNN
```
Request flow: React → Spring Boot → FastAPI → ML Model.
React does not directly call the FastAPI service.
Technology Stack
Frontend
React
Vite
JavaScript
HTML/CSS
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
ML Service
Python
FastAPI
Uvicorn
NumPy
Pandas
Scikit-learn
TensorFlow
Python Multipart
AI / External Services
Google Gemini API
OpenWeather API
Machine Learning Modules
1. Crop Recommendation
Crop selection is treated as a multi-class classification problem.
Inputs:
``` text
N, P, K, Temperature, Humidity, pH, Rainfall
```
Model:
``` text
Random Forest Classifier
```
The system supports 22 crop classes, including Rice, Maize, Cotton,
Coconut, Apple, Mango, Banana, Chickpea, Coffee and others.
2. Crop Yield Prediction
Yield prediction is treated as a supervised regression problem.
Inputs:
``` text
Year
Average Rainfall
Pesticides
Average Temperature
Area
Crop/Item
```
Preprocessing:
``` text
Numerical features → StandardScaler
Categorical features → OneHotEncoder
                     ↓
               ColumnTransformer
                     ↓
             Decision Tree Regressor
```
Output:
``` text
Predicted Yield (hg/ha)
```
Example verified output:
``` text
Albania + Maize → 36613.0 hg/ha
```
This is a model prediction, not a guaranteed real-world yield.
3. Plant Disease Detection
The disease module performs image classification.
``` text
Leaf Image
    ↓
RGB Processing
    ↓
Resize to 128 × 128
    ↓
CNN
    ↓
38 Disease Classes
    ↓
Disease + Confidence
```
The integrated model accepts `128 × 128 × 3` RGB images.
A project test successfully returned:
``` text
Grape___Leaf_blight_(Isariopsis_Leaf_Spot)
Confidence: 96.47%
```
Authentication
The application supports farmer-only authentication.
Registration includes:
Full name
Email
Password
Optional phone
FARMER role
Security:
BCrypt password hashing
JWT authentication
Protected farmer profile endpoints
Farmer profile fields include:
Full name
Email
Phone
Farm location
Farm size
Soil type
Irrigation type
AI Farming Chatbot
The chatbot uses Gemini through the backend:
``` text
React
  ↓
POST /api/chatbot
  ↓
Spring Boot
  ↓
Gemini API
```
The Gemini API key is stored only in the local ignored file:
``` text
backend/.env
```
Example:
``` env
GEMINI_API_KEY=your-key-here
GEMINI_MODEL=gemini-2.0-flash
```
Never commit real API keys or passwords to GitHub.
The chatbot is designed for agriculture-focused questions and supports
English/Hindi interaction and follow-up context.
Weather
Weather requests are handled by Spring Boot using the OpenWeather API.
The API key remains server-side.
``` env
OPENWEATHER_API_KEY=your-key-here
```
Market Prices and Government Schemes
The application includes:
Agricultural market-price information
Government agriculture scheme information
Scheme benefits and eligibility/application details
These services complement the machine-learning decision-support modules.
Project Structure
``` text
smart farming/
│
├── AGENTS.md
├── .gitignore
├── backend/
│   ├── src/
│   ├── .env
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   └── package.json
│
├── ml-service/
│   ├── app/
│   │   ├── main.py
│   │   ├── services/
│   │   ├── schemas/
│   │   └── models/
│   └── requirements.txt
│
├── Crop-Recommendation/
├── Crop-Yields-Prediction/
└── Plant_Disease_Prediction/
```
Local Setup
Prerequisites
Install:
Git
Java
Maven
Node.js and npm
Python
PostgreSQL
Current local ports:
``` text
PostgreSQL  → 1144
FastAPI     → 8000
Spring Boot → 8080
React       → 5173
```
1. PostgreSQL
Check the configured port:
``` powershell
netstat -ano | findstr :1144
```
You should see `LISTENING`.
The project database is:
``` text
Database: smart_farming
Host: localhost
Port: 1144
User: postgres
```
2. FastAPI / ML Service
``` powershell
cd "C:\Users\devan\Downloads\smart farming\ml-service"
python -m uvicorn app.main:app --reload --port 8000
```
FastAPI documentation:
``` text
http://localhost:8000/docs
```
Keep this terminal open.
3. Spring Boot
Open another terminal:
``` powershell
cd "C:\Users\devan\Downloads\smart farming\backend"
mvn spring-boot:run
```
Backend:
``` text
http://localhost:8080
```
Keep this terminal open.
4. React
Open a third terminal:
``` powershell
cd "C:\Users\devan\Downloads\smart farming\frontend"
npm run dev
```
Open:
``` text
http://localhost:5173
```
Environment Variables
Use the local ignored file:
``` text
backend/.env
```
Typical configuration:
``` env
SPRING_PROFILES_ACTIVE=postgres
DB_URL=jdbc:postgresql://localhost:1144/smart_farming
DB_USERNAME=postgres
DB_PASSWORD=YOUR_POSTGRES_PASSWORD

JWT_SECRET=YOUR_JWT_SECRET

OPENWEATHER_API_KEY=YOUR_OPENWEATHER_KEY

GEMINI_API_KEY=YOUR_GEMINI_KEY
GEMINI_MODEL=gemini-2.0-flash
```
Never commit the real `.env` file.
Testing
The major application flows have been tested, including:
Farmer registration
Duplicate registration handling
Login and invalid-login handling
JWT-protected profile access
Profile update and PostgreSQL persistence
Crop recommendation
Yield prediction
Plant disease detection
Weather
Market prices
Government schemes
Hindi/English chatbot
CORS
Maven build
React production build
Example ML results:
``` text
Crop:
Training sample → Rice / Maize / Mothbeans

Yield:
Albania + Maize → 36613.0 hg/ha
Albania + Rice, paddy → 23333.0 hg/ha
Albania + Wheat → 30197.0 hg/ha
```
Unsupported yield categories return validation errors instead of
fabricated predictions.
Limitations
Model performance depends on the quality and representativeness of
the training datasets.
Real-world leaf images can differ because of lighting, backgrounds,
camera quality, and leaf orientation.
Actual crop yield depends on factors not completely represented by
the model inputs.
Disease detection is limited to the supported 38 classes.
Weather and Gemini features require network access and valid API
credentials.
Model predictions are decision-support outputs and are not
guaranteed real-world outcomes.
The serialized scikit-learn models were created with an earlier
scikit-learn version, so matching the training version is preferable
for reproducibility.
Future Scope
Possible extensions include:
IoT soil and weather sensors
Mobile application
Satellite/remote-sensing integration
Larger and more diverse disease datasets
Additional crops and disease classes
Personalized recommendations using historical farmer data
Improved model evaluation and optimization
Additional real-time agricultural data sources
Research Paper
The project was also developed into an IEEE-format research paper:
"An Integrated Machine-Learning Framework for Crop Recommendation,
Yield Forecasting, and Plant Disease Classification in Smart
Agriculture"
The paper describes the integrated architecture, ML methodology,
implementation, and experimental evaluation.
Team
Project: Machine Learning Based Smart Farming and Plant Disease
Detection System
Member            University Roll No.
---
Gaurav Singh      2300290100111
Devansh Gaur      2300290100100
Dhirendra Singh   2300290100102
Supervisor: Mr. Harsh Modi
Department: Computer Science and Engineering  
Institution: KIET Group of Institutions, Delhi-NCR, Ghaziabad
Academic Project
This project was developed as an academic major project demonstrating
the integration of machine learning, computer vision, AI services, web
development, authentication, databases, and agricultural
decision-support functionality in a single platform.
