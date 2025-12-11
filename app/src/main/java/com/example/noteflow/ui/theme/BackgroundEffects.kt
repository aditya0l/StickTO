package com.example.noteflow.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * God-level animated gradient background with floating particles
 */
@Composable
fun AnimatedGradientBackground(
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetX"
    )
    
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = colors,
                    center = Offset(offsetX, offsetY)
                )
            )
    )
}

/**
 * Floating particles background effect
 */
@Composable
fun FloatingParticlesBackground(
    particleColor: Color = Color.White.copy(alpha = 0.3f),
    particleCount: Int = 30,
    modifier: Modifier = Modifier
) {
    val particles = remember {
        List(particleCount) { Particle() }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(50000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            particle.update(time)
            drawCircle(
                color = particleColor,
                radius = particle.radius,
                center = Offset(
                    particle.x * size.width,
                    particle.y * size.height
                ),
                alpha = particle.alpha
            )
        }
    }
}

/**
 * Mesh gradient background with animated waves
 */
@Composable
fun MeshGradientBackground(
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mesh")
    
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        // Draw multiple layers of waves
        for (i in 0 until 3) {
            val path = Path().apply {
                moveTo(0f, height * (0.3f + i * 0.2f))
                
                var x = 0f
                while (x <= width) {
                    val y = height * (0.3f + i * 0.2f) + 
                           sin(phase + x / 100f + i) * 50f
                    lineTo(x, y)
                    x += 10f
                }
                
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            
            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colors[i % colors.size].copy(alpha = 0.3f),
                        Color.Transparent
                    )
                )
            )
        }
    }
}

/**
 * Geometric shapes floating background
 */
@Composable
fun GeometricShapesBackground(
    shapeColor: Color = Color.White.copy(alpha = 0.1f),
    modifier: Modifier = Modifier
) {
    val shapes = remember {
        List(15) { GeometricShape() }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "shapes")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        shapes.forEach { shape ->
            shape.update(rotation)
            drawGeometricShape(shape, shapeColor, size.width, size.height)
        }
    }
}

/**
 * Aurora borealis effect background
 */
@Composable
fun AuroraBackground(
    colors: List<Color> = listOf(
        Color(0xFF667EEA),
        Color(0xFFF093FB),
        Color(0xFF4FACFE),
        Color(0xFF00F2C3)
    ),
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aurora")
    
    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset1"
    )
    
    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset2"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        // First wave
        val path1 = createWavePath(width, height, offset1, 0.3f)
        drawPath(
            path = path1,
            brush = Brush.verticalGradient(
                colors = listOf(colors[0].copy(alpha = 0.4f), Color.Transparent),
                startY = 0f,
                endY = height
            )
        )
        
        // Second wave
        val path2 = createWavePath(width, height, offset2, 0.5f)
        drawPath(
            path = path2,
            brush = Brush.verticalGradient(
                colors = listOf(colors[1].copy(alpha = 0.3f), Color.Transparent),
                startY = 0f,
                endY = height
            )
        )
        
        // Third wave
        val path3 = createWavePath(width, height, 1f - offset1, 0.7f)
        drawPath(
            path = path3,
            brush = Brush.verticalGradient(
                colors = listOf(colors[2].copy(alpha = 0.2f), Color.Transparent),
                startY = 0f,
                endY = height
            )
        )
    }
}

/**
 * Bubble background effect
 */
@Composable
fun BubblesBackground(
    bubbleColor: Color = Color.White.copy(alpha = 0.15f),
    bubbleCount: Int = 20,
    modifier: Modifier = Modifier
) {
    val bubbles = remember {
        List(bubbleCount) { Bubble() }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "bubbles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(50000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        bubbles.forEach { bubble ->
            bubble.update(time, size.height)
            drawCircle(
                color = bubbleColor,
                radius = bubble.radius,
                center = Offset(
                    bubble.x * size.width,
                    bubble.y
                ),
                alpha = bubble.alpha
            )
        }
    }
}

// Helper classes
private data class Particle(
    var x: Float = Random.nextFloat(),
    var y: Float = Random.nextFloat(),
    val radius: Float = Random.nextFloat() * 3f + 2f,
    val speedX: Float = (Random.nextFloat() - 0.5f) * 0.0002f,
    val speedY: Float = (Random.nextFloat() - 0.5f) * 0.0002f,
    val alpha: Float = Random.nextFloat() * 0.5f + 0.2f
) {
    fun update(time: Float) {
        x += speedX * time
        y += speedY * time
        
        // Wrap around
        if (x > 1f) x = 0f
        if (x < 0f) x = 1f
        if (y > 1f) y = 0f
        if (y < 0f) y = 1f
    }
}

private data class GeometricShape(
    var x: Float = Random.nextFloat(),
    var y: Float = Random.nextFloat(),
    val size: Float = Random.nextFloat() * 100f + 50f,
    val rotationSpeed: Float = Random.nextFloat() * 0.5f + 0.1f,
    var rotation: Float = Random.nextFloat() * 360f,
    val shapeType: Int = Random.nextInt(3) // 0: square, 1: triangle, 2: circle
) {
    fun update(globalRotation: Float) {
        rotation = (globalRotation * rotationSpeed) % 360f
    }
}

private data class Bubble(
    var x: Float = Random.nextFloat(),
    var y: Float = Random.nextFloat() * 1000f + 1000f,
    val radius: Float = Random.nextFloat() * 30f + 10f,
    val speed: Float = Random.nextFloat() * 2f + 1f,
    val alpha: Float = Random.nextFloat() * 0.3f + 0.1f,
    val wobble: Float = Random.nextFloat() * 0.02f
) {
    fun update(time: Float, screenHeight: Float) {
        y -= speed
        x += sin(time * wobble) * 0.5f
        
        // Reset when bubble goes off screen
        if (y < -radius) {
            y = screenHeight + radius
            x = Random.nextFloat()
        }
        
        // Wrap x
        if (x > 1f) x = 0f
        if (x < 0f) x = 1f
    }
}

// Helper functions
private fun DrawScope.drawGeometricShape(
    shape: GeometricShape,
    color: Color,
    width: Float,
    height: Float
) {
    val centerX = shape.x * width
    val centerY = shape.y * height
    
    when (shape.shapeType) {
        0 -> { // Square
            drawRect(
                color = color,
                topLeft = Offset(centerX - shape.size / 2, centerY - shape.size / 2),
                size = androidx.compose.ui.geometry.Size(shape.size, shape.size)
            )
        }
        1 -> { // Triangle
            val path = Path().apply {
                moveTo(centerX, centerY - shape.size / 2)
                lineTo(centerX - shape.size / 2, centerY + shape.size / 2)
                lineTo(centerX + shape.size / 2, centerY + shape.size / 2)
                close()
            }
            drawPath(path, color)
        }
        2 -> { // Circle
            drawCircle(
                color = color,
                radius = shape.size / 2,
                center = Offset(centerX, centerY)
            )
        }
    }
}

private fun createWavePath(width: Float, height: Float, offset: Float, baseHeight: Float): Path {
    return Path().apply {
        moveTo(0f, height * baseHeight)
        
        var x = 0f
        while (x <= width) {
            val y = height * baseHeight + sin((x / width) * 4 * PI.toFloat() + offset * 2 * PI.toFloat()) * 100f
            lineTo(x, y)
            x += 10f
        }
        
        lineTo(width, height)
        lineTo(0f, height)
        close()
    }
}

