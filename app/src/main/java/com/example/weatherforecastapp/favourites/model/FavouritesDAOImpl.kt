//package com.example.weatherforecastapp.favourites.model
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import androidx.room.TypeConverters
//import com.example.weatherforecastapp.Data.FavouritesResp
//
////@Database(entities = [FavouritesResp::class], version = 1)
////abstract class FavouritesDAOImpl: RoomDatabase() {
////
////    abstract fun getFavouritesDAO() : FavouritesDAO
////
////    companion object{
////
////        @Volatile
////        private var instance: FavouritesDAOImpl? = null
////
////        fun getInstance(context: Context): FavouritesDAOImpl {
////            return instance ?: synchronized(this){
////                val INSTANCE = Room.databaseBuilder(context.applicationContext, FavouritesDAOImpl::class.java, "Favourites").build()
////                instance = INSTANCE
////                INSTANCE
////            }
////        }
////    }
////}
//
//
//@Database(entities = [FavouritesResp::class], version = 1, exportSchema = false)
////@TypeConverters(Converters::class)
//
//abstract class FavouritesDAOImpl : RoomDatabase() {
//    abstract fun getFavouritesDAO(): FavouritesDAO
//
//    companion object {
//        @Volatile
//        private var instance: FavouritesDAOImpl? = null
//
//        fun getInstance(context: Context): FavouritesDAOImpl {
//            return instance ?: synchronized(this) {
//                val INSTANCE = Room.databaseBuilder(
//                    context.applicationContext,
//                    FavouritesDAOImpl::class.java,
//                    "Favourites"
//                ).build()
//                instance = INSTANCE
//                INSTANCE
//            }
//        }
//    }
//}