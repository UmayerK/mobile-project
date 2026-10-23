# Mini Games (Android)

An Android app (Java) with a hub screen of card tiles that launch small, self-contained games. Built as a mobile development course project.

## Games

| Game | What it does |
|---|---|
| Snake | Custom `View` (`SnakeGameView`) drawing a 20×20 grid; swipe to steer, eat food to grow, score shown live |
| Tic Tac Toe | Two players on a 3×3 grid with win/draw detection |
| Number Guess | Guess a random number from 1–100 with too-high/too-low hints and an attempt counter |
| Reflex Test | Wait for the button to turn green, tap, and see your reaction time in ms plus your best time |
| Quick Math | Answer random +, −, × questions against a 30-second `CountDownTimer`; final score shown at the end |

The hub (`MainActivity`) and manifest also reference Memory Match, Whack-a-Mole and Color Match, but those activities are not in this repository yet, so they need to be added (or their cards removed) before the project builds.

## Tech

Java, Android SDK (minSdk 24, target/compile SDK 33), AndroidX AppCompat, Material Components, ConstraintLayout, CardView, Gradle.

## Build

Open the project in Android Studio and run the `app` configuration, or:

```bash
./gradlew assembleDebug
```

Source is in `app/src/main/java/com/example/assignment1/`, layouts in `app/src/main/res/layout/`.
