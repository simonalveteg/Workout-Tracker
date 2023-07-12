package com.example.android.january2022.utils

import com.example.android.january2022.db.MuscleGroup
import timber.log.Timber

fun turnTargetIntoMuscleGroups(targets: List<String>): List<String> {
  return turnTargetIntoMuscleGroups(targets.joinToString("|"))
}

fun turnTargetIntoMuscleGroups(targets: String): List<String> {
  return targets.split("|").map {
    when (it.trim().replace("\n", "").replace(" ", "").lowercase()) {
      "adductorbrevis" -> MuscleGroup.THIGHS
      "adductorlongus" -> MuscleGroup.THIGHS
      "adductormagnus" -> MuscleGroup.THIGHS
      "adductormagnus,ischialfibers" -> MuscleGroup.THIGHS
      "adductors" -> MuscleGroup.THIGHS
      "adductors,hip" -> MuscleGroup.HIPS
      "aductorbrevis" -> MuscleGroup.THIGHS
      "back,general" -> MuscleGroup.BACK
      "bicepsbrachii" -> MuscleGroup.BICEPS
      "bicepsbrachii,shorthead" -> MuscleGroup.BICEPS
      "brachialis" -> MuscleGroup.BICEPS
      "brachioradialis" -> MuscleGroup.FOREARMS
      "deltoid,anterior" -> MuscleGroup.SHOULDERS
      "deltoid,lateral" -> MuscleGroup.SHOULDERS
      "deltoid,posterior" -> MuscleGroup.SHOULDERS
      "erectorspinae" -> MuscleGroup.BACK
      "erectorspinae,cervicis&capitisfibers" -> MuscleGroup.BACK
      "extensorcarpiradialis" -> MuscleGroup.FOREARMS
      "extensorcarpiulnaris" -> MuscleGroup.FOREARMS
      "externalhiprotators" -> MuscleGroup.HIPS
      "flexorcarpiradialis" -> MuscleGroup.FOREARMS
      "flexorcarpiulnaris" -> MuscleGroup.FOREARMS
      "gastrocnemius" -> MuscleGroup.CALVES
      "gemellusinferior" -> MuscleGroup.HIPS
      "gemellussuperior" -> MuscleGroup.HIPS
      "general,back" -> MuscleGroup.BACK
      "gluteusmaximus" -> MuscleGroup.THIGHS
      "gluteusmaximus,lowerfibers" -> MuscleGroup.THIGHS
      "gluteusmedius" -> MuscleGroup.THIGHS
      "gluteusmedius,posteriorfibers" -> MuscleGroup.THIGHS
      "gluteusminimus" -> MuscleGroup.THIGHS
      "gluteusminimus,anteriorfibers" -> MuscleGroup.THIGHS
      "gracilis" -> MuscleGroup.THIGHS
      "hamstrings" -> MuscleGroup.THIGHS
      "hipabductors" -> MuscleGroup.HIPS
      "hipabductors(listedbelow)" -> MuscleGroup.HIPS
      "hipabductors(opposite)" -> MuscleGroup.HIPS
      "hipadductors" -> MuscleGroup.HIPS
      "hipexternalrotators" -> MuscleGroup.HIPS
      "hipexternalrotators(listedbelow)" -> MuscleGroup.HIPS
      "hipflexors" -> MuscleGroup.HIPS
      "hipinternalrotators(listedbelow)" -> MuscleGroup.HIPS
      "iliocastalislumborum" -> MuscleGroup.BACK
      "iliocastalisthoracis" -> MuscleGroup.BACK
      "iliopsoas" -> MuscleGroup.HIPS
      "infraspinatus" -> MuscleGroup.BACK
      "latissimusdorsi" -> MuscleGroup.BACK
      "levatorscapulae" -> MuscleGroup.SHOULDERS
      "longuscapitis" -> MuscleGroup.NECK
      "longuscolli" -> MuscleGroup.NECK
      "obliques" -> MuscleGroup.ABDOMINALS
      "obturatorexternus" -> MuscleGroup.HIPS
      "obturatorinternus" -> MuscleGroup.HIPS
      "pectineus" -> MuscleGroup.HIPS
      "pectoralismajor" -> MuscleGroup.CHEST
      "pectoralismajor(sternalhead)" -> MuscleGroup.CHEST
      "pectoralismajor,clavicular" -> MuscleGroup.CHEST
      "pectoralismajor,sternal" -> MuscleGroup.CHEST
      "pectoralisminor" -> MuscleGroup.CHEST
      "piriformis" -> MuscleGroup.HIPS
      "popliteus" -> MuscleGroup.CALVES
      "pronators" -> MuscleGroup.FOREARMS
      "psoasmajor" -> MuscleGroup.HIPS
      "quadratusfemoris" -> MuscleGroup.HIPS
      "quadratuslumborum" -> MuscleGroup.BACK
      "quadriceps" -> MuscleGroup.THIGHS
      "rectusabdominis" -> MuscleGroup.ABDOMINALS
      "rectuscapitus" -> MuscleGroup.NECK
      "rectusfemoris" -> MuscleGroup.THIGHS
      "rhomboids" -> MuscleGroup.BACK
      "sartorius" -> MuscleGroup.THIGHS
      "serratusanterior" -> MuscleGroup.SHOULDERS
      "serratusanterior,inferiordigitations" -> MuscleGroup.SHOULDERS
      "serratusanterior,lowerfibers" -> MuscleGroup.SHOULDERS
      "soleus" -> MuscleGroup.CALVES
      "splenius" -> MuscleGroup.NECK
      "sternocleidomastoid" -> MuscleGroup.NECK
      "sternocleidomastoid,posteriorfibers" -> MuscleGroup.NECK
      "subscapularis" -> MuscleGroup.SHOULDERS
      "supinator" -> MuscleGroup.FOREARMS
      "supraspinatus" -> MuscleGroup.SHOULDERS
      "tensorfasciaelatae" -> MuscleGroup.THIGHS
      "teresmajor" -> MuscleGroup.SHOULDERS
      "teresminor" -> MuscleGroup.SHOULDERS
      "tibialisanterior" -> MuscleGroup.CALVES
      "transverseabdominis" -> MuscleGroup.ABDOMINALS
      "compressesandsupportsabdominalviscera" -> MuscleGroup.ABDOMINALS
      "trapezius,lower" -> MuscleGroup.SHOULDERS
      "trapezius,middle" -> MuscleGroup.SHOULDERS
      "trapezius,middlefibers" -> MuscleGroup.SHOULDERS
      "trapezius,upper" -> MuscleGroup.NECK
      "trapezius,upper(part1)" -> MuscleGroup.NECK
      "trapezius,upper(parti)" -> MuscleGroup.NECK
      "trapezius,upper(partii)" -> MuscleGroup.NECK
      "triceps" -> MuscleGroup.TRICEPS
      "triceps,longhead" -> MuscleGroup.TRICEPS
      "tricepsbrachii" -> MuscleGroup.TRICEPS
      "tricepsbrachii,longhead" -> MuscleGroup.TRICEPS
      "tricepslonghead" -> MuscleGroup.TRICEPS
      "wrist/fingerflexors" -> MuscleGroup.FOREARMS
      "wristextensors" -> MuscleGroup.FOREARMS
      "wristflexors" -> MuscleGroup.FOREARMS
      else -> "FAILURE".also { Timber.d("Target: $targets") }
    }
  }.distinct().filterNot { it == "FAILURE" }
}