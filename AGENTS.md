# IPA-UNO

Read README.md for basic information about the project.

## Architecture

The repo is split into two independently built parts:

- `frontend/` — Vite + React + TypeScript single-page app. Presentation and input only.
- `backend/` — Java 21 + Spring Boot 4 service, built with Maven. Owns the authoritative game state and rules.

All game rules live in the backend: deck construction, shuffling, feature matching (a card must share at least 2 of its 3 features with the previous card), turn validation, and wildcard resolution. The frontend renders whatever state it is given and sends player actions; it never decides whether a move is legal.

Hands are hidden information, so the backend sends each player a redacted view of the game state in which other players' hands are reduced to card counts. DO NOT send full game state to a client.

Run npm commands from `frontend/` and `./mvnw` from `backend/`; there is no package manifest at the repo root.

Backend web tests use MockMvc from `spring-boot-starter-webmvc-test`, which is a separate dependency from `spring-boot-starter-test`. In Spring Boot 4 these annotations live in `org.springframework.boot.webmvc.test.autoconfigure`, not the pre-4.x `org.springframework.boot.test.autoconfigure.web.servlet` that most examples online still use.

## DOs and DON'Ts

- DO write tests for backend production code. DO use test-driven development (TDD) when applicable, especially when writing backend logic code. Write the tests, run them, see them fail (failure to build doesn't count), then implement the production code and run the tests. Continue to fix the production code until the tests pass.
- DO write small, targeted commits that are easy to review. DO separate the frontend changes from the backend changes. DO NOT write one large end-to-end commit that both add UI elements and implement a complicated backend feature (one that requires unit tests).
- DO NOT commit changes on behalf of the user. Leave them uncommitted in the working tree for the user to review.
- DO run the Java formatter (backend/mvnw formatter:format) after changing code in the backend.
