package com.alveteg.simon.workouts.utils

import com.alveteg.simon.workouts.db.MuscleGroup
import com.alveteg.simon.workouts.db.entities.Exercise
import com.alveteg.simon.workouts.db.entities.SessionExerciseWithExercise
import timber.log.Timber

fun turnTargetIntoMuscleGroups(targets: List<String>): List<String> {
  return turnTargetIntoMuscleGroups(targets.joinToString("|"))
}

fun turnTargetIntoMuscleGroups(targets: String): List<String> {
  return targets.split("|").filterNot { it.isBlank() }.map { target ->
    when (target.trim()) {
      "Adductor Brevis" -> MuscleGroup.HIPS
      "Adductor Longus" -> MuscleGroup.HIPS
      "Adductor Magnus, ischial fibers" -> MuscleGroup.HIPS
      "Adductor Magnus" -> MuscleGroup.HIPS
      "Adductors" -> MuscleGroup.HIPS
      "Adductors, Hip" -> MuscleGroup.HIPS
      "Back, General" -> MuscleGroup.BACK
      "Biceps Brachii" -> MuscleGroup.BICEPS
      "Biceps Brachii, Short Head" -> MuscleGroup.BICEPS
      "Brachialis" -> MuscleGroup.BICEPS
      "Brachioradialis" -> MuscleGroup.BICEPS
      "Deltoid, Anterior" -> MuscleGroup.SHOULDERS
      "Deltoid, Lateral" -> MuscleGroup.SHOULDERS
      "Deltoid, Posterior" -> MuscleGroup.SHOULDERS
      "Erector Spinae" -> MuscleGroup.BACK
      "Erector Spinae, Cervicis & Capitis Fibers" -> MuscleGroup.NECK
      "Extensor Carpi Radialis" -> MuscleGroup.FOREARMS
      "Extensor Carpi Ulnaris" -> MuscleGroup.FOREARMS
      "External Hip Rotators" -> MuscleGroup.HIPS
      "Flexor Carpi Radialis" -> MuscleGroup.FOREARMS
      "Flexor Carpi Ulnaris" -> MuscleGroup.FOREARMS
      "Gastrocnemius" -> MuscleGroup.CALVES
      "Gemellus inferior" -> MuscleGroup.HIPS
      "Gemellus superior" -> MuscleGroup.HIPS
      "Gluteus Maximus" -> MuscleGroup.GLUTES
      "Gluteus Maximus, Lower Fibers" -> MuscleGroup.GLUTES
      "Gluteus Medius" -> MuscleGroup.GLUTES
      "Gluteus Medius, Posterior fibers" -> MuscleGroup.GLUTES
      "Gluteus Minimus" -> MuscleGroup.GLUTES
      "Gluteus Minimus, Anterior Fibers" -> MuscleGroup.GLUTES
      "Gracilis" -> MuscleGroup.HIPS
      "Hamstrings" -> MuscleGroup.HAMSTRINGS
      "Hip Abductors" -> MuscleGroup.HIPS
      "Hip Adductors" -> MuscleGroup.HIPS
      "Hip External Rotators" -> MuscleGroup.HIPS
      "Hip Internal Rotators" -> MuscleGroup.HIPS
      "Iliocastalis Lumborum" -> MuscleGroup.BACK
      "Iliocastalis Thoracis" -> MuscleGroup.BACK
      "Iliopsoas" -> MuscleGroup.HIPS
      "Infraspinatus" -> MuscleGroup.SHOULDERS
      "Latissimus Dorsi" -> MuscleGroup.BACK
      "Levator Scapulae" -> MuscleGroup.BACK
      "Longus capitis" -> MuscleGroup.NECK
      "Longus colli" -> MuscleGroup.BACK
      "Obliques" -> MuscleGroup.CORE
      "Obturator externus" -> MuscleGroup.HIPS
      "Obturator internus" -> MuscleGroup.HIPS
      "Pectineus" -> MuscleGroup.HIPS
      "Pectoralis Major" -> MuscleGroup.CHEST
      "Pectoralis Major, Clavicular" -> MuscleGroup.CHEST
      "Pectoralis Major, Sternal" -> MuscleGroup.CHEST
      "Pectoralis Major, Sternal " -> MuscleGroup.CHEST
      "Pectoralis Minor" -> MuscleGroup.CHEST
      "Piriformis" -> MuscleGroup.HIPS
      "Popliteus" -> MuscleGroup.HAMSTRINGS
      "Pronators" -> MuscleGroup.FOREARMS
      "Psoas Major" -> MuscleGroup.HIPS
      "Quadratus Femoris" -> MuscleGroup.QUADRICEPS
      "Quadratus Lumborum" -> MuscleGroup.BACK
      "Quadriceps" -> MuscleGroup.QUADRICEPS
      "Rectus Abdominis" -> MuscleGroup.CORE
      "Rectus Capitus" -> MuscleGroup.NECK
      "Rectus Femoris" -> MuscleGroup.QUADRICEPS
      "Rhomboids" -> MuscleGroup.BACK
      "Sartorius" -> MuscleGroup.HIPS
      "Serratus Anterior" -> MuscleGroup.BACK
      "Serratus Anterior, Inferior Digitations" -> MuscleGroup.BACK
      "Serratus Anterior, Lower Fibers" -> MuscleGroup.BACK
      "Soleus" -> MuscleGroup.CALVES
      "Splenius" -> MuscleGroup.BACK
      "Sternocleidomastoid" -> MuscleGroup.NECK
      "Sternocleidomastoid, Posterior Fibers" -> MuscleGroup.NECK
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
      else -> "FAILURE".also { Timber.d("Failed with: $target") }
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
