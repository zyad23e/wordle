# Wordle Web — Full-Stack Application

A full-stack Wordle-style game built with a **Spring Boot backend** and a **React (Vite) frontend**.  
This project began as a simple college assignment, but was later **rebuilt and significantly extended** into a complete web application with a real UI, backend-enforced game logic, user accounts, and persistent statistics.

The backend is the source of truth for gameplay rules, dictionary validation, daily puzzle selection, and stat tracking. The frontend focuses on clean interaction, keyboard-driven gameplay, and presenting results to the user.

---

## Project Structure

---

## Features

- **Daily Wordle gameplay**
  - 5-letter words, 6 attempts
  - Deterministic daily puzzle shared by all users
  - Duplicate-letter handling matches official Wordle rules

- **Backend-enforced game logic**
  - Guess validation and evaluation handled server-side
  - Invalid dictionary words rejected by the API
  - Server determines win/loss and game completion

- **User authentication**
  - Register, login, logout
  - Session-based authentication using Spring Security
  - Cookie-based sessions with credentialed API requests

- **Persistent player statistics**
  - Games played and wins
  - Current and max win streak
  - Win distribution (1–6 guesses)
  - Stats update once per daily puzzle to prevent duplicates

---

## Tech Stack

**Backend**
- Java
- Spring Boot (Web, Security, Data JPA)
- Hibernate
- MySQL
- Maven

**Frontend**
- React
- Vite
- JavaScript
- CSS

---

## Local Development

### Prerequisites
- Java (project specifies Java 25 in `pom.xml`)
- Node.js
- MySQL
