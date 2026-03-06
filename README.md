# 🃏 Cardflow

**Cardflow** is a deterministic, grid-based puzzle and automation game inspired by *Balatro*, *shapez.io*, and *Mindustry*. The core objective is to route, filter, and transform playing cards through a grid of modifiers to produce a specific output sequence. 

The game focuses on planning, simulation, and emergent behavior through logical composition rather than reflexes.



---

## 📌 Project Overview
* **Course:** 2110215 Programming Methodology (Semester 2, 2025)
* **Institution:** Chulalongkorn University (CEDT)
* **Genre:** Puzzle / Factory Simulation
* **Key Mechanics:** Tick-based simulation, Modulo 13 arithmetic, and Material-based interactions.

---

### [Link Report](https://docs.google.com/document/d/1gC24tA5RU6ZqhgHYP4bdYx1R-tJYspbVh3TwXkdrjw4/edit?usp=sharing)
### [Link Video Presentation](https://www.youtube.com/watch?v=YgXQ6aTmz8A)

---

## ⚙️ Core Gameplay & Mechanics

The game operates in two distinct phases:
1. **The Planning Phase:** The simulation is paused. Players analyze the incoming queue and place/rotate `Movers` on the grid to build their logic pipeline.
2. **The Simulation Phase:** Time moves in discrete "Ticks" (Moving Tick & Modifying Tick). Cards spawn, move, and get processed automatically until the required output is met or the player stops the simulation.

### 🃏 Card Properties
Every card in the game possesses three fundamental properties:
* **Suit:** The standard four suits (♠, ♥, ♦, ♣). Used for routing and filtering.
* **Value:** Ranges from 1 to 13 (J = 11, Q = 12, K = 13). Arithmetic operations wrap around using **Modulo 13** (e.g., Subtracting 1 from an Ace results in a King).
* **Material:** Determines how the card physically reacts to Modifiers on the grid.

### 🧱 Material System
Materials add a layer of resource management and physical constraints to the puzzle:

| Material | Behavior |
| :--- | :--- |
| **Plastic** | The basic material. No special effects. |
| **Glass** | Fragile. Destroyed instantly after passing through 3 modifiers. |
| **Metal** | Sturdy. Immune to Arithmetic Changers (Add, Sub, Mul, Div). |
| **Stone** | Dense. Immune to Combinators (Cannot be split, duplicated, merged, etc.). |
| **Rubber** | Bouncy. Causes all Arithmetic Changers to apply their effects **twice**. |
| **Corrupted** | Volatile. Permanently disables the next modifier it touches, then reverts to Plastic. |

### 🛠️ Tools of the Trade
Players manipulate the board using two types of components:

**1. Movers (Player-Placed)**
* `Conveyor`: Moves cards forward.
* `Flip-Flop`: Alternates output direction (Left/Right) per card.
* `Parity Filter`: Evens go straight; Odds go left.
* `Red/Black Filter`: Reds (♥, ♦) go straight; Blacks (♠, ♣) go left.
* `Delay`: Holds a card for 2 movement ticks before releasing it.

**2. Modifiers (Pre-Placed Obstacles)**
* **Changers:** Single-card modifiers like Arithmetic (`ADD`, `SUB`, `MUL`, `DIV`) and Setters (overwrites Value/Suit/Material).
* **Combinators:** Multi-card/Multi-tick logic gates like `Splitter`, `Duplicator`, `Merger`, `Absorber`, and `Vaporizer`.



---

## 💻 Tech Stack
* **Language:** Java
* **Build Tool:** Gradle
* **UI/Graphics:** Custom GUI (Java-based)

---


## Javadocs
Javadocs source is put at /javadoc you can view it there or host it as static page


## 👥 Team Members

Developed by **CEDT** First-Year Students:

| Name | Student ID |
| --- | --- |
| Nontapat Auetrongjit | 6833130321 |
| Sira Tongsima | 6833254521 |
| Piyawat Jaikla | 6833168221 |
| Chadmongkol Tangwadthana | 6833033121 |


