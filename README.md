# IPA-UNO

The card game UNO, but all the cards are IPA (international phonetic alphabet) symbols.

Each pulmonic consonant has three features: manner of articulation, place of articulation, and whether it's voiced or not. Therefore, the rules of UNO can be modified as follows: Each turn, a player must play a card that matches at least 2 of the 3 features of the previous card played.

Similarly, each vowel has three features: height (close, close-mid, open-mid, open), backness (front, central, back), and roundedness.

This gives us two sets of cards that can be played separately. Each set of cards can also contain wildcards. The wildcard can be played on top of any card, and the player who played it names the consonant or vowel that the card represents, and play continues.

## Running tests

The repo has no root-level test command. Run backend and frontend checks from their respective directories.

### Backend

From `backend/`:

```bash
./mvnw test
```

This runs all JUnit tests under `backend/src/test/java/`. Requires Java 21.

Useful variants:

- `./mvnw test -Dtest=GameControllerTest` — run one test class
- `./mvnw test -Dtest=GameControllerTest#createsGame` — run one test method
- `./mvnw verify` — run tests plus any other Maven verify-phase checks

### Frontend

The frontend does not have a test runner configured yet. The closest checks are:

```bash
npm run lint
npm run build
```
