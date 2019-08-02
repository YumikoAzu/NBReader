package com.example.newbiechen.nbreader.data.repository.impl

import com.example.newbiechen.nbreader.data.entity.CatalogEntity
import io.reactivex.Flowable

interface ICatalogRepository {
    fun getCatalogItems(): Flowable<List<CatalogEntity>>
    fun saveCatalogItems(entities: List<CatalogEntity>)
}