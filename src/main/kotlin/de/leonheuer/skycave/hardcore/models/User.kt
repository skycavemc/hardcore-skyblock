package de.leonheuer.skycave.hardcore.models

import org.bson.types.ObjectId
import java.util.UUID

data class User(
    val id: ObjectId,
    val uuid: UUID,
    var kills: Long,
    var deaths: Long,
)
