package com.g3ida.withflyingcolours.core.ecs.entities

import com.badlogic.gdx.physics.box2d.Contact
import com.g3ida.withflyingcolours.core.scripts.ContactSpark
import games.rednblack.editor.renderer.utils.ItemWrapper

fun RuntimeEntitySpawner.spawnContactSpark(contact: Contact): Int {
    val entityId = this.spawn()
    val engine = this.entityFactory.engine
    val script = ContactSpark(engine, this.entityFactory.world, contact)
    val itemWrapper = ItemWrapper(entityId, engine)
    itemWrapper.addScript(script)
    return entityId
}