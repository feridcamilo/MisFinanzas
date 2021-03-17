package com.android.data.remote.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

@Serializable
data class MovementDTO(
    @SerialName("IdMovimiento")
    val id: Int,
    @SerialName("Valor") @Serializable(with = BigDecimalSerializer::class)
    val value: BigDecimal,
    @SerialName("Descripcion")
    val description: String,
    @SerialName("FechaMovimiento")
    val date: String,
    @SerialName("FechaIngreso")
    val dateEntry: String? = null,
    @SerialName("FechaActualizacion")
    val dateUpdate: String?,
    @SerialName("IdTipoMovimiento")
    val idMovType: Int,
    @SerialName("IdCategoria")
    val idCategory: Int? = null,
    @SerialName("IdDeuda")
    val idDebt: Int? = null,
    @SerialName("IdPersona")
    val idPerson: Int? = null,
    @SerialName("IdLugar")
    val idPlace: Int? = null
)

object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): BigDecimal = BigDecimal(decoder.decodeString())
    override fun serialize(encoder: Encoder, value: BigDecimal) = encoder.encodeString(value.toPlainString())
}
