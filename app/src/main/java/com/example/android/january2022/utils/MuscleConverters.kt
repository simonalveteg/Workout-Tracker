package com.example.android.january2022.utils

import com.example.android.january2022.db.MuscleGroup
import com.example.android.january2022.db.entities.Exercise
import com.example.android.january2022.db.entities.SessionExerciseWithExercise
import timber.log.Timber

fun turnTargetIntoMuscleGroups(targets: List<String>): List<String> {
    return turnTargetIntoMuscleGroups(targets.joinToString("|"))
}

fun turnTargetIntoMuscleGroups(targets: String): List<String> {
    return targets.split("|").map { target ->
        when (target) {
            "Adductor Magnus, ischial fibers" -> MuscleGroup.HIPS
            "Adductors, Hip" -> MuscleGroup.HIPS
            "Back, General" -> MuscleGroup.BACK
            "Biceps Brachii" -> MuscleGroup.BICEPS
            "Brachialis" -> MuscleGroup.BICEPS
            "Brachioradialis" -> MuscleGroup.BICEPS
            "Deltoid, Anterior" -> MuscleGroup.SHOULDERS
            "Deltoid, Lateral" -> MuscleGroup.SHOULDERS
            "Deltoid, Posterior" -> MuscleGroup.SHOULDERS
            "Erector Spinae" -> MuscleGroup.BACK
            "Extensor Carpi Radialis" -> MuscleGroup.FOREARMS
            "Extensor Carpi Ulnaris" -> MuscleGroup.FOREARMS
            "External Hip Rotators" -> MuscleGroup.HIPS
            "Flexor Carpi Radialis" -> MuscleGroup.FOREARMS
            "Flexor Carpi Ulnaris" -> MuscleGroup.FOREARMS
            "Gastrocnemius" -> MuscleGroup.CALVES
            "Gluteus Maximus" -> MuscleGroup.GLUTES
            "Gluteus Medius" -> MuscleGroup.GLUTES
            "Gluteus Minimus" -> MuscleGroup.GLUTES
            "Gluteus Minimus, Anterior Fibers" -> MuscleGroup.GLUTES
            "Hamstrings" -> MuscleGroup.HAMSTRINGS
            "Hip Abductors" -> MuscleGroup.HIPS
            "Hip External Rotators" -> MuscleGroup.HIPS
            "Hip Internal Rotators" -> MuscleGroup.HIPS
            "Iliopsoas" -> MuscleGroup.HIPS
            "Infraspinatus" -> MuscleGroup.SHOULDERS
            "Latissimus Dorsi" -> MuscleGroup.BACK
            "Longus colli" -> MuscleGroup.BACK
            "Obliques" -> MuscleGroup.CORE
            "Pectoralis Major, Clavicular" -> MuscleGroup.CHEST
            "Pectoralis Major, Sternal" -> MuscleGroup.CHEST
            "Pectoralis Minor" -> MuscleGroup.CHEST
            "Piriformis" -> MuscleGroup.HIPS
            "Pronators" -> MuscleGroup.FOREARMS
            "Quadratus Femoris" -> MuscleGroup.QUADRICEPS
            "Quadriceps" -> MuscleGroup.QUADRICEPS
            "Rectus Abdominis" -> MuscleGroup.CORE
            "Rectus Femoris" -> MuscleGroup.QUADRICEPS
            "Rhomboids" -> MuscleGroup.BACK
            "Serratus Anterior" -> MuscleGroup.BACK
            "Soleus" -> MuscleGroup.CALVES
            "Splenius" -> MuscleGroup.BACK
            "Sternocleidomastoid" -> MuscleGroup.NECK
            "Subscapularis" -> MuscleGroup.SHOULDERS
            "Supinator" -> MuscleGroup.FOREARMS
            "Supraspinatus" -> MuscleGroup.SHOULDERS
            "Tensor Fasciae Latae" -> MuscleGroup.GLUTES
            "Teres Major" -> MuscleGroup.SHOULDERS
            "Teres Minor" -> MuscleGroup.SHOULDERS
            "Tibialis Anterior" -> MuscleGroup.CALVES
            "Transverse Abdominis" -> MuscleGroup.CORE
            "Trapezius, Lower" -> MuscleGroup.BACK
            "Trapezius, Middle" -> MuscleGroup.BACK
            "Trapezius, Upper" -> MuscleGroup.BACK
            "Triceps Brachii" -> MuscleGroup.TRICEPS
            "Triceps Brachii, Long Head" -> MuscleGroup.TRICEPS
            "Wrist Extensors" -> MuscleGroup.FOREARMS
            "Wrist Flexors" -> MuscleGroup.FOREARMS
            else -> "FAILURE".also { Timber.d("Failed with: $targets") }
        }
    }.distinct().filterNot { it == "FAILURE" }
}

@JvmName("sortedListOfMuscleGroupsForSessionExercises")
fun List<SessionExerciseWithExercise>.sortedListOfMuscleGroups(): List<String> {
    return this.map { it.exercise }.sortedListOfMuscleGroups()
}

@JvmName("sortedListOfMuscleGroupsForExercises")
fun List<Exercise>.sortedListOfMuscleGroups(): List<String> {
    return this.map { turnTargetIntoMuscleGroups(it.targets) }.flatten()
        .groupingBy { it }.eachCount().toList()
        .sortedByDescending { it.second }
        .map { it.first }
}
