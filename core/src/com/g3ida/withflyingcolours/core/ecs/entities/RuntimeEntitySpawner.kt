package com.g3ida.withflyingcolours.core.ecs.entities

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonReader
import games.rednblack.editor.renderer.data.*
import games.rednblack.editor.renderer.factory.EntityFactory
import games.rednblack.editor.renderer.utils.HyperJson
import ktx.collections.gdxMapOf
import ktx.collections.set


class RuntimeEntitySpawner(val entityFactory: EntityFactory): Disposable {

    private val mEntitiesModelsMap = gdxMapOf<String, MainItemVO>()

    fun loadModels() {
        val files = Gdx.files.local(entitiesFolder).list()
        val json = HyperJson.getJson()
        for (file in files) {
            val model = loadEntityModel(file, json)
            mEntitiesModelsMap[file.nameWithoutExtension()] = model
        }
    }

    private fun loadEntityModel(handle: FileHandle, json: Json): MainItemVO {
        val fileString = handle.readString()
        val jsonReader = JsonReader()
        val fileAsJson = jsonReader.parse(fileString)
        val className = fileAsJson.get("class")?.asString()
            ?: throw Exception("could not find 'class' attribute in ${handle.name()}")

        val mainItem = when(className) {
            "SimpleImageVO" -> json.fromJson(SimpleImageVO::class.java, fileString)
            "SpriteAnimationVO" -> json.fromJson(SpriteAnimationVO::class.java, fileString)
            "ParticleEffectVO" -> json.fromJson(ParticleEffectVO::class.java, fileString)
            "LightVO" -> json.fromJson(LightVO::class.java, fileString)
            "LabelVO" -> json.fromJson(LabelVO::class.java, fileString)
            "Image9patchVO" -> json.fromJson(Image9patchVO::class.java, fileString)
            "CompositeItemVO" -> json.fromJson(CompositeItemVO::class.java, fileString)
            else -> throw NotImplementedError("could not find $className class")
        }
        return mainItem
    }

    fun spawn(entityName: String): Int {
        return entityFactory.createEntity(0, mEntitiesModelsMap[entityName])
    }

    fun spawn(): Int {
        val model = SimpleImageVO()
        model.imageName = "default"
        model.itemIdentifier = "default"
        model.itemName = "default"
        return entityFactory.createEntity(0, model)
    }

    companion object {
        const val entitiesFolder = "entities/"
    }

    override fun dispose() {
        mEntitiesModelsMap.clear()
    }
}