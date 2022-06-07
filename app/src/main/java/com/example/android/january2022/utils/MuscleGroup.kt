package com.example.android.january2022.utils

import com.example.android.january2022.db.MuscleGroup

fun turnTargetIntoMuscleGroup(target: String): String {
    val muscleGroup = when (target.trim().replace("\n", "")) {
        "Sternocleidomastoid", "Splenius" -> "Neck"
        "Deltoid, Anterior", "Deltoid, Posterior", "Deltoid, Lateral", "Supraspinatus" -> MuscleGroup.SHOULDERS
        "Biceps Brachii", "Brachialis" -> MuscleGroup.BICEPS
        "Triceps Brachii" -> MuscleGroup.TRICEPS
        "Brachioradialis", "Wrist Flexors", "Wrist Extensors", "Pronators", "Supinators" -> MuscleGroup.FOREARMS
        "Latissimus Dorsi", "Teres Major", "Trapezius, Upper", "Trapezius, Middle", "Trapezius, Lower",
        "Levator Scapulae", "Rhomboids", "Infraspinatus", "Teres Minor", "Subscapularis" -> MuscleGroup.BACK
        "Pectoralis Minor", "Pectoralis Major, Sternal", "Pectoralis Major, Clavicular",
        "Serratus Anterior, Inferior Digitations", "Serratus Anterior" -> MuscleGroup.CHEST
        "Rectus Abdominis", "Transverse Abdominis", "Obliques", "Quadratus Lumborum", "Erector Spinae" -> MuscleGroup.ABDOMINALS
        "Gluteus Maximus", "Gluteus Medius", "Gluteus Minimus", "Gluteus Minimus, anterior fibers",
        "Iliopsoas", "Sartorius", "Rectus Femoris", "Tensor Fasciae Latae", "Pectineus", "External Hip Rotators" -> MuscleGroup.HIPS
        "Quadriceps", "Hamstrings", "Hip Adductors" -> MuscleGroup.THIGHS
        "Gastrocnemius", "Soleus", "Tibialis Anterior", "Popliteus" -> MuscleGroup.CALVES
        else -> ""
    }
    return muscleGroup
}