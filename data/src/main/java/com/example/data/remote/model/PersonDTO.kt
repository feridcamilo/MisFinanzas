package com.example.data.remote.model

import com.example.data.local.model.PersonVO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PersonaDTO(
    @SerializedName("getPersonaResult")
    val results: List<PersonVO>
) : Serializable
