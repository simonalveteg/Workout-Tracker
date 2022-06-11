package com.example.android.january2022.utils

import com.example.android.january2022.db.MuscleGroup

fun turnTargetIntoMuscleGroup(target: String): String {
    val muscleGroup = when (target.trim().replace("\n", " ")) {
        "Adductor Brevis" -> MuscleGroup.THIGHS
        "Adductor Longus" -> MuscleGroup.THIGHS
        "Adductor Magnus" -> MuscleGroup.THIGHS
        "Adductor Magnus, ischial fibers"-> MuscleGroup.THIGHS
        "Adductors"-> MuscleGroup.THIGHS
        "Adductors, Hip"-> MuscleGroup.HIPS
        "Aductor Brevis"-> MuscleGroup.THIGHS
        "Biceps Brachii"-> MuscleGroup.BICEPS
        "Biceps Brachii, Short Head"-> MuscleGroup.BICEPS
        "Brachialis"-> MuscleGroup.BICEPS // fett oklart
        "Brachioradialis"-> MuscleGroup.FOREARMS
        "Deltoid, Anterior"-> MuscleGroup.SHOULDERS
        "Deltoid, Lateral"-> MuscleGroup.SHOULDERS
        "Deltoid, Posterior"-> MuscleGroup.SHOULDERS
        "Erector Spinae"-> MuscleGroup.BACK
        "Erector Spinae, Cervicis & Capitis Fibers"-> MuscleGroup.BACK
        "Extensor Carpi Radialis"-> MuscleGroup.FOREARMS
        "Extensor Carpi Ulnaris"-> MuscleGroup.FOREARMS
        "External Hip Rotators"-> MuscleGroup.HIPS
        "Flexor Carpi Radialis"-> MuscleGroup.FOREARMS
        "Flexor Carpi Ulnaris"-> MuscleGroup.FOREARMS
        "Gastrocnemius"-> MuscleGroup.CALVES
        "Gemellus inferior"-> MuscleGroup.HIPS
        "Gemellus superior"-> MuscleGroup.HIPS
        "Gluteus Maximus"-> MuscleGroup.THIGHS // butt?
        "Gluteus Maximus, Lower Fibers"-> MuscleGroup.THIGHS
        "Gluteus Maximus, lower fibers"-> MuscleGroup.THIGHS
        "Gluteus Medius"-> MuscleGroup.THIGHS
        "Gluteus Minimus"-> MuscleGroup.THIGHS
        "Gluteus Minimus, Anterior Fibers"-> MuscleGroup.THIGHS
        "Gluteus maximus"-> MuscleGroup.THIGHS
        "Gluteus medius"-> MuscleGroup.THIGHS
        "Gluteus medius, posterior fibers"-> MuscleGroup.THIGHS
        "Gluteus minimus"-> MuscleGroup.THIGHS
        "Gluteus minimus, anterior fibers"-> MuscleGroup.THIGHS
        "Gracilis"-> MuscleGroup.THIGHS
        "Hamstrings"-> MuscleGroup.THIGHS
        "Hip Abductors"-> MuscleGroup.HIPS
        "Hip Adductors"-> MuscleGroup.HIPS
        "Hip External Rotators"-> MuscleGroup.HIPS
        "Hip Flexors"-> MuscleGroup.HIPS
        "Hip adductors"-> MuscleGroup.HIPS
        "Iliocastalis Lumborum"-> MuscleGroup.BACK
        "Iliocastalis Thoracis"-> MuscleGroup.BACK
        "Iliocastalis lumborum"-> MuscleGroup.BACK
        "Iliocastalis thoracis"-> MuscleGroup.BACK
        "Iliopsoas"-> MuscleGroup.HIPS
        "Infraspinatus"-> MuscleGroup.BACK
        "Latissimus Dorsi"-> MuscleGroup.BACK
        "Latissimus dorsi"-> MuscleGroup.BACK
        "Levator Scapulae"-> MuscleGroup.NECK // kanske shoulders
        "Levator scapulae"-> MuscleGroup.NECK
        "Obliques"-> MuscleGroup.ABDOMINALS
        "Obturator externus"-> MuscleGroup.HIPS
        "Obturator internus"-> MuscleGroup.HIPS
        "Pectineus"-> MuscleGroup.HIPS
        "Pectoralis Major, Clavicular"-> MuscleGroup.CHEST
        "Pectoralis Major, Sternal"-> MuscleGroup.CHEST
        "Pectoralis Major"-> MuscleGroup.CHEST
        "Pectoralis Minor"-> MuscleGroup.CHEST
        "Pectoralis major"-> MuscleGroup.CHEST
        "Pectoralis major (sternal head)"-> MuscleGroup.CHEST
        "Pectoralis minor "-> MuscleGroup.CHEST
        "Piriformis"-> MuscleGroup.HIPS
        "Popliteus"-> MuscleGroup.CALVES
        "Pronators"-> MuscleGroup.FOREARMS
        "Psoas Major"-> MuscleGroup.HIPS
        "Psoas major"-> MuscleGroup.HIPS
        "Quadratus Lumborum"-> MuscleGroup.BACK
        "Quadratus Femoris"-> MuscleGroup.HIPS
        "Quadratus femoris"-> MuscleGroup.HIPS
        "Quadratus lumborum"-> MuscleGroup.BACK
        "Quadriceps"-> MuscleGroup.THIGHS
        "Rectus Abdominis"-> MuscleGroup.ABDOMINALS
        "Rectus Femoris"-> MuscleGroup.THIGHS
        "Rectus abdominis"-> MuscleGroup.ABDOMINALS
        "Rhomboids"-> MuscleGroup.BACK
        "Sartorius"-> MuscleGroup.THIGHS
        "Serratus Anterior"-> MuscleGroup.SHOULDERS
        "Serratus Anterior, Inferior Digitations"-> MuscleGroup.SHOULDERS
        "Serratus Anterior, Lower Fibers"-> MuscleGroup.SHOULDERS
        "Soleus"-> MuscleGroup.CALVES
        "Splenius"-> MuscleGroup.NECK
        "Sternocleidomastoid"-> MuscleGroup.NECK
        "Sternocleidomastoid, Posterior Fibers"-> MuscleGroup.NECK
        "Subscapularis"-> MuscleGroup.SHOULDERS
        "Supraspinatus"-> MuscleGroup.SHOULDERS
        "Tensor Fasciae Latae"-> MuscleGroup.THIGHS
        "Tensor fasciae latae"-> MuscleGroup.THIGHS
        "Teres Major"-> MuscleGroup.SHOULDERS
        "Teres Minor"-> MuscleGroup.SHOULDERS
        "Tibialis Anterior"-> MuscleGroup.CALVES
        "Transverse Abdominis"-> MuscleGroup.ABDOMINALS
        "Trapezius, Lower"-> MuscleGroup.SHOULDERS
        "Trapezius, Middle"-> MuscleGroup.SHOULDERS
        "Trapezius, Upper"-> MuscleGroup.NECK
        "Trapezius, Upper (Part 1)"-> MuscleGroup.NECK
        "Trapezius, Upper (part I)"-> MuscleGroup.NECK
        "Trapezius, Upper (part II)"-> MuscleGroup.NECK
        "Trapezius, middle fibers"-> MuscleGroup.SHOULDERS
        "Triceps"-> MuscleGroup.TRICEPS
        "Triceps Brachii"-> MuscleGroup.TRICEPS
        "Triceps Brachii, Long Head"-> MuscleGroup.TRICEPS
        "Triceps Long Head"-> MuscleGroup.TRICEPS
        "Triceps brachii"-> MuscleGroup.TRICEPS
        "Triceps, Long Head"-> MuscleGroup.TRICEPS
        "Wrist Extensors"-> MuscleGroup.FOREARMS
        "Wrist Flexors"-> MuscleGroup.FOREARMS
        "Wrist/Finger Flexors"-> MuscleGroup.FOREARMS
        else -> ""
    }
    return muscleGroup
}