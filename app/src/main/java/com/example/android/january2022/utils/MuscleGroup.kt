package com.example.android.january2022.utils

fun turnTargetIntoMuscleGroup(target: String): String {
    val muscleGroup = when (target.trim().replace("\n", "")) {
        "Sternocleidomastoid", "Splenius" -> "Neck"
        "Deltoid, Anterior", "Deltoid, Posterior", "Deltoid, Lateral", "Supraspinatus" -> "Shoulders"
        "Triceps Brachii", "Biceps Brachii", "Brachialis" -> "Upper Arms"
        "Brachioradialis", "Wrist Flexors", "Wrist Extensors", "Pronators", "Supinators" -> "Forearms"
        "Latissimus Dorsi", "Teres Major", "Trapezius, Upper", "Trapezius, Middle", "Trapezius, Lower",
        "Levator Scapulae", "Rhomboids", "Infraspinatus", "Teres Minor", "Subscapularis" -> "Back"
        "Pectoralis Minor", "Pectoralis Major, Sternal", "Pectoralis Major, Clavicular",
        "Serratus Anterior, Inferior Digitations", "Serratus Anterior" -> "Chest"
        "Rectus Abdominis", "Transverse Abdominis", "Obliques", "Quadratus Lumborum", "Erector Spinae" -> "Waist"
        "Gluteus Maximus", "Gluteus Medius", "Gluteus Minimus", "Gluteus Minimus, anterior fibers",
        "Iliopsoas", "Sartorius", "Rectus Femoris", "Tensor Fasciae Latae", "Pectineus", "External Hip Rotators" -> "Hips"
        "Quadriceps", "Hamstrings", "Hip Adductors" -> "Thighs"
        "Gastrocnemius", "Soleus", "Tibialis Anterior", "Popliteus" -> "Calves"
        else -> ""
    }
    return muscleGroup
}