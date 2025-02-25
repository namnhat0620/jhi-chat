# Messenger Clone

## Overview

This project replicates the core features of popular messaging apps, including real-time messaging, multimedia sharing, push notifications, and user authentication. It aims to provide users with a seamless messaging experience similar to leading platforms in the market.

## Features

- **Real-Time Messaging**: Send and receive messages instantly without delays.
- **Multimedia Sharing**: Share images, videos, and audio files in your conversations.
- **Push Notifications**: Receive immediate notifications for new messages, even when the app is not active.
- **User Authentication**: Secure sign-up, login, and user session management for a personalized experience.

## Technical Stack

- **Backend**: Java Spring Boot for handling the core logic, user authentication, and messaging services.
- **Frontend**: Angular for building an interactive and dynamic web interface.
- **Database**: PostgreSQL for storing user data, message histories, and metadata.
- **NoSQL Database**: MongoDB for handling media files and large-scale unstructured data.
- **Push Notifications**: Firebase Cloud Messaging (FCM) for real-time notifications.

## Installation

### Prerequisites

1. **Java 8 or later**
2. **Node.js and npm**
3. **PostgreSQL** and **MongoDB** installed locally or using Docker.
4. **Firebase Project** for push notifications setup.

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/messenger-clone.git

2. Navigate into the project directory:

```bash
cd messenger-clone

3. Backend Setup (Java Spring Boot):

- Make sure you have **JDK** installed and configured on your machine.
- Update the database credentials and Firebase configurations in `application.properties`.
- Install dependencies and build the backend:

```bash
mvn clean install
mvn spring-boot:run

4. Frontend Setup (Angular):

- Navigate to the frontend directory:

```bash
cd frontend

- Install the required npm packages:

```bash
npm install

- Start the Angular development server:

```bash
ng serve

5. Database Setup

- Set up PostgreSQL for relational data storage.
- Set up MongoDB for unstructured media storage.

6. Firebase Setup:

- Create a Firebase project and configure Cloud Messaging.
- Add the required credentials in the backend configuration for push notifications.

## Usage

- Authentication: Sign up with your email or log in if you have an existing account.
- Messaging: Start real-time conversations with friends, send text, images, and videos.
- Push Notifications: Keep your notifications on to stay updated with incoming messages.
