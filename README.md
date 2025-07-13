# 2048 - Kotlin Multiplatform Implementation

A modern implementation of the classic 2048 game built with Kotlin Multiplatform, supporting both desktop and web platforms.

![2048 Game](https://upload.wikimedia.org/wikipedia/commons/thumb/1/18/2048_logo.svg/220px-2048_logo.svg.png)

## Project Overview

This project is a complete implementation of the 2048 game using Kotlin Multiplatform. It features:

- Cross-platform core game logic
- Desktop UI using Compose for Desktop
- Web UI using Compose for Web
- Reactive architecture with Kotlin Coroutines and StateFlow

## Technologies Used

- **Kotlin Multiplatform**: For sharing code between platforms
- **Compose for Desktop**: For the desktop UI
- **Compose for Web**: For the web UI
- **Kotlin Coroutines**: For asynchronous programming
- **Gradle**: For build automation

## How to Build and Run

### Prerequisites

- JDK 17 or higher
- Gradle 8.0 or higher (or use the included Gradle wrapper)

### Desktop Application

To run the desktop application:

```bash
./gradlew run
```

### Web Application

To run the web application:

```bash
./gradlew jsBrowserDevelopmentRun
```

This will start a development server and open the application in your default browser.

To build for production:

```bash
./gradlew jsBrowserProductionWebpack
```

The output will be in `build/distributions`.

## Game Rules

### Objective
The goal of 2048 is to slide numbered tiles on a 4x4 grid to combine them into a tile with the number 2048.

### How to Play

1. **Tile Movement**: Use arrow keys (desktop) or on-screen buttons (web) to move all tiles in one direction.
2. **Combining Tiles**: When two tiles with the same number touch, they merge into one tile with the sum of their values.
3. **New Tile Appearance**: After each move, a new tile (2 or 4) appears in a random empty cell.
4. **Game Over**: The game ends when there are no more empty cells and no possible merges.
5. **Winning**: You win when you create a tile with the value 2048.

### Controls

#### Desktop
- **Arrow Keys**: Move tiles
- **New Game Button**: Reset the game

#### Web
- **Arrow Keys**: Move tiles (on desktop)
- **On-screen Buttons**: Move tiles (on mobile)
- **New Game Button**: Reset the game

## Project Structure

```
src/
├── commonMain/       # Shared code
│   └── kotlin/
│       └── com/game2048/
│           ├── Game.kt           # Core game logic
│           └── GameViewModel.kt   # Shared ViewModel
│
├── jvmMain/          # Desktop-specific code
│   └── kotlin/
│       └── com/game2048/
│           └── Main.kt           # Desktop UI
│
└── jsMain/           # Web-specific code
    ├── kotlin/
    │   └── com/game2048/
    │       └── Main.kt           # Web UI
    └── resources/
        └── index.html            # HTML entry point
```

## Strategies for Success

1. **Build High-Value Tiles in a Corner**: Keep your highest tiles in one corner.
2. **Keep the Highest Tile in a Corner**: Avoid moving your highest tile away from its corner.
3. **Make Merges in the Same Direction**: Try to move in the same direction repeatedly.
4. **Plan Your Moves Ahead**: Think several steps ahead.
5. **Don't Get Greedy**: Focus on building up high-value tiles strategically.
6. **Use the Edges and Corners Wisely**: Store tiles along edges to keep the board organized.

## Troubleshooting

### Missing Gradle Wrapper JAR

If you encounter the following error when trying to run Gradle commands:

```
Error: Could not find or load main class org.gradle.wrapper.GradleWrapperMain
Caused by: java.lang.ClassNotFoundException: org.gradle.wrapper.GradleWrapperMain
```

This means the `gradle-wrapper.jar` file is missing from the `gradle/wrapper` directory. You can fix this by running the following PowerShell script to download it:

```powershell
# PowerShell script to download the gradle-wrapper.jar file
$url = "https://github.com/gradle/gradle/raw/v8.2.1/gradle/wrapper/gradle-wrapper.jar"
$output = "gradle\wrapper\gradle-wrapper.jar"

Write-Host "Downloading gradle-wrapper.jar from $url"
Invoke-WebRequest -Uri $url -OutFile $output
Write-Host "Downloaded gradle-wrapper.jar to $output"
```

Save this as `download-gradle-wrapper.ps1` and run it with:

```powershell
powershell -ExecutionPolicy Bypass -File download-gradle-wrapper.ps1
```

After downloading the JAR file, you should be able to run Gradle commands normally.

## License

This project is open source and available under the MIT License.
