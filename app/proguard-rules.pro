# 1. Essential attributes for GSON & Reflection
# 'Signature' prevents the TypeToken error by keeping generic type info
# 'InnerClasses' and 'EnclosingMethod' are required for library-based deserializers
-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses

# 2. Keep GSON internal classes
-keep class com.google.gson.** { *; }

# 3. Keep your Legacy Data Models
# We must keep the classes AND their fields so GSON can map them
-keep class com.alveteg.simon.workouts.ui.OldDatabaseModel { *; }
-keep class com.alveteg.simon.workouts.ui.OldGymSet { *; }

# 4. Keep your Database Entities (referenced inside OldDatabaseModel)
-keep class com.alveteg.simon.workouts.db.entities.** { *; }

# 5. Keep the JavaTime Serializer library
# This prevents the ExceptionInInitializerError when calling Converters.registerAll
-keep class com.fatboyindustrial.gsonjavatime.** { *; }

# 6. Room Database support
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**
