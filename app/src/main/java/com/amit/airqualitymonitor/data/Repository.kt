package com.amit.airqualitymonitor.data

class Repository(private val localDataSource: DataSource) : DataSource {

    companion object {

        private var sINSTANCE: Repository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param remoteDataSource the backend data source
         *
         * @return the [Repository] instance
         */
        @JvmStatic
        fun getInstance(localDataSource: DataSource) =
            sINSTANCE ?: synchronized(Repository::class.java) {
                sINSTANCE ?: Repository(localDataSource)
                    .also { sINSTANCE = it }
            }


        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            sINSTANCE = null
        }
    }

    override fun getAllAqiData(callback: DataSource.GetAqiIndexCallback) {
        localDataSource.getAllAqiData(callback)
    }

    override fun getAqiDataGroupedByCity(callback: DataSource.GetAqiIndexCallback) {
        localDataSource.getAqiDataGroupedByCity(callback)
    }

    override fun getSpecificCityAqiData(city: String, callback: DataSource.GetAqiIndexCallback) {
        localDataSource.getSpecificCityAqiData(city, callback)
    }
}
