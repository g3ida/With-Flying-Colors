package com.g3ida.withflyingcolours.utils.extensions

import com.badlogic.gdx.math.Vector2
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

val Float.Companion.EPSILON: Float
    get() = 0.001f

val Double.Companion.EPSILON: Double
    get() = 0.001

val Float.Companion.PI: Float
    get() = 3.14159265359f

val Double.Companion.PI: Double
    get() = 3.14159265359

val Float.Companion.PI2: Float
    get() = 1.57079632679f

val Double.Companion.PI2: Double
    get() = 1.57079632679

val Float.Companion.PI4: Float
    get() = 0.78539816339f

val Double.Companion.PI4: Double
    get() = 0.78539816339

val Float.Companion.PI3: Float
    get() = 0.52359877559f

val Double.Companion.PI3: Double
    get() = 0.52359877559

val Float.isAlmostZero: Boolean
    get() = abs(this) < Float.EPSILON

val Double.isAlmostZero: Boolean
    get() = abs(this) < Double.EPSILON

@JvmName("modPI21")
fun Float.modPI2(): Float {
    var ang = this
    while (ang < -Double.PI) ang += (2.0 * Double.PI).toFloat()
    while (ang > Double.PI) ang -= (2.0 * Double.PI).toFloat()
    return ang
}

typealias PairOfFloats = Pair<Float, Float>

operator fun Float.plus(other: Vector2) = Vector2(this + other.x, this + other.y)
operator fun Float.minus(other: Vector2) = Vector2(this - other.x, this - other.y)
operator fun Float.times(other: Vector2) = Vector2(this * other.x, this * other.y)

operator fun Vector2.plus(other: Float) = Vector2(this.x + other, this.y + other)
operator fun Vector2.minus(other: Float) = Vector2(this.x - other, this.y - other)
operator fun Vector2.times(other: Float) = Vector2(this.x * other, this.y * other)

operator fun Vector2.plus(other: Vector2) = Vector2(this.x + other.x, this.y + other.y)
operator fun Vector2.minus(other: Vector2) = Vector2(this.x - other.x, this.y - other.y)
operator fun Vector2.times(other: Vector2) = Vector2(this.x * other.x, this.y * other.y)

operator fun Vector2.plus(other: PairOfFloats) = Vector2(this.x + other.first, this.y + other.second)
operator fun Vector2.minus(other: PairOfFloats) = Vector2(this.x - other.first, this.y - other.second)
operator fun Vector2.times(other: PairOfFloats) = Vector2(this.x * other.first, this.y * other.second)

operator fun PairOfFloats.plus(other: Vector2) = Vector2(this.first + other.x, this.second + other.y)
operator fun PairOfFloats.minus(other: Vector2) = Vector2(this.first - other.x, this.second - other.y)
operator fun PairOfFloats.times(other: Vector2) = Vector2(this.first * other.x, this.second * other.y)

operator fun Vector2.plusAssign(other: Float) { this.x += other; this.y += other }
operator fun Vector2.minusAssign(other: Float) { this.x -= other; this.y -= other }
operator fun Vector2.timesAssign(other: Float) { this.x *= other; this.y *= other }

operator fun Vector2.plusAssign(other: Vector2) { this.x += other.x; this.y += other.y }
operator fun Vector2.minusAssign(other: Vector2) { this.x -= other.x; this.y -= other.y }
operator fun Vector2.timesAssign(other: Vector2) { this.x *= other.x; this.y *= other.y }

operator fun Vector2.plusAssign(other: PairOfFloats) { this.x += other.first; this.y += other.second }
operator fun Vector2.minusAssign(other: PairOfFloats) { this.x -= other.first; this.y -= other.second }
operator fun Vector2.timesAssign(other: PairOfFloats) { this.x *= other.first; this.y *= other.second }

fun sincos(x: Float) = Pair(sin(x), cos(x))

val PairOfFloats.swapped: Pair<Float, Float>
    get() = Pair(this.second, this.first)