package com.game2048

import androidx.compose.runtime.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlinx.browser.window
import org.w3c.dom.events.KeyboardEvent

fun main() {
    val viewModel = GameViewModel()

    window.addEventListener("keydown", { event ->
        val keyEvent = event as KeyboardEvent
        when (keyEvent.key) {
            "ArrowUp" -> viewModel.move(Direction.UP)
            "ArrowDown" -> viewModel.move(Direction.DOWN)
            "ArrowLeft" -> viewModel.move(Direction.LEFT)
            "ArrowRight" -> viewModel.move(Direction.RIGHT)
            else -> {}
        }
        event.preventDefault()
    })

    renderComposable(rootElementId = "root") {
        val gameState by viewModel.gameState.collectAsState()

        Div(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                    alignItems(AlignItems.Center)
                    maxWidth(500.px)
                    property("margin", "16px auto")
                    fontFamily("Arial", "sans-serif")
                }
            }
        ) {
            // Header with title and score
            Div(
                attrs = {
                    style {
                        display(DisplayStyle.Flex)
                        justifyContent(JustifyContent.SpaceBetween)
                        alignItems(AlignItems.Center)
                        width(100.percent)
                        marginBottom(16.px)
                    }
                }
            ) {
                H1(
                    attrs = {
                        style {
                            fontSize(48.px)
                            fontWeight("bold")
                            color(Color("#776E65"))
                            margin(0.px)
                        }
                    }
                ) {
                    Text("2048")
                }

                Div(
                    attrs = {
                        style {
                            display(DisplayStyle.Flex)
                            flexDirection(FlexDirection.Column)
                            alignItems(AlignItems.End)
                        }
                    }
                ) {
                    // Score display
                    Div(
                        attrs = {
                            style {
                                backgroundColor(Color("#BBADA0"))
                                borderRadius(4.px)
                                padding(8.px, 16.px)
                                textAlign("center")
                                marginBottom(8.px)
                            }
                        }
                    ) {
                        Div(
                            attrs = {
                                style {
                                    fontSize(12.px)
                                    color(Color("#EEE4DA"))
                                }
                            }
                        ) {
                            Text("SCORE")
                        }
                        Div(
                            attrs = {
                                style {
                                    fontSize(20.px)
                                    fontWeight("bold")
                                    color(Color("white"))
                                }
                            }
                        ) {
                            Text("${gameState.score}")
                        }
                    }

                    // New Game button
                    Button(
                        attrs = {
                            style {
                                backgroundColor(Color("#8F7A66"))
                                color(Color("white"))
                                border(0.px)
                                borderRadius(4.px)
                                padding(8.px, 16.px)
                                fontSize(14.px)
                                fontWeight("bold")
                                cursor("pointer")
                            }
                            onClick { viewModel.resetGame() }
                        }
                    ) {
                        Text("New Game")
                    }
                }
            }

            // Game instructions
            P(
                attrs = {
                    style {
                        fontSize(18.px)
                        color(Color("#776E65"))
                        textAlign("center")
                        margin(0.px, 0.px, 8.px)
                    }
                }
            ) {
                Text("Join the tiles, get to 2048!")
            }

            P(
                attrs = {
                    style {
                        fontSize(14.px)
                        color(Color("#776E65"))
                        textAlign("center")
                        margin(0.px, 0.px, 16.px)
                    }
                }
            ) {
                Text("Use arrow keys to move the tiles.")
            }

            // Game grid
            Div(
                attrs = {
                    style {
                        backgroundColor(Color("#BBADA0"))
                        borderRadius(8.px)
                        padding(8.px)
                        width(100.percent)
                        property("aspect-ratio", "1")
                        display(DisplayStyle.Grid)
                        property("grid-template-columns", "repeat(4, 1fr)")
                        property("grid-template-rows", "repeat(4, 1fr)")
                        gap(8.px)
                    }
                }
            ) {
                for (i in gameState.grid.indices) {
                    for (j in gameState.grid[i].indices) {
                        val value = gameState.grid[i][j]

                        // Determine tile color based on value
                        val backgroundColor = when (value) {
                            0 -> "#CDC1B4"
                            2 -> "#EEE4DA"
                            4 -> "#EDE0C8"
                            8 -> "#F2B179"
                            16 -> "#F59563"
                            32 -> "#F67C5F"
                            64 -> "#F65E3B"
                            128 -> "#EDCF72"
                            256 -> "#EDCC61"
                            512 -> "#EDC850"
                            1024 -> "#EDC53F"
                            2048 -> "#EDC22E"
                            else -> "#3C3A32" // For values > 2048
                        }

                        val textColor = if (value <= 4) "#776E65" else "white"
                        val fontSize = when (value) {
                            in 0..64 -> 24
                            in 128..512 -> 20
                            else -> 18
                        }

                        Div(
                            attrs = {
                                style {
                                    backgroundColor(Color(backgroundColor))
                                    borderRadius(4.px)
                                    display(DisplayStyle.Flex)
                                    justifyContent(JustifyContent.Center)
                                    alignItems(AlignItems.Center)
                                    width(100.percent)
                                    height(100.percent)
                                }
                            }
                        ) {
                            if (value != 0) {
                                Span(
                                    attrs = {
                                        style {
                                            fontSize(fontSize.px)
                                            fontWeight("bold")
                                            color(Color(textColor))
                                        }
                                    }
                                ) {
                                    Text("$value")
                                }
                            }
                        }
                    }
                }
            }

            // Game over message
            if (gameState.gameOver) {
                Div(
                    attrs = {
                        style {
                            position(Position.Fixed)
                            top(0.px)
                            left(0.px)
                            width(100.percent)
                            height(100.percent)
                            backgroundColor(Color("rgba(255, 255, 255, 0.8)"))
                            display(DisplayStyle.Flex)
                            justifyContent(JustifyContent.Center)
                            alignItems(AlignItems.Center)
                            property("z-index", "10")
                        }
                    }
                ) {
                    Div(
                        attrs = {
                            style {
                                backgroundColor(Color("white"))
                                borderRadius(8.px)
                                padding(24.px)
                                textAlign("center")
                                property("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)")
                            }
                        }
                    ) {
                        H2(
                            attrs = {
                                style {
                                    fontSize(32.px)
                                    fontWeight("bold")
                                    color(Color(if (gameState.won) "#EDC22E" else "#776E65"))
                                    margin(0.px, 0.px, 16.px)
                                }
                            }
                        ) {
                            Text(if (gameState.won) "You Won!" else "Game Over!")
                        }

                        Button(
                            attrs = {
                                style {
                                    backgroundColor(Color("#8F7A66"))
                                    color(Color("white"))
                                    border(0.px)
                                    borderRadius(4.px)
                                    padding(8.px, 16.px)
                                    fontSize(16.px)
                                    fontWeight("bold")
                                    cursor("pointer")
                                }
                                onClick { viewModel.resetGame() }
                            }
                        ) {
                            Text("Try Again")
                        }
                    }
                }
            }

            // Touch controls for mobile
            Div(
                attrs = {
                    style {
                        display(DisplayStyle.Grid)
                        property("grid-template-columns", "repeat(3, 1fr)")
                        property("grid-template-rows", "repeat(2, 1fr)")
                        gap(8.px)
                        width(200.px)
                        marginTop(24.px)
                    }
                }
            ) {
                // Empty cell (top-left)
                Div {}

                // Up button
                Button(
                    attrs = {
                        style {
                            backgroundColor(Color("#8F7A66"))
                            color(Color("white"))
                            border(0.px)
                            borderRadius(4.px)
                            padding(8.px)
                            fontSize(16.px)
                            fontWeight("bold")
                            cursor("pointer")
                        }
                        onClick { viewModel.move(Direction.UP) }
                    }
                ) {
                    Text("↑")
                }

                // Empty cell (top-right)
                Div {}

                // Left button
                Button(
                    attrs = {
                        style {
                            backgroundColor(Color("#8F7A66"))
                            color(Color("white"))
                            border(0.px)
                            borderRadius(4.px)
                            padding(8.px)
                            fontSize(16.px)
                            fontWeight("bold")
                            cursor("pointer")
                        }
                        onClick { viewModel.move(Direction.LEFT) }
                    }
                ) {
                    Text("←")
                }

                // Down button
                Button(
                    attrs = {
                        style {
                            backgroundColor(Color("#8F7A66"))
                            color(Color("white"))
                            border(0.px)
                            borderRadius(4.px)
                            padding(8.px)
                            fontSize(16.px)
                            fontWeight("bold")
                            cursor("pointer")
                        }
                        onClick { viewModel.move(Direction.DOWN) }
                    }
                ) {
                    Text("↓")
                }

                // Right button
                Button(
                    attrs = {
                        style {
                            backgroundColor(Color("#8F7A66"))
                            color(Color("white"))
                            border(0.px)
                            borderRadius(4.px)
                            padding(8.px)
                            fontSize(16.px)
                            fontWeight("bold")
                            cursor("pointer")
                        }
                        onClick { viewModel.move(Direction.RIGHT) }
                    }
                ) {
                    Text("→")
                }
            }
        }
    }
}
