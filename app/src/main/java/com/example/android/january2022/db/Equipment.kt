package com.example.android.january2022.db

object Equipment {
    const val ATLAS_STONE = "Atlas Stone"
    const val BARBELL = "Barbell"
    const val BODYWEIGHT = "Bodyweight"
    const val BROOM_STICK = "Broom Stick"
    const val CABLE = "Cable"
    const val DUMBBELLS = "Dumbbell"
    const val KETTLEBELL = "Kettlebell"
    const val LEVER = "Lever"
    const val MACHINE = "Machine"
    const val MEDICINE_BALL = "Medicine Ball"
    const val RESISTANCE_BAND = "Resistance Band"
    const val ROPE = "Rope"
    const val ROPE_MACHINE = "Rope Machine"
    const val SLED = "Sled"
    const val SMITH_MACHINE = "Smith Machine"
    const val STABILITY_BALL = "Stability Ball"
    const val SUSPENSION = "Suspension"
    const val WEIGHT = "Weight"

    fun getAllEquipment(): List<String> {
        return listOf(
            ATLAS_STONE,
            BARBELL,
            BODYWEIGHT,
            BROOM_STICK,
            CABLE,
            DUMBBELLS,
            KETTLEBELL,
            LEVER,
            MACHINE,
            MEDICINE_BALL,
            RESISTANCE_BAND,
            ROPE,
            ROPE_MACHINE,
            SLED,
            SMITH_MACHINE,
            STABILITY_BALL,
            SUSPENSION,
            WEIGHT,
        )
    }
}
