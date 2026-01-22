# Full-Stack Wordle Web Application

A full-stack Wordle-style web application built with a Java Spring Boot backend and a lightweight React frontend.  
The project emphasizes backend-driven game logic, secure API design, and deterministic state handling, with the frontend acting as a thin client that renders server-authoritative results.

This application is a **ground-up rebuild** of a prior academic Wordle project, redesigned to demonstrate improved architecture, clarity, and full-stack ownership.

---

## Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Security
- MySQL
- JPA / Hibernate
- RESTful API design
- File-based word dictionaries

### Frontend
- React (Vite)
- Functional components with hooks
- Keyboard and UI-driven input
- Server-authoritative game state

---

## Features

- Daily Wordle-style puzzle with deterministic answer rotation
- Secure backend endpoints enforced with Spring Security
- Guess validation against a dictionary word list
- Server-side evaluation of guesses (correct / present / absent)
- React-based 6×5 grid rendering driven entirely by API responses
- Development-only endpoint for answer inspection

---

## Backend API Overview

### `GET /api/puzzle`
Returns metadata for the current daily puzzle.

### `POST /api/guess`
Submits a guess and returns per-letter evaluation results.

**Request**
```json
{
  "guess": "CRANE"
}
